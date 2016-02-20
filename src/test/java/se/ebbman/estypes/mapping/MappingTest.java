/*
 */
package se.ebbman.estypes.mapping;

import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 */
public class MappingTest {

    
    @Test
    public void testGetMapping() {
        //TODO
    }

    @Test
    public void testScanForMappings() throws Exception {
        
        String packageScope = this.getClass().getPackage().getName();
        Mapping instance = Mapping.getInstance();
        Map<String, ESTypedClass> result = instance.scanForMappings(packageScope);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("test"));
        
    }


    @Test
    public void testParseAnnotatedClass() {
        Class<?> clazz = new ESTypeTest().getClass();
        Mapping instance = Mapping.getInstance();
        ESTypedClass result = instance.parseAnnotatedClass(clazz);
        assertNotNull(result);
        assertFalse(result.getProperties().isEmpty());
    }


    
}
