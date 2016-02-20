package se.ebbman.estypes.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ebbman.estypes.annotations.ESType;

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

    private Mapping() {
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
    ESTypedClass parseAnnotatedClass(Class<?> clazz) {
        ESTypedClass esTyped = new ESTypedClass();
        for (Field field : clazz.getDeclaredFields()) {
            FieldProperties fieldProperty = new FieldProperties();
            fieldProperty.populatePropertiesFromField(field);
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

    @VisibleForTesting
    static Mapping getInstance() {
        return new Mapping();
    }
}
