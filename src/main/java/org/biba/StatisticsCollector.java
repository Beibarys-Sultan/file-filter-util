package org.biba;

import java.util.*;

public class StatisticsCollector {

    private final Map<DataType, List<String>> dataMap = new EnumMap<>(DataType.class);

    public void record(DataType type, String value) {
        dataMap.computeIfAbsent(type, k -> new ArrayList<>()).add(value);
    }

    public boolean hasType(DataType type) {
        return dataMap.containsKey(type);
    }

    public int getCount(DataType type) {
        return dataMap.getOrDefault(type, Collections.emptyList()).size();
    }

    public List<String> getValues(DataType type) {
        return dataMap.getOrDefault(type, Collections.emptyList());
    }

    public void printShortStats() {
        System.out.println("\n Краткая статистика:");
        for (DataType type : DataType.values()) {
            if (hasType(type)) {
                System.out.printf("%s: %d строк%n", type.name(), getCount(type));
            }
        }
    }

    public void printFullStats() {
        System.out.println("\n Полная статистика:");
        for (DataType type : DataType.values()) {
            if (!hasType(type)) continue;

            System.out.printf("[%s]%n", type.name());

            List<String> values = getValues(type);
            System.out.printf("- Количество: %d%n", values.size());

            switch (type) {
                case INTEGER, FLOAT -> {
                    List<Double> nums = values.stream()
                            .map(Double::parseDouble)
                            .toList();
                    double min = nums.stream().min(Double::compareTo).orElse(0.0);
                    double max = nums.stream().max(Double::compareTo).orElse(0.0);
                    double sum = nums.stream().mapToDouble(Double::doubleValue).sum();
                    double avg = sum / nums.size();
                    System.out.printf("- Минимум: %s%n", min);
                    System.out.printf("- Максимум: %s%n", max);
                    System.out.printf("- Сумма: %.4f%n", sum);
                    System.out.printf("- Среднее: %.4f%n", avg);
                }
                case STRING -> {
                    int minLen = values.stream().mapToInt(String::length).min().orElse(0);
                    int maxLen = values.stream().mapToInt(String::length).max().orElse(0);
                    System.out.printf("- Самая короткая строка: %d символов%n", minLen);
                    System.out.printf("- Самая длинная строка: %d символов%n", maxLen);
                }
            }
        }
    }
}
