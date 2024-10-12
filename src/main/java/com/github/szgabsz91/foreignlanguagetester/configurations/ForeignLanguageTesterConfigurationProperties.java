package com.github.szgabsz91.foreignlanguagetester.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "foreign-language-tester")
public record ForeignLanguageTesterConfigurationProperties(String questionsFolderPath) {
}
