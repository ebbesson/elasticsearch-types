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
 * Feb 12, 2016 - 8:29:34 PM
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface DateType {

    /**
     * Should the field be searchable? Accepts not_analyzed (default) and no.
     *
     * @return
     */
    public String type() default "date";

    /**
     * Should the field be searchable? Accepts not_analyzed (default) and no.
     *
     * @return
     */
    public String index() default "";

    /**
     * The date format(s) that can be parsed.
     * <p>
     * Defaults to strict_date_optional_time||epoch_millis.
     *
     * @return
     */
    public String format() default "";

    /**
     * Accepts a date value in one of the configured format's as the field which is substituted for any explicit null values. Defaults to
     * null, which means the field is treated as missing.
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
     * Whether the field value should be stored and retrievable separately from the _source field. Accepts true or false (default).
     *
     * @return
     */
    public boolean store() default false;

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

}
