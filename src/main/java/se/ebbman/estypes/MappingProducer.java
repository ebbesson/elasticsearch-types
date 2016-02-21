package se.ebbman.estypes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ebbman.estypes.annotations.ESType;
import se.ebbman.estypes.mapping.ESTypedClass;
import se.ebbman.estypes.mapping.FieldProperties;

/**
 *
 * @author Magnus Ebbesson
 * Feb 17, 2016 - 3:49:55 PM
 */
public class MappingProducer {

    private static final Logger log = LoggerFactory.getLogger(MappingProducer.class);

    private MappingProducer() {
    }

    /**
     * Scanns your runtime packages for classes annotated with {@link ESType} and produces an Elasticsearch compatiable MappingProducer of
     * those
     * objects
     *
     * @param packageScope Top-level package where the classes
     * @return JSON-formatted String representing Elasticsearch field mappings of all your {@link ESType} annotated classes
     */
    public static Map<String, String> getMapping(String packageScope) {

        Map<String, String> mapOfJsonRepresentationsForEachType = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Map.Entry<String, ESTypedClass> entry : scanForESTypedClasses(packageScope).entrySet()) {
            try {
                mapOfJsonRepresentationsForEachType.put(entry.getKey(), mapper.writeValueAsString(entry.getValue()));
            } catch (JsonProcessingException ex) {
                log.error("JsonProcessingException cought, returning empty mapping", ex);
            }
        }

        return mapOfJsonRepresentationsForEachType;
    }

    static Map<String, ESTypedClass> scanForESTypedClasses(String packageScope) {

        Map<String, ESTypedClass> fieldMappings = new HashMap<>();

        Set<Class<?>> esTypeAnnotatedClasses = getClasses(packageScope)
                .stream()
                .filter((Class<?> clazz) -> clazz.isAnnotationPresent(ESType.class))
                .collect(Collectors.toSet());

        esTypeAnnotatedClasses.forEach((Class<?> clazz) -> {
            ESTypedClass esTyped = parseAnnotatedClass(clazz);
            ESType esTypeAnnotation = clazz.getAnnotation(ESType.class);
            fieldMappings.put(esTypeAnnotation.name(), esTyped);
        });

        return fieldMappings;
    }

    static ESTypedClass parseAnnotatedClass(Class<?> clazz) {
        ESTypedClass esTyped = new ESTypedClass();
        for (Field field : clazz.getDeclaredFields()) {
            FieldProperties fieldProperty = new FieldProperties();
            fieldProperty.populatePropertiesFromField(field);
            if (fieldProperty.hasProperties()) {
                esTyped.addFieldProperty(field.getName(), fieldProperty);
            }

        }
        return esTyped;
    }

    private static Set<Class<?>> getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;

        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException ex) {
            log.error("Unable to read classpath due to ", ex);
            return classes;
        }
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        dirs.stream().forEach((directory) -> {
            try {
                classes.addAll(findClasses(directory, packageName));
            } catch (ClassNotFoundException ex) {
                // pass
            }
        });
        return classes;
    }

    private static Collection<? extends Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists()) {
            return classes;
        }
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }

        }
        return classes;
    }

}
