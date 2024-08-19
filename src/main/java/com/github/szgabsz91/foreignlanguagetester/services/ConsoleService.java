package com.github.szgabsz91.foreignlanguagetester.services;

import com.github.szgabsz91.foreignlanguagetester.models.OutputColor;
import org.springframework.stereotype.Service;

import java.util.Scanner;
import java.util.function.Supplier;

@Service
public class ConsoleService {

    private static final String ANSI_RESET = "\u001B[0m";

    private final Scanner scanner = new Scanner(System.in);

    public String read() {
        return this.scanner.nextLine();
    }

    public void write(String line, Object... args) {
        System.out.printf(line, args);
        System.out.flush();
    }

    public void deleteLastLine() {
        this.write("\033[1A\033[2K");
    }

    public void useColor(OutputColor color, Runnable operations) {
        System.out.printf(color.getAnsiCode());
        operations.run();
        System.out.printf(ANSI_RESET);
        System.out.flush();
    }

    public String useColor(OutputColor color, Supplier<String> operations) {
        System.out.printf(color.getAnsiCode());
        var result = operations.get();
        System.out.printf(ANSI_RESET);
        System.out.flush();
        return result;
    }

}
