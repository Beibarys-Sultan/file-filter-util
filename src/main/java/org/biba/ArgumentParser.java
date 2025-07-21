package org.biba;

import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {

    private boolean appendMode;
    private boolean shortStats;
    private boolean fullStats;
    private Path outputDir = Path.of(".");
    private String prefix = "";
    private List<Path> inputFiles = new ArrayList<>();

    public void parse(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("a", "append", false, "режим добавления в файл");
        options.addOption("s", "short", false, "краткая статистика");
        options.addOption("f", "full", false, "полная статистика");
        options.addOption("o", "output", true, "директория вывода");
        options.addOption("p", "prefix", true, "префикс выходных файлов");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        this.appendMode = cmd.hasOption("a");
        this.shortStats = cmd.hasOption("s");
        this.fullStats = cmd.hasOption("f");

        if (!shortStats && !fullStats) {
            throw new ParseException("Укажите хотя бы один флаг статистики: -s или -f");
        }

        if (cmd.hasOption("o")) {
            outputDir = Path.of(cmd.getOptionValue("o"));
        }

        if (cmd.hasOption("p")) {
            prefix = cmd.getOptionValue("p");
        }

        // Оставшиеся аргументы — это пути к файлам
        for (String arg : cmd.getArgList()) {
            inputFiles.add(Path.of(arg));
        }

        if (inputFiles.isEmpty()) {
            throw new ParseException("Не указано ни одного входного файла");
        }
    }

    public boolean isAppendMode() { return appendMode; }
    public boolean isShortStats() { return shortStats; }
    public boolean isFullStats() { return fullStats; }
    public Path getOutputDir() { return outputDir; }
    public String getPrefix() { return prefix; }
    public List<Path> getInputFiles() { return inputFiles; }
}
