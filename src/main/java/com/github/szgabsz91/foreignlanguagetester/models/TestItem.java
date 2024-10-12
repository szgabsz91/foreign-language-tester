package com.github.szgabsz91.foreignlanguagetester.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public record TestItem(@JsonIgnore String filename, Map<Language, String> translations, TestItemCounter counters) {

    public TestItem withFilename(String filename) {
        return new TestItem(filename, this.translations(), this.counters());
    }

    public TestItem withCounters(TestItemCounter counters) {
        return new TestItem(this.filename(), this.translations(), counters);
    }

    public TestItem incrementAsked() {
        return new TestItem(this.filename, this.translations, this.counters.incrementAsked());
    }

    public TestItem incrementFailed() {
        return new TestItem(this.filename, this.translations, this.counters.incrementFailed());
    }

    public TestItem resetCounters() {
        return new TestItem(this.filename, this.translations, new TestItemCounter(0, 0));
    }

}
