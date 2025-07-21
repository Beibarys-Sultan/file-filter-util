package org.biba;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

public class OutputManager implements AutoCloseable {

    private final Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);
    private final Path outputDir;
    private final String prefix;
    private final boolean append;

    public OutputManager(Path outputDir, String prefix, boolean append) {
        this.outputDir = outputDir;
        this.prefix = prefix;
        this.append = append;
    }

    public void write(DataType type, String value) throws IOException {
        BufferedWriter writer = writers.get(type);

        if (writer == null) {
            // Файл ещё не открыт — открыть его
            Path filePath = buildFilePath(type);
            OpenOption option = append ? StandardOpenOption.APPEND : StandardOpenOption.CREATE;
            writer = Files.newBufferedWriter(filePath,
                    StandardOpenOption.CREATE,
                    option,
                    StandardOpenOption.WRITE);
            writers.put(type, writer);
        }

        writer.write(value);
        writer.newLine();
    }

    private Path buildFilePath(DataType type) {
        String suffix = switch (type) {
            case INTEGER -> "integers.txt";
            case FLOAT -> "floats.txt";
            case STRING -> "strings.txt";
        };
        return outputDir.resolve(prefix + suffix);
    }

    @Override
    public void close() {
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии файла: " + e.getMessage());
            }
        }
    }
}
