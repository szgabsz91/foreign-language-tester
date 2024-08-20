package com.github.szgabsz91.foreignlanguagetester.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.szgabsz91.foreignlanguagetester.models.Language;
import com.github.szgabsz91.foreignlanguagetester.models.TestItem;
import com.github.szgabsz91.foreignlanguagetester.models.TestItemCounter;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RegisterReflectionForBinding(classes = {Language.class, TestItemCounter.class, TestItem.class})
public class ForeignLanguageTesterConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

}
