package com.dailyalcorcode.buynowdotcom.service.llm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LLMService {

    private final ChatModel chatModel;


    public String describeImage(MultipartFile image) throws IOException {
        Resource resource = new InputStreamResource(image.getInputStream());

        String content = ChatClient
                .create(chatModel)
                .prompt()
                .user(promptUserSpec -> promptUserSpec.text("""
                        Describe the image.""").media(MediaType.parseMediaType(Objects.requireNonNull(image.getContentType())), resource))
                .call()
                .content();

        log.info("The image description {} : ", content);
        return content;
    }
}
