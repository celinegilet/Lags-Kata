package com.tof.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileHandler {

    private Console console;
    private String fileName;

    public FileHandler(Console console, String fileName) {
        this.console = console;
        this.fileName = fileName;
    }

    public List<String> readLines() throws IOException {
        try {
            return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            console.println("FICHIER " + fileName +" NON TROUVE. CREATION FICHIER.");
            throw e;
        }
    }

    public void writeLines(List<String> lines) {
        try {
            Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
        } catch (IOException e) {
            console.println("PROBLEME AVEC FICHIER");
        }
    }

}
