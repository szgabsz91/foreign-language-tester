package com.github.szgabsz91.foreignlanguagetester.converters;

import com.github.szgabsz91.foreignlanguagetester.models.Language;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LanguageConverter implements Converter<String, Language> {

    @Override
    @Nullable
    public Language convert(String languageCode) {
        return Arrays.stream(Language.values())
                .filter(language -> language.toString().equals(languageCode))
                .findFirst()
                .orElse(null);
    }

}
