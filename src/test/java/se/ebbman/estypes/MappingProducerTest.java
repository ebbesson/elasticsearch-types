package se.ebbman.estypes;

import java.util.Map;
import org.json.JSONException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import se.ebbman.estypes.mapping.ESTypedClass;
import se.ebbman.estypes.mapping.dummy.ESTypeTest;

/**
 *
 * @author Magnus Ebbesson
 */
public class MappingProducerTest {

    @Test
    public void testGetMapping() throws JSONException {
        Map<String, String> mapping = MappingProducer.getMapping("se.ebbman.estypes");
        assertFalse(mapping.isEmpty());
        assertTrue(mapping.size() == 2);
        String expected = "{\n"
            + "			\"properties\": {\n"
            + "				\"longTest\": {\n"
            + "					\"type\": \"long\"\n"
            + "				},\n"
            + "				\"dateTest\": {\n"
            + "					\"type\": \"date\"\n"
            + "				},\n"
            + "				\"stringTest\": {\n"
            + "					\"analyzer\": \"special\",\n"
            + "					\"type\": \"string\"\n"
            + "				}\n"
                + "		}\n"
                + "}";
        JSONAssert.assertEquals(expected, mapping.get("test"), true);
        JSONAssert.assertEquals(expected, mapping.get("test2"), true);
    }

    @Test
    public void testScanForMappings() throws Exception {

        String packageScope = this.getClass().getPackage().getName();

        Map<String, ESTypedClass> result = MappingProducer.scanForESTypedClasses(packageScope);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("test"));

    }

    @Test
    public void testParseAnnotatedClass() {
        Class<?> clazz = new ESTypeTest().getClass();
        ESTypedClass result = MappingProducer.parseAnnotatedClass(clazz);
        assertNotNull(result);
        assertFalse(result.getProperties().isEmpty());
    }

}
