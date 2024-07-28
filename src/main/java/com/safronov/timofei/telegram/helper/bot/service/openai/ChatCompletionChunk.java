package com.safronov.timofei.telegram.helper.bot.service.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionChunk {
    private String id;
    private String object;
    private long created;
    private String model;

    @Getter
    @Setter
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
    private Choice[] choices;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private int index;
        private Delta delta;
        private String finishReason;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Delta {
            private String content;
        }
    }
}
