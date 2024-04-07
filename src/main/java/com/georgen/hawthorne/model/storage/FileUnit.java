package com.georgen.hawthorne.model.storage;

import com.georgen.hawthorne.tools.Serializer;
import com.georgen.hawthorne.tools.id.IdGenerator;
import com.georgen.hawthorne.tools.extractors.BinaryDataExtractor;
import com.georgen.hawthorne.tools.id.extractors.IdValueExtractor;

public class FileUnit<S> extends StorageUnit<byte[], S>{
    private byte[] content;

    public FileUnit(S source) throws Exception {
        this(
                new StorageArchetype(source),
                source
        );
    }

    public FileUnit(StorageArchetype archetype, S source) throws Exception {
        this.setArchetype(archetype);
        this.setSource(source);

        if (IdGenerator.isGenerationRequired(this)){
            Object generatedId = IdGenerator.generateForUnit(this);
            this.setSourceId(generatedId);
            this.setNew(true);
        } else {
            Object existingId = IdValueExtractor.extract(source);
            this.setSourceId(existingId);
        }

        this.setMetadata(Serializer.toJson(source));
        this.setContent(BinaryDataExtractor.extract(source));
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
    }
}
