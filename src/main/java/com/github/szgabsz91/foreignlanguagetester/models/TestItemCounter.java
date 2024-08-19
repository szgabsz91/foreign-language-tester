package com.github.szgabsz91.foreignlanguagetester.models;

public record TestItemCounter(int asked, int failed) {

    public TestItemCounter incrementAsked() {
        return new TestItemCounter(this.asked + 1, this.failed);
    }

    public TestItemCounter incrementFailed() {
        return new TestItemCounter(this.asked, this.failed + 1);
    }

}
