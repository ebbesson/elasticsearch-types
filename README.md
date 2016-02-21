## elasticsearch-types 

Elasticsearch-types provides a simple way of generating valid [Elasticsearch Mapping](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html) of your POJOs as Elasticsearch Types, allowing for you to have your configuration of your types where your code resides. 

## Code Example

```java```

package my.cool.elasticsearch.project

@ESType(name = "MyESType")
public class MyPojo{
  
  @StringType(analyzer = "standard_en", store = true)
  private String myToken = "My Pojo Rocks"
  
  @NumericType(type = long)
  private long mySerialNumer = 1337L;
  //... and so forth 
}

package my.uber.elasticsearch.project

public class ElasticSearchIndexer {

  //Returns a valid JSON-string 
  String jsonMapping = MappingsProducer.getMapping("my.cool.elasticsearch.project");
  // PUT to Elasticsearch instance to update Mapping.
}

``````

## Motivation

Elasticsearch is quite a powerful and API-rich search and analytics engine. Elasticsearch does not provide a lightweight Java-client (that doesnt ship the entire search engine as a JAR). Thirdparty clients such as [JEST](https://github.com/searchbox-io/Jest) are addressing this issue, but still requires you to generate quite a bit of JSON-strings interact with your ES instance. JSON-strings in Java aren't a match made in heaven for code readability and testing. Elasticsearch-types aim to fill address this issue somewhat by addressing the Mapping issues, that quickly can get out of hand if a client has to deal with many different dataobjects.  

## Installation

Requirements
* Maven
* Git
* minimum Java 8

For now, clone this repository, run 
``````
git clone git@github.com:ebbesson/elasticsearch-types.git
cd elastic-types
mvn install;
``````

Deploy to Maven Central are on the way.

## API Reference

Annotation types are mapped towards [Elasticsearch Field](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html) types 

Currently Elastic Types (0.1.0) only supports 

* String
* Numeric
* Date

but more types will be added

## Contributors

This is still work in progress and features will get added. Pull-requrest are welcomed. 

## License

MIT
