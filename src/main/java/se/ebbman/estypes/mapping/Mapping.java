package se.ebbman.estypes.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ebbman.estypes.annotations.DateType;
import se.ebbman.estypes.annotations.ESType;
import se.ebbman.estypes.annotations.NumericType;
import se.ebbman.estypes.annotations.StringType;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 17, 2016 - 3:49:55 PM
 */
public class Mapping {

    private Map<String, ESTypedClass> fields;
    private static final Logger log = LoggerFactory.getLogger(Mapping.class);;

    private Mapping() {
        try {
            this.fields = scanForMappings();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        if(this.fields == null){
            this.fields = ImmutableMap.of();
        }
    }

    public static String getMapping(){
        Mapping mapping = new Mapping();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(mapping);
        } catch (JsonProcessingException ex) {
            log.error("JsonProcessingException cought, returning empty mapping", ex);
        }
        return "{\"mapping\":{}}";
        
    }



    private Map<String, ESTypedClass> scanForMappings() throws IOException {
        final ClassLoader loader = Mapping.class.getClassLoader();

        Map<String, ESTypedClass> fieldMappings = new HashMap<>();
        ClassPath classPath = ClassPath.from(loader);
        Set<Class<?>> esTypeAnnotatedClasses = classPath.getTopLevelClasses("se.ebbman.wattsup.api.dbo")
                .parallelStream()
                .filter((ClassInfo clazzInfo) -> {
                    Class<?> clazz = clazzInfo.load();
                    boolean isEsType = clazz.isAnnotationPresent(ESType.class);
                    return isEsType;
                        })
                .map(ClassInfo::load)
                .collect(Collectors.toSet());

        esTypeAnnotatedClasses.forEach((Class<?> clazz) -> {

            ESTypedClass esTyped = new ESTypedClass();

            for (Field field : clazz.getDeclaredFields()) {
                FieldProperty fieldProperty = new FieldProperty();
                if (field.isAnnotationPresent(StringType.class)) {
                    StringType stringField = field.getAnnotation(StringType.class);
                    populatePropertiesFromField(fieldProperty, stringField.getClass().getDeclaredMethods(), stringField);

                } else if (field.isAnnotationPresent(NumericType.class)) {
                    NumericType numericField = field.getAnnotation(NumericType.class);
                    populatePropertiesFromField(fieldProperty, numericField.getClass().getDeclaredMethods(), numericField);
                } else if (field.isAnnotationPresent(DateType.class)) {
                    DateType dateField = field.getAnnotation(DateType.class);
                    populatePropertiesFromField(fieldProperty, dateField.getClass().getDeclaredMethods(), dateField);
                } else {
                    log.debug("Declared field does not carry any cool annotation");
                }
                esTyped.addFieldProperty(field.getName(), fieldProperty);
            }

            ESType esTypeAnnotation = clazz.getAnnotation(ESType.class);
            fieldMappings.put(esTypeAnnotation.type(), esTyped);
        });
        return fieldMappings;
    }

    private void populatePropertiesFromField(FieldProperty fieldProperty, Method[] declaredFieldMethods, Annotation fieldTypeAnnotation) {
        for (Method method : declaredFieldMethods) {
            if (method.getParameterCount() == 0) {
                try {
                    fieldProperty.addFieldProperty(method.getName(), method.invoke(fieldTypeAnnotation));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    log.error("Exception caught, unable to add properties data for " + method.getName() + " reason: ", ex);
                }
            }
        }
    }

}
