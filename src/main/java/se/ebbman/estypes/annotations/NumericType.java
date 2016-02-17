/*
 */
package se.ebbman.estypes.annotations;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author Magnus Ebbesson <magnus.ebbesson@findwise.com>
 * Feb 12, 2016 - 8:39:57 PM
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface NumericType {

    /**
     * Accepts values of "long", "integer", "short", "byte", "double", "float"
     *
     * @return
     */
    public String type();

    /**
     * Should the field be searchable? Accepts not_analyzed (default) and no.
     *
     * @return
     */
    public String index() default "";

    /**
     * Accepts a numeric value of the same type as the field which is substituted for any explicit null values. Defaults to null, which
     * means the field is treated as missing.
     *
     * @return
     */
    public String null_value() default "";
    /**
     * Field-level index time boosting. Accepts a floating point number, defaults to 1.0.
     *
     * @return
     */
    public double boost() default 1.0;

    /**
     * Controls the number of extra terms that are indexed
     *
     * @return
     */
    public int precision_step() default 16;

    /**
     * doc_values
     * <p>
     * Should the field be stored on disk in a column-stride fashion,
     * so that it can later be used for sorting, aggregations, or scripting?
     * Accepts true (default) or false.
     *
     * @return
     */
    public boolean doc_values() default true;

    /**
     * If true, malformed numbers are ignored.
     * If false (default), malformed numbers throw an exception and reject the whole document.
     *
     * @return
     */
    public boolean ignore_malformed() default false;

    /**
     * Whether or not the field value should be included in the _all field? Accepts true or false. Defaults to false if index is set to no,
     * or if a parent object field sets include_in_all to false. Otherwise defaults to true.
     *
     * @return
     */
    public boolean include_in_all() default true;

    /**
     * Try to convert strings to numbers and truncate fractions for integers. Accepts true (default) and false.
     *
     * @return
     */
    public boolean coerce() default false;

}
