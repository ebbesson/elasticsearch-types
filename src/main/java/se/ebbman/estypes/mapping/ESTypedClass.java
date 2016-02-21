package se.ebbman.estypes.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Magnus Ebbesson
 * Feb 16, 2016 - 3:26:49 PM
 */
public class ESTypedClass {

    private final Map<String, FieldProperties> properties;

    public ESTypedClass() {
        this.properties = new HashMap<>();
    }

    public Map<String, FieldProperties> getProperties() {
        return properties;
    }

    public void addFieldProperty(String field, FieldProperties fieldProps) {
        properties.put(field, fieldProps);
    }

}
