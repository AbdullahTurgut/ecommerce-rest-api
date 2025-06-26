package com.dailyalcorcode.buynowdotcom.service.chroma;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.ai.chroma.vectorstore.ChromaApi.QueryRequest.Include.all;

@Service
@RequiredArgsConstructor
public class ChromaService implements IChromaService {

    @Value("${spring.ai.vectorstore.chroma.tenant}")
    private String tenant;

    @Value("${spring.ai.vectorstore.chroma.database}")
    private String database;
    private final ChromaApi chromaApi;

    @Override
    public void deleteCollection(String collectionName) {
        try {
            chromaApi.deleteCollection(tenant, database, collectionName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete collection: " + collectionName);
        }
    }

    @Override
    public List<Collection> getCollections() {
        try {
            return chromaApi.listCollections(tenant, database);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete collection");
        }
    }

    @Override
    public GetEmbeddingResponse getEmbeddings(String collectionId) {
        try {
            GetEmbeddingsRequest request = new GetEmbeddingsRequest(null, null, 0, 0, all);
            return chromaApi.getEmbeddings(tenant, database, collectionId, request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get embedding" + collectionId);
        }
    }
}
