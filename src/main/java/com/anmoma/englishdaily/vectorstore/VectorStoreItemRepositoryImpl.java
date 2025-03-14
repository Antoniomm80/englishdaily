package com.anmoma.englishdaily.vectorstore;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VectorStoreItemRepositoryImpl implements VectorStoreItemRepository {
    private final VectorStoreItemJpaRepository vectorStoreItemJpaRepository;

    public VectorStoreItemRepositoryImpl(VectorStoreItemJpaRepository vectorStoreItemJpaRepository) {
        this.vectorStoreItemJpaRepository = vectorStoreItemJpaRepository;
    }

    @Override
    public List<VectorStoreItem> findItemsByFileName(String fileName) {
        return vectorStoreItemJpaRepository.findByMetadataFileName(fileName);
    }
}
