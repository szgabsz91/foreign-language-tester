package com.github.szgabsz91.foreignlanguagetester.commands;

import com.github.szgabsz91.foreignlanguagetester.models.OutputColor;
import com.github.szgabsz91.foreignlanguagetester.services.ConsoleService;
import com.github.szgabsz91.foreignlanguagetester.services.TestService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ResetCommands implements ICommand {

    private final TestService testService;
    private final ConsoleService consoleService;

    public ResetCommands(TestService testService, ConsoleService consoleService) {
        this.testService = testService;
        this.consoleService = consoleService;
    }

    @ShellMethod(key = "reset-all", group = COMMAND_GROUP, value = "Resets the counters of every question")
    public void resetAll() {
        this.testService.resetCounters();
        this.testService.saveResults();
        this.consoleService.useColor(OutputColor.GREEN, () -> this.consoleService.write("%nSuccessfully reset every counter.%n%n"));
    }

}
