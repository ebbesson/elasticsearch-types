package se.ebbman.estypes.annotations;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * 
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 16, 2016 - 9:44:43 AM
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface ESType {
    public String type();
}
