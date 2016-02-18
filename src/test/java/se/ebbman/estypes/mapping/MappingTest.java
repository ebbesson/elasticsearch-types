/*
 */
package se.ebbman.estypes.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 */
public class MappingTest {

    public MappingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetMapping() {

    }

    @Test
    public void testScanForMappings() throws Exception {
        
        String packageScope = this.getClass().getPackage().getName();
        Mapping instance = new Mapping();
        Map<String, ESTypedClass> result = instance.scanForMappings(packageScope);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("test"));
        
    }

    @Test
    public void testPopulatePropertiesFromField() {
        System.out.println("populatePropertiesFromField");
        FieldProperty fieldProperty = null;
        Method[] declaredFieldMethods = null;
        Annotation fieldTypeAnnotation = null;
        Mapping instance = null;
//        instance.populatePropertiesFromField(fieldProperty, declaredFieldMethods, fieldTypeAnnotation);
//        fail("The test case is a prototype.");
    }

    @Test
    public void testParseAnnotatedClass() {
        Class<?> clazz = new ESTypeTest().getClass();
        Mapping instance = new Mapping();
        ESTypedClass result = instance.parseAnnotatedClass(clazz);
        assertNotNull(result);
        assertFalse(result.getProperties().isEmpty());
    }


    
}
