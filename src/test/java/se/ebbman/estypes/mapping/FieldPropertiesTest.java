package se.ebbman.estypes.mapping;

import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Magnus Ebbesson
 */
public class FieldPropertiesTest {

    public FieldPropertiesTest() {
    }

    @Test
    public void testPopulatePropertiesFromStringField() throws NoSuchFieldException {
        ESTypeTest esTypedClass = new ESTypeTest();
        Field field = esTypedClass.getClass().getDeclaredField("stringTest");
        FieldProperties instance = new FieldProperties();
        instance.populatePropertiesFromField(field);
        assertTrue(instance.getProperties().size() == 2);
        assertTrue(instance.getProperties().containsKey("analyzer"));
        assertEquals(instance.getProperties().get("analyzer"), "special");
        assertTrue(instance.getProperties().containsKey("type"));
        assertEquals(instance.getProperties().get("type"), "string");
    }

    @Test
    public void testPopulatePropertiesFromNumericField() throws NoSuchFieldException {
        ESTypeTest esTypedClass = new ESTypeTest();
        Field field = esTypedClass.getClass().getDeclaredField("longTest");
        FieldProperties instance = new FieldProperties();
        instance.populatePropertiesFromField(field);
        assertTrue(instance.getProperties().size() == 1);
        assertTrue(instance.getProperties().containsKey("type"));
        assertEquals(instance.getProperties().get("type"), "long");
    }

    @Test
    public void testPopulatePropertiesFromDate() throws NoSuchFieldException {
        ESTypeTest esTypedClass = new ESTypeTest();
        Field field = esTypedClass.getClass().getDeclaredField("dateTest");
        FieldProperties instance = new FieldProperties();
        instance.populatePropertiesFromField(field);
        assertTrue(instance.getProperties().size() == 1);
        assertTrue(instance.getProperties().containsKey("type"));
        assertEquals(instance.getProperties().get("type"), "date");
    }

}
