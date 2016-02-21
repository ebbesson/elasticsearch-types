package se.ebbman.estypes.mapping;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ebbman.estypes.annotations.DateType;
import se.ebbman.estypes.annotations.NumericType;
import se.ebbman.estypes.annotations.StringType;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 17, 2016 - 5:36:27 PM
 */
public class FieldProperties {

    private static final Logger log = LoggerFactory.getLogger(FieldProperties.class);
    private final Map<String, Object> props;
    private final Set<String> objectMethods;

    public FieldProperties() {
        this.props = new HashMap<>();
        Object o = new Object();
        objectMethods = Arrays.asList(o.getClass().getMethods())
                .stream()
                .map(Method::getName)
                .collect(Collectors.toSet());
        objectMethods.add("annotationType");
    }

    public ImmutableMap<String, Object> getProperties() {
        return ImmutableMap.copyOf(props);
    }

    @VisibleForTesting
    void populatePropertiesFromField(Field field) {

        if (field.isAnnotationPresent(StringType.class)) {
            StringType stringField = field.getAnnotation(StringType.class);
            parseAnnotationMethodsForProperties(stringField.getClass().getDeclaredMethods(), stringField, StringType.class);

        } else if (field.isAnnotationPresent(NumericType.class)) {
            NumericType numericField = field.getAnnotation(NumericType.class);
            parseAnnotationMethodsForProperties(numericField.getClass().getDeclaredMethods(), numericField, NumericType.class);

        } else if (field.isAnnotationPresent(DateType.class)) {
            DateType dateField = field.getAnnotation(DateType.class);
            parseAnnotationMethodsForProperties(dateField.getClass().getDeclaredMethods(), dateField, DateType.class);
        } else {
            log.debug("Declared field does not carry any cool annotation");
        }

    }

    private void parseAnnotationMethodsForProperties(Method[] declaredMethods, Annotation typeAnnotationInstance, Class<?> instanceClazz) {

        for (Method method : declaredMethods) {
            if (method.getParameterCount() == 0 && !objectMethods.contains(method.getName())) {
                try {
                    Object methodReturnValue = method.invoke(typeAnnotationInstance);
                    if (method.getName().equalsIgnoreCase("type") || (!isDefaultReturnValue(method.getName(), methodReturnValue, instanceClazz))) {
                        props.put(method.getName(), methodReturnValue);
                    }
                } catch (IllegalArgumentException ex) {
                    log.error("Exception caught, unable to add properties data for " + method.getName() + " reason: ", ex);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                }
            }
        }

    }

    private boolean isDefaultReturnValue(String methodName, Object instanceMethodReturnValue, Class<?> instanceClazz) {
        try {
            Object defaultValue = instanceClazz.getMethod(methodName).getDefaultValue();
            return instanceMethodReturnValue.equals(defaultValue);
        } catch (NoSuchMethodException | SecurityException ex) {
            return false;
        }

    }
}
