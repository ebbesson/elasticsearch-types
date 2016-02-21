package se.ebbman.estypes.mapping;

import java.util.Map;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 21, 2016 - 9:58:21 AM
 */
public class Mapping {

    private Map<String, ESTypedClass> mappings;

    private Mapping(){}

    public Mapping(Map<String, ESTypedClass> mappings) {
        this.mappings = mappings;
    }

    public Map<String, ESTypedClass> getMappings() {
        return mappings;
    }
}
