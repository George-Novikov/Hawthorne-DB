package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StorageSchemaDeserializer extends StdDeserializer<StorageSchema> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected StorageSchemaDeserializer(Class<?> vc) {
        super(vc);
    }

    protected StorageSchemaDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected StorageSchemaDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public StorageSchema deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JacksonException {
        JsonNode storageSchemaNode = jsonParser.getCodec().readTree(jsonParser);

        JsonNode schemaNode = storageSchemaNode.get("schema");
        Iterator<JsonNode> iterator = schemaNode.elements();

        ConcurrentMap<String, StorageArchetype> schemaMap = new ConcurrentHashMap<>();
        while (iterator.hasNext()){
            JsonNode child = iterator.next();
            String stringChild = child.toString();
            StorageArchetype archetype = OBJECT_MAPPER.readValue(stringChild, StorageArchetype.class);
            schemaMap.put(archetype.getSimpleName(), archetype);
        }

        StorageSchema storageSchema = new StorageSchema();
        storageSchema.setSchema(schemaMap);
        return storageSchema;
    }
}
