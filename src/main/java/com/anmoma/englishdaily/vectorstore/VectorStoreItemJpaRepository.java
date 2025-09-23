package com.anmoma.englishdaily.vectorstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VectorStoreItemJpaRepository extends JpaRepository<VectorStoreItem, Long> {

    @Query(value = """
            SELECT *
            FROM vector_store
            WHERE metadata ->> 'file_name' = :fileName
            """, nativeQuery = true)
    List<VectorStoreItem> findByMetadataFileName(@Param("fileName") String fileName);

    @Query(value = """
            SELECT *
            FROM vector_store
            WHERE metadata ->> 'fileuuid' = :fileuuid
            """, nativeQuery = true)
    List<VectorStoreItem> findByMetadataFileUuid(@Param("fileuuid") String fileuuid);
}
