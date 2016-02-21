/*
 */
package se.ebbman.estypes.mapping;

import static java.time.Instant.now;
import java.util.Date;
import se.ebbman.estypes.annotations.DateType;
import se.ebbman.estypes.annotations.ESType;
import se.ebbman.estypes.annotations.NumericType;
import se.ebbman.estypes.annotations.StringType;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 18, 2016 - 10:08:04 AM
 */
@ESType(type = "test")
public class ESTypeTest {

    @StringType(analyzer = "special")
    private final String stringTest = "banana";

    @NumericType(type = "long")
    private final long longTest = 123;

    @DateType()
    private final Date dateTest = Date.from(now());

}
