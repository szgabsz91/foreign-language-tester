package com.github.szgabsz91.foreignlanguagetester.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.szgabsz91.foreignlanguagetester.models.Language;
import com.github.szgabsz91.foreignlanguagetester.models.Test;
import com.github.szgabsz91.foreignlanguagetester.models.TestItem;
import com.github.szgabsz91.foreignlanguagetester.models.TestItemCounter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TestService implements InitializingBean {

    private final List<TestItem> testItems = new ArrayList<>();

    private final ObjectMapper objectMapper;
    private final Path questionsFolder;

    public TestService(ObjectMapper objectMapper, @Value("${foreign-language-tester.questions-folder-path}") String questionsFolderPath) {
        this.objectMapper = objectMapper;
        this.questionsFolder = Paths.get(questionsFolderPath);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try (var questionFiles = Files.walk(this.questionsFolder)) {
            var testItems = questionFiles
                    .filter(Files::isRegularFile)
                    .flatMap(questionsFile -> {
                        try {
                            return this.objectMapper.readValue(questionsFile.toFile(), new TypeReference<List<TestItem>>() {})
                                    .stream()
                                    .map(testItem -> testItem.counters() == null ? testItem.withCounters(new TestItemCounter(0, 0)) : testItem)
                                    .map(testItem -> testItem.withFilename(questionsFile.getFileName().toString()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            this.testItems.addAll(testItems);
        }
    }

    public Test createCompleteTest() {
        return new Test(this.testItems);
    }

    public Test createReinforcingTest() {
        var failedTestItems = this.testItems
                .stream()
                .filter(testItem -> testItem.counters().failed() > 0)
                .toList();
        return new Test(failedTestItems);
    }

    public void incrementAskedCounter(Language nativeLanguage, String question) {
        var askedTestItemIndex = IntStream.range(0, this.testItems.size())
                .filter(index -> this.testItems.get(index).translations().get(nativeLanguage).equals(question))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The test item was not found!"));
        var askedTestItem = this.testItems.get(askedTestItemIndex);
        this.testItems.set(askedTestItemIndex, askedTestItem.incrementAsked());
    }

    public void incrementFailedCounter(Language nativeLanguage, String question) {
        var failedTestItemIndex = IntStream.range(0, this.testItems.size())
                .filter(index -> this.testItems.get(index).translations().get(nativeLanguage).equals(question))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The test item was not found!"));
        var failedTestItem = this.testItems.get(failedTestItemIndex);
        this.testItems.set(failedTestItemIndex, failedTestItem.incrementFailed());
    }

    public void resetCounters() {
        var resetTestItems = this.testItems
                .stream()
                .map(TestItem::resetCounters)
                .toList();
        this.testItems.clear();
        this.testItems.addAll(resetTestItems);
    }

    public void saveResults() {
        this.testItems
                .stream()
                .collect(Collectors.groupingBy(TestItem::filename))
                .forEach((filename, testItems) -> {
                    var outputFile = this.questionsFolder.resolve(filename);

                    try (var outputStream = Files.newOutputStream(outputFile)) {
                        this.objectMapper.writeValue(outputStream, testItems);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
