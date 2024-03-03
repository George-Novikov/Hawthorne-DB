package com.georgen.hawthorne.model.storage;

import com.georgen.hawthorne.tools.Serializer;
import com.georgen.hawthorne.tools.id.IdGenerator;

public class EntityUnit<S> extends StorageUnit<String, S> {
    private String content;
    public EntityUnit(S source) throws Exception {
        this(
                new StorageArchetype(source),
                source
        );
    }

    public EntityUnit(StorageArchetype archetype, S source) throws Exception {
        this.setArchetype(archetype);
        String jsonContent = Serializer.toJson(source);
        this.setMetadata(jsonContent);
        this.setContent(jsonContent);
        this.setSource(source);

        if (IdGenerator.isGenerationRequired(this)){
            Object generatedId = IdGenerator.generateForUnit(this);
            this.setGeneratedId(generatedId);
            this.setNew(true);
        }
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }
}
