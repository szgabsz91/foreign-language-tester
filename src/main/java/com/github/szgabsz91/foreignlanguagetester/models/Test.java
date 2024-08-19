package com.github.szgabsz91.foreignlanguagetester.models;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Test {

    private final List<TestItem> testItems;
    private final Set<Integer> askedQuestionIndices;
    private final Random random;

    public Test(List<TestItem> testItems) {
        this.testItems = testItems;
        this.askedQuestionIndices = new HashSet<>();
        this.random = new Random();
    }

    public TestItem getRandomTestItem() {
        int randomIndex;

        do {
            randomIndex = this.random.nextInt(this.testItems.size());
        } while (this.askedQuestionIndices.contains(randomIndex));

        this.askedQuestionIndices.add(randomIndex);
        return this.testItems.get(randomIndex);
    }

    public int size() {
        return this.testItems.size();
    }

    public boolean isFinished() {
        return this.askedQuestionIndices.size() == this.testItems.size();
    }

}
