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
                                Generate a concise, detailed textual description of the image strictly for visual similarity search. \
                                       Please follow these rules:
                                       - Limit the description to 2-3 short sentences or a list of key attributes.
                                       - Focus ONLY on clearly visible, distinctive visual features such as:
                                         * Colors, patterns, textures, shapes, and materials.
                                         * Specific object types (e.g., 'red leather handbag', 'wooden dining table').
                                         * Brand names or logos if clearly visible.
                                         * Scene context ONLY if obvious (e.g., 'on a beach', 'in a kitchen').
                                       - Do NOT include subjective opinions, guesses, or generic terms like 'product', 'item',
                                        'electronics device', 'communication device', etc.
                                       - Avoid filler words or vague language.
                                       - Use simple, direct language suitable for automated similarity matching.
                                       Provide the description in a clear, structured format (e.g., comma-separated attributes or bullet points).
                                      \s""")
                        .media(MediaType.parseMediaType(Objects.requireNonNull(image.getContentType())), resource))
                .call()
                .content();

        log.info("The image description {} : ", content);
        return content;
    }
}
