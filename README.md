## Hawthorne - Database Library

This library is designed to simplify the data storage process:
- No standalone database application required (no need to install SQL/NoSQL).
- No queries are needed and no query languages are used.
- All data is stored via *.json and *.bin files.
- All storage operations are available through a single Repository class and its static methods  
- The storage process is easy and fast.

## Installation  

To use the dependency in your projects, install it like this:

Gradle:
```
implementation 'com.george-n:hawthorne:1.0.2'
```

Maven:
```
<dependency>
    <groupId>com.george-n</groupId>
    <artifactId>hawthorne</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Usage:
### 1. Singleton Entity  

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
> ${ROOT}/hawthorne/entities/Sample/Sample.json  

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
> ${ROOT}/hawthorne/entities/samples/example/Sample/Sample.json

### 2. Entity Collection  
The same method applies to collections for storing sequential data such as users, documents, goods, etc.  
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

Now, every time you save a new object, Hawthorne will generate an ID (Integer, Long, or String UUID - depending on the field type).  
Objects with an existing ID will be updated.  

Both the "get" and "delete" method require an id parameter.  
A "list" method is also available, requiring the "limit" and "offset" parameters.

```java  
Sample sample = new Sample();
sample.setField("Sample ");

Sample savedSample = Repository.save(sample);
Sample retrievedSample = Repository.get(Sample.class, 1);
List<Sample> samples = Repository.list(Sample.class, 5, 0);
boolean isDeleted = Repository.delete(Sample.class, 1);
```

@EntityCollection paths look like this: 
> ${ROOT}/hawthorne/entities/Sample/1.json  
> ${ROOT}/hawthorne/entities/Sample/2.json  

### 3. Binary Data  

Entities that have a byte[] field marked with the @BinaryData annotation will be stored differently.  
Your object will still be serialized in JSON format, but its byte[] contents will be stored separately in a *.bin file.  
This method saves approximately 33% of disk space compared to Base64 serialization.  
In this case, java class acts as a wrapper for the byte[] content of your choice — it could be an image, pdf, mp3, etc.  
Everything else works the same. Your class might look like this:  

```java  
@EntityCollection(path = "samples/example")
public class Sample {
    @Id
    private String uuid;
    @BinaryData
    private byte[] bytes;

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public byte[] getBytes() { return bytes; }

    public void setBytes(byte[] bytes) { this.bytes = bytes; }
}
```

And this is the usage:  

```java
Sample sample = new Sample();
sample.setBytes("How are you?".getBytes());

Sample savedSample = Repository.save(sample);
Sample retrievedSample = Repository.get(Sample.class, "86a0fad8-12a4-4839-a4dd-403824421b71");
List<Sample> samples = Repository.list(Sample.class, 1, 5);
boolean isDeleted = Repository.delete(Sample.class, "86a0fad8-12a4-4839-a4dd-403824421b71");
```

The binary data is stored in the same directory as the entity:  
> ${ROOT}/hawthorne/entities/samples/example/Sample/86a0fad8-12a4-4839-a4dd-403824421b71.json  
> ${ROOT}/hawthorne/entities/samples/example/Sample/86a0fad8-12a4-4839-a4dd-403824421b71.bin  

Also you can ignore this method — without marking it as @BinaryData, your byte[] data will be serialized as Base64.  

## Storage schema  
Your database structure will be available at the path:  
> ${ROOT}/hawthorne/storage-schema.json

## Possible limitations

- Each stored entity must have a default no-args constructor (otherwise a HawthorneException will be thrown).  
- I strongly recommend that you do not edit the files directly — neither storage schema nor the entity files. This will likely result in data corruption.
- Currently (April 2024), Hawthorne does not support changes to your schema. This feature will be added in future releases.  