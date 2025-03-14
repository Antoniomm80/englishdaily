package com.anmoma.englishdaily.vectorstore;

import java.util.List;

public interface VectorStoreItemRepository {
    List<VectorStoreItem> findItemsByFileName(String fileName);
}
