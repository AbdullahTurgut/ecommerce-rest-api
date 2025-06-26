package com.dailyalcorcode.buynowdotcom.controller;

import com.dailyalcorcode.buynowdotcom.response.ApiResponse;
import com.dailyalcorcode.buynowdotcom.service.chroma.IChromaService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/collections")
public class ChromaController {

    private final IChromaService chromaService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCollections() {
        List<Collection> collections = chromaService.getCollections();
        return ResponseEntity.ok(new ApiResponse("Collections found!", collections));
    }

    @DeleteMapping("/{collectionName}/delete")
    public ResponseEntity<ApiResponse> deleteCollection(@PathVariable String collectionName) {
        chromaService.deleteCollection(collectionName);
        return ResponseEntity.ok(new ApiResponse("Collection deleted!", collectionName));
    }

    @GetMapping("/{collectionId}/embeddings")
    public ResponseEntity<ApiResponse> getCollectionEmbeddings(@PathVariable String collectionId) {
        GetEmbeddingResponse embeddings = chromaService.getEmbeddings(collectionId);
        return ResponseEntity.ok(new ApiResponse("Embedding found", embeddings));
    }
}
