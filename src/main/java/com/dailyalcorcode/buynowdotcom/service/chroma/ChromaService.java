package com.dailyalcorcode.buynowdotcom.service.chroma;

import com.dailyalcorcode.buynowdotcom.service.llm.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingsRequest;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.ai.chroma.vectorstore.ChromaApi.QueryRequest.Include.all;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChromaService implements IChromaService {

    @Value("${spring.ai.vectorstore.chroma.tenant}")
    private String tenant;

    @Value("${spring.ai.vectorstore.chroma.database}")
    private String database;
    private final ChromaApi chromaApi;

    private final ChromaVectorStore chromaVectorStore;

    private final LLMService llmService;

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

    @Override
    public String saveImageEmbeddings(Long productId, MultipartFile image) throws IOException {
        String imageDescription = llmService.describeImage(image);
        // Her görsel için benzersiz ID
        String embeddingId = UUID.randomUUID().toString();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("productId", productId);


        var document = Document.builder()
                .id(embeddingId)
                .text(imageDescription)
                .metadata(metadata)
                .build();

        try {
            chromaVectorStore.doAdd(List.of(document));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return "Document added to chroma store successfully";
    }

    public List<Long> searchImageSimilarity(MultipartFile image) throws IOException {
        String imageDescription = llmService.describeImage(image);
        SearchRequest searchRequest = SearchRequest.builder()
                .query(imageDescription)
                .topK(10)
                .similarityThreshold(0.85f)
                .build();
        List<Document> searchResult = chromaVectorStore.doSimilaritySearch(searchRequest);

        /*
        searchResult.forEach(document -> {
            Double score = document.getScore();
            Double distance = null;
            Object distanceObj = document.getMetadata().get("distance");
            if (distanceObj != null) {
                distance = Double.parseDouble(distanceObj.toString());
            }
            Object productId = document.getMetadata().get("productId");
            log.info("Search image similarity score {} : ," +
                    "ProductId : {} , " +
                    "Distance : {} ", score, productId, distance);
        });
         */
        return searchResult.stream()
                .map(doc -> doc.getMetadata().get("productId"))
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
