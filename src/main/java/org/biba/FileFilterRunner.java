package org.biba;

import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileFilterRunner {

    public void run(String[] args) {

        ArgumentParser parser = new ArgumentParser();
        try {
            parser.parse(args);
        } catch (ParseException e) {
            System.err.println("Ошибка в аргументах: " + e.getMessage());
        }

        StatisticsCollector stats = new StatisticsCollector();

        try (OutputManager output = new OutputManager(
                parser.getOutputDir(),
                parser.getPrefix(),
                parser.isAppendMode()
        )) {
            for (Path file : parser.getInputFiles()) {
                if (!Files.exists(file)) {
                    System.err.println("Файл не найден: " + file);
                    continue;
                }

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty()) continue;

                        DataType type = DataClassifier.classify(line);
                        output.write(type, line);
                        stats.record(type, line);
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла: " + file + " — " + e.getMessage());
                }
            }
        }

        // Печать статистики
        if (parser.isShortStats()) stats.printShortStats();
        if (parser.isFullStats()) stats.printFullStats();
    }
}
