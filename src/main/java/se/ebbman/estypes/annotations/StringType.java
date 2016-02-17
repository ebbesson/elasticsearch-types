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
 * Feb 12, 2016 - 7:50:08 PM
 */
@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface StringType {


    /**
     * Field data type
     *
     * @return
     */
    public String type() default "string";

    /**
     * Should the field be searchable? Accepts not_analyzed (default) and no.
     *
     * @return
     */
    public String index() default "";

    /**
     * The analyzer which should be used for analyzed string fields, both at index-time and at search-time (unless overridden by the
     * search_analyzer). Defaults to the default index analyzer, or the standard analyzer.
     *
     * @return
     */
    public String analyzer() default "standard";

    /**
     * Field-level index time boosting. Accepts a floating point number, defaults to 1.0.
     *
     * @return
     */
    public double boost() default 1.0;

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
     *
     * Can the field use in-memory fielddata for sorting, aggregations, or scripting? Accepts disabled or paged_bytes (default). Not
     * analyzed fields will use doc values in preference to fielddata.
     *
     * @return
     */
    public String fielddata() default "paged_bytes";

    /**
     * Multi-fields allow the same string value to be indexed in multiple ways for different purposes, such as one field for search and a
     * multi-field for sorting and aggregations, or the same string value analyzed by different analyzers.
     *
     * @return
     */
    public String fields() default "";

    /**
     * Do not index or analyze any string longer than this value. Defaults to 0 (disabled).
     *
     * @return
     */
    public int ignore_above() default 0;

    /**
     *
     * What information should be stored in the index, for search and highlighting purposes. Defaults to positions for analyzed fields, and
     * to docs for not_analyzed fields.
     *
     * @return
     */
    public String index_options() default "analyzed";

    /**
     * Whether field-length should be taken into account when scoring queries. Defaults depend on the index setting:
     * <p>
     * analyzed fields default to { "enabled": true, "loading": "lazy" }.
     * not_analyzed fields default to { "enabled": false }.
     *
     * @return
     */
    //TODO Implement annotation processor http://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html
    public String norms() default "";

    /**
     * The number of fake term positions which should be inserted between each element of an array of strings. Defaults to 0. The number of
     * fake term position which should be inserted between each element of an array of strings. Defaults to the position_increment_gap
     * configured on the analyzer which defaults to 100. 100 was chosen because it prevents phrase queries with reasonably large slops (less
     * than 100) from matching terms across field values
     *
     * @return
     */
    public int position_increment_gap() default 0;

    /**
     *
     * The analyzer that should be used at search time on analyzed fields. Defaults to the analyzer setting.
     *
     * @return
     */
    public String search_analyzer() default "standard";

    /**
     * The analyzer that should be used at search time when a phrase is encountered. Defaults to the search_analyzer setting.
     *
     * @return
     */
    public String search_quote_analyzer() default "standard";

    /**
     * Which scoring algorithm or similarity should be used. Defaults to default, which uses TF/IDF.
     *
     * @return
     */
    public String similarity() default "default";

    /**
     * Whether term vectors should be stored for an analyzed field. Defaults to no.
     *
     * @return
     */
    public String term_vector() default "no";

    /**
     * Controls the number of extra terms that are indexed to make range queries faster. Defaults to 16.
     *
     * @return
     */
    //String null_value();

    /**
     * Whether the field value should be stored and retrievable separately from the _source field. Accepts true or false (default).
     *
     * @return
     */
    boolean store() default false;

    /**
     * Whether or not the field value should be included in the _all field? Accepts true or false. Defaults to false if index is set to no,
     * or if a parent object field sets include_in_all to false. Otherwise defaults to true.
     *
     * @return
     */
    boolean include_in_all() default true;

}
