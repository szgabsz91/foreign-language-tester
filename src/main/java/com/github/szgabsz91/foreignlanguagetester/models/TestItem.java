package com.github.szgabsz91.foreignlanguagetester.models;

import java.util.Map;

public record TestItem(Map<Language, String> translations, TestItemCounter counters) {

    public TestItem incrementAsked() {
        return new TestItem(this.translations, this.counters.incrementAsked());
    }

    public TestItem incrementFailed() {
        return new TestItem(this.translations, this.counters.incrementFailed());
    }

    public TestItem resetCounters() {
        return new TestItem(this.translations, new TestItemCounter(0, 0));
    }

}
