package com.dailyalcorcode.buynowdotcom.service.chroma;

import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IChromaService {

    void deleteCollection(String collectionName);

    List<Collection> getCollections();

    GetEmbeddingResponse getEmbeddings(String collectionId);

    String saveImageEmbeddings(Long productId, MultipartFile image) throws IOException;
}
