
package se.ebbman.estypes.mapping.dummy;

import java.io.Serializable;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.time.Instant.now;
import java.util.Date;
import se.ebbman.estypes.annotations.DateType;
import se.ebbman.estypes.annotations.ESType;
import se.ebbman.estypes.annotations.NumericType;
import se.ebbman.estypes.annotations.StringType;

/**
 *
 * @author Magnus Ebbesson
 * Feb 18, 2016 - 10:08:04 AM
 */
@ESType(name = "test2")
public class ESTypeTest2 implements Serializable{

    private static final long serialVersionUID = 1L;

    @StringType(analyzer = "special")
    private final String stringTest = "banana";

    @NumericType(type = "long")
    private final long longTest = 123;

    @DateType()
    private final Date dateTest = Date.from(now());

    @Retention(RUNTIME)
    @Target(FIELD)
    public @interface Dummer{}

    @Dummer
    private String something = "dummer";

}

