package com.github.szgabsz91.foreignlanguagetester.providers;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class ForeignLanguageTesterPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        var style = new AttributedStyle().foreground(AttributedStyle.YELLOW);
        return new AttributedString("Foreign Language Tester:> ", style);
    }

}
