package se.ebbman.estypes.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
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
    private static final Logger log = LoggerFactory.getLogger(Mapping.class);

    private Mapping(String packageScope) {
        this.fields = scanForESTypedClasses(packageScope);
    }

    @VisibleForTesting
    Mapping() {
        this.fields = null;
    }

    @VisibleForTesting
    void setFields(Map<String, ESTypedClass> fields) {
        this.fields = fields;
    }

    /**
     * Scanns your runtime packages for classes annotated with {@link ESType} and produces an Elasticsearch compatiable Mapping of those
     * objects
     *
     * @param packageScope Top-level package where the classes
     * @return JSON-formatted String representing Elasticsearch field mappings of all your {@link ESType} annotated classes
     */
    public static String getMapping(String packageScope) {
        Mapping mapping = new Mapping(packageScope);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(mapping);
        } catch (JsonProcessingException ex) {
            log.error("JsonProcessingException cought, returning empty mapping", ex);
        }
        return "{\"mapping\":{}}";
    }

    public Map<String, ESTypedClass> getFields() {
        return fields;
    }

    @VisibleForTesting
    Map<String, ESTypedClass> scanForMappings(String packageScope) throws IOException {
        final ClassLoader loader = Mapping.class.getClassLoader();

        Map<String, ESTypedClass> fieldMappings = new HashMap<>();
        ClassPath classPath = ClassPath.from(loader);
        Set<Class<?>> esTypeAnnotatedClasses = classPath.getTopLevelClassesRecursive(packageScope)
                .stream()
                .filter((ClassInfo clazzInfo) -> clazzInfo.load().isAnnotationPresent(ESType.class))
                .map(ClassInfo::load)
                .collect(Collectors.toSet());

        esTypeAnnotatedClasses.forEach((Class<?> clazz) -> {
            ESTypedClass esTyped = parseAnnotatedClass(clazz);
            ESType esTypeAnnotation = clazz.getAnnotation(ESType.class);
            fieldMappings.put(esTypeAnnotation.type(), esTyped);
        });
        return fieldMappings;
    }

    @VisibleForTesting
    void populatePropertiesFromField(FieldProperty fieldProperty, Method[] declaredFieldMethods, Annotation fieldTypeAnnotation) {
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

    @VisibleForTesting
    ESTypedClass parseAnnotatedClass(Class<?> clazz) {

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

        return esTyped;
    }

    private Map<String, ESTypedClass> scanForESTypedClasses(String packageScope) {
        try {
            return scanForMappings(packageScope);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return ImmutableMap.of();

    }

}
