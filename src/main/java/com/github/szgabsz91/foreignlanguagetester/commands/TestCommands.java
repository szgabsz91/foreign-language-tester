package com.github.szgabsz91.foreignlanguagetester.commands;

import com.github.szgabsz91.foreignlanguagetester.models.*;
import com.github.szgabsz91.foreignlanguagetester.services.ConsoleService;
import com.github.szgabsz91.foreignlanguagetester.services.TestService;
import org.jline.utils.InfoCmp;
import org.springframework.shell.component.ConfirmationInput;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;

@ShellComponent
public class TestCommands extends AbstractShellComponent implements ICommand {

    private final TestService testService;
    private final ConsoleService consoleService;

    TestCommands(TestService testService, ConsoleService consoleService) {
        this.testService = testService;
        this.consoleService = consoleService;
    }

    @ShellMethod(key = "test-all", group = COMMAND_GROUP, value = "Tests your knowledge in a specified foreign language")
    public void testAll(
            @ShellOption(value = "native-language", defaultValue = "hu", help = "Your native language in which the questions are asked") Language nativeLanguage,
            @ShellOption(value = "foreign-language", defaultValue = "de", help = "The foreign language in which the answers are required") Language foreignLanguage) {
        getTerminal().puts(InfoCmp.Capability.clear_screen);
        getTerminal().flush();

        try {
            var test = this.testService.createCompleteTest();
            this.test(test, nativeLanguage, foreignLanguage);
        } finally {
            this.testService.saveResults();
        }
    }

    @ShellMethod(key = "test-failed", group = COMMAND_GROUP, value = "Asks you all the questions that you ever failed")
    public void testFailed(
            @ShellOption(value = "native-language", defaultValue = "hu", help = "Your native language in which the questions are asked") Language nativeLanguage,
            @ShellOption(value = "foreign-language", defaultValue = "de", help = "The foreign language in which the answers are required") Language foreignLanguage) {
        getTerminal().puts(InfoCmp.Capability.clear_screen);
        getTerminal().flush();

        try {
            var test = this.testService.createReinforcingTest();
            this.test(test, nativeLanguage, foreignLanguage);
        } finally {
            this.testService.saveResults();
        }
    }

    private void test(Test test, Language nativeLanguage, Language foreignLanguage) {
        this.test(test, nativeLanguage, foreignLanguage, true);
    }

    private void test(Test test, Language nativeLanguage, Language foreignLanguage, boolean shouldIncrementAskedCounter) {
        var failedTestItems = new ArrayList<TestItem>();

        while (!test.isFinished()) {
            var testItem = test.getRandomTestItem();
            var question = testItem.translations().get(nativeLanguage);

            if (shouldIncrementAskedCounter) {
                this.testService.incrementAskedCounter(nativeLanguage, question);
            }

            var expectedAnswer = testItem.translations().get(foreignLanguage);
            var result = this.askQuestion(question, expectedAnswer);

            if (result == TestResult.FAILED) {
                failedTestItems.add(testItem);
            }
        }

        failedTestItems.forEach(testItem -> this.testService.incrementFailedCounter(nativeLanguage, testItem.translations().get(nativeLanguage)));

        var numberOfQuestions = test.size();
        var numberOfFailures = failedTestItems.size();
        var numberOfCorrectAnswers = numberOfQuestions - numberOfFailures;
        var percent = 100 * numberOfCorrectAnswers / (double) numberOfQuestions;
        var resultColor = percent >= 80 ? OutputColor.GREEN : OutputColor.RED;
        this.consoleService.useColor(resultColor, () -> this.consoleService.write("%nResult: %d/%d (%.2f%%)%n", numberOfCorrectAnswers, numberOfQuestions, percent));

        if (failedTestItems.isEmpty()) {
            this.consoleService.useColor(OutputColor.GREEN, () -> this.consoleService.write("Congratulations!%n%n"));
            return;
        }

        this.consoleService.write("%n");
        var confirmationInput = new ConfirmationInput(getTerminal(), "Would you like to repeat the failed questions?");
        confirmationInput.setResourceLoader(getResourceLoader());
        confirmationInput.setTemplateExecutor(getTemplateExecutor());
        ConfirmationInput.ConfirmationInputContext context = confirmationInput.run(ConfirmationInput.ConfirmationInputContext.empty());
        this.consoleService.write("%n");

        if (context.getResultValue()) {
            var reinforcingTest = new Test(failedTestItems);
            this.test(reinforcingTest, nativeLanguage, foreignLanguage, false);
        }
    }

    private TestResult askQuestion(String question, String expectedAnswer) {
        String answer = "";
        TestResult result = TestResult.PASSED;

        this.consoleService.useColor(OutputColor.YELLOW, () -> this.consoleService.write("> "));
        this.consoleService.write("%s%n", question);

        while (true) {
            this.consoleService.write("  ");
            answer = this.consoleService.useColor(OutputColor.WHITE, this.consoleService::read);
            var currentAnswer = answer;

            this.consoleService.deleteLastLine();
            if (answer.equals(expectedAnswer)) {
                this.consoleService.useColor(OutputColor.GREEN, () -> this.consoleService.write("  %s ✓%n", currentAnswer));
                break;
            } else if (answer.equalsIgnoreCase("x")) {
                result = TestResult.FAILED;
                this.consoleService.useColor(OutputColor.RED, () -> this.consoleService.write("  %s ✘%n", expectedAnswer));
                break;
            } else {
                result = TestResult.FAILED;
                this.consoleService.useColor(OutputColor.RED, () -> this.consoleService.write("  %s ✘%n", currentAnswer));
            }
        }

        return result;
    }

}
