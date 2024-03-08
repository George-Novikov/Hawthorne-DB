### Hawthorne - Database Library

This library is designed to simplify the data storage process:
- No standalone database application required (no need to install SQL/NoSQL).
- No queries are needed and no query languages are used.
- Lightweight and fast.
- All data is stored via *.json and *.bin files.

### Usage:
#### 1. Singleton Entity  

We have a simple class that needs to be stored long-term but always has a single copy:
```java
@SingletonEntity
public class Sample {
    private String field;

    public String getField() { return field; }

    public void setField(String field) { this.field = field; }
}
```
It can be then saved and modified as follows:
```java
Sample sample = new Sample();
sample.setField("How are you?");

Sample savedSample = Repository.save(sample);
Sample retrievedSample = Repository.get(Sample.class);
boolean isDeleted = Repository.delete(Sample.class);
```
The resulting file is saved along the path:  
>${ROOT}/hawthorne/entities/Sample/Sample.json  

And has the following structure:
```json
{"field":"How are you?"}
```
Also you can use your custom entity path:  
```java
@SingletonEntity(path = "samples/example")
...
```
And your path now looks like this:  
>>${ROOT}/hawthorne/entities/samples/example/Sample/Sample.json

#### 2. Entity Collection  
The same method applies to collections for storing user data, documents, goods, etc.  
Such a class requires the @EntityCollection annotation and one field marked with @Id.  
<h6>N.B.: You can use either the "native" @Id annotation or the one from the jakarta.persistence package.</h6>
```java
@EntityCollection
public class Sample {
    @Id
    private int id;
    private String field;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getField() { return field; }

    public void setField(String field) { this.field = field; }
}
```

Now, every time you save a new object, Hawthorne will generate an id (Integer, Long, or String UUID - depending on the field type). Objects saved with an existing id will be updated.  
Both the "get" and "delete" method require an id parameter. A "list" method is also available, requiring the "limit" and "offset" parameters.
```java
Sample sample = new Sample();
sample.setField("How are you?");

Sample savedSample = Repository.save(sample);
Sample retrievedSample = Repository.get(Sample.class, 1);
List<Sample> samples = Repository.list(Sample.class, 0, 0);
boolean isDeleted = Repository.delete(Sample.class, 1);
```
@EntityCollection paths look like this: 
>${ROOT}/hawthorne/entities/Sample/1.json  
>${ROOT}/hawthorne/entities/Sample/2.json  

#### 2. Binary Data
Entities that have a byte[] field with the @BinaryData annotation will be stored as a "singleton file" or a "file collection".  
Your object will still be stored in JSON format, but its byte[] contents will be stored separately in a *.bin file.  
Everything else works the same.


