
package se.ebbman.estypes.mapping;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 16, 2016 - 3:26:49 PM
 */
public class ESTypedClass {

    private static final String PROPERTIES_KEY = "properties";
    private final Map<String,FieldProperty> properties;

    public ESTypedClass() {
        this.properties = new HashMap<>();
    }


    public ImmutableMap<String, FieldProperty> getProperties(){
        return ImmutableMap.copyOf(properties);
    }

    public void addFieldProperty(String field, FieldProperty fieldProps){
        properties.put(field, fieldProps);
    }





}
