package com.github.szgabsz91.foreignlanguagetester;

import com.github.szgabsz91.foreignlanguagetester.configurations.ForeignLanguageTesterConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ForeignLanguageTesterConfigurationProperties.class)
public class ForeignLanguageTesterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForeignLanguageTesterApplication.class, args);
	}

}
