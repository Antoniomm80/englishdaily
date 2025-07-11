package com.anmoma.englishdaily.vectorstore;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "vector_store")
public class VectorStoreItem {
    @Id
    private String id;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Metadata metadata;

    public String getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Transient
    public String getFileName() {
        if (metadata == null) {
            return "";
        }
        return metadata.fileName();
    }
}
