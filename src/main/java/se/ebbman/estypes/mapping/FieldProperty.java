package se.ebbman.estypes.mapping;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 17, 2016 - 5:36:27 PM
 */
public class FieldProperty {

    private final Map<String, Object> props;
    
    public FieldProperty() {
        this.props = new HashMap<>();
    }

    public ImmutableMap<String, Object> getProperties() {
        return ImmutableMap.copyOf(props);
    }

    public void addFieldProperty(String property, Object propertyValue) {
        props.put(property, propertyValue);
    }

}
