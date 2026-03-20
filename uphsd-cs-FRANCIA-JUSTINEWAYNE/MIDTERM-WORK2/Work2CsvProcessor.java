/**
 * =====================================================
 * Student Name    : Justine Wayne S. Francia
 * Student ID      : 25-1671-710
 * Course          : BSCSIT 9338 Programming 2
 * Machine Problem : MP05, MP11, MP18 (CSV Dataset Processing)
 * Date            : 2026-03-18
 *
 * Description:
 *   This program asks for the CSV dataset path first, then processes:
 *   MP05 - Extract selected column
 *   MP11 - Frequency count of a selected column
 *   MP18 - Remove rows with empty fields
 * =====================================================
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Work2CsvProcessor {

    // Container for parsed dataset content.
    static class Dataset {
        List<String> header;
        List<List<String>> rows;
        int effectiveColumns;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== WORK 2 CSV PROCESSOR (MP05, MP11, MP18) ===");
        System.out.print("Enter CSV dataset file path: ");
        String datasetPath = scanner.nextLine().trim();

        Dataset dataset = readDataset(datasetPath);
        if (dataset == null) {
            scanner.close();
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("Select machine problem:");
            System.out.println("5  - MP05: Extract and display a selected column");
            System.out.println("11 - MP11: Frequency count for column values");
            System.out.println("18 - MP18: Remove rows with empty fields");
            System.out.println("0  - Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            if ("5".equals(choice)) {
                runMp05(scanner, dataset);
            } else if ("11".equals(choice)) {
                runMp11(scanner, dataset);
            } else if ("18".equals(choice)) {
                runMp18(scanner, dataset);
            } else if ("0".equals(choice)) {
                System.out.println("Program ended.");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    // Reads CSV file and extracts usable header + data rows.
    private static Dataset readDataset(String path) {
        Path file = Paths.get(path);

        if (!Files.exists(file)) {
            System.out.println("Error: File not found -> " + path);
            return null;
        }

        List<List<String>> allRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                allRows.add(parseCsvLine(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }

        if (allRows.isEmpty()) {
            System.out.println("Error: CSV file is empty.");
            return null;
        }

        int headerIndex = findHeaderRowIndex(allRows);
        if (headerIndex == -1) {
            System.out.println("Error: Could not find CSV header row (expected 'Candidate').");
            return null;
        }

        List<String> rawHeader = allRows.get(headerIndex);
        int effectiveColumns = findEffectiveColumnCount(rawHeader);
        if (effectiveColumns == 0) {
            System.out.println("Error: Header has no usable columns.");
            return null;
        }

        List<String> header = normalizeHeader(rawHeader, effectiveColumns);
        List<List<String>> dataRows = new ArrayList<>();

        for (int i = headerIndex + 1; i < allRows.size(); i++) {
            List<String> row = normalizeRow(allRows.get(i), effectiveColumns);
            if (!isRowCompletelyEmpty(row)) {
                dataRows.add(row);
            }
        }

        Dataset dataset = new Dataset();
        dataset.header = header;
        dataset.rows = dataRows;
        dataset.effectiveColumns = effectiveColumns;

        System.out.println();
        System.out.println("Dataset loaded successfully.");
        System.out.println("Detected columns: " + effectiveColumns);
        System.out.println("Detected data rows: " + dataRows.size());

        return dataset;
    }

    // MP05: display all non-empty values of user-selected column.
    private static void runMp05(Scanner scanner, Dataset dataset) {
        System.out.println();
        System.out.println("=== MP05: Extract and Display Selected Column ===");
        printColumns(dataset.header);

        int columnIndex = askColumnIndex(scanner, dataset.header);
        if (columnIndex == -1) {
            System.out.println("Invalid column selection.");
            return;
        }

        String selectedName = dataset.header.get(columnIndex);
        int count = 0;

        System.out.println();
        System.out.println("Selected column: " + selectedName);
        System.out.println("------------------------------------------------");

        for (List<String> row : dataset.rows) {
            String value = row.get(columnIndex).trim();
            if (!value.isEmpty()) {
                count++;
                System.out.printf("%4d. %s%n", count, value);
            }
        }

        System.out.println("------------------------------------------------");
        System.out.println("Non-empty values displayed: " + count);
    }

    // MP11: frequency count of unique values from selected column.
    private static void runMp11(Scanner scanner, Dataset dataset) {
        System.out.println();
        System.out.println("=== MP11: Frequency Count for Column Values ===");
        printColumns(dataset.header);

        int columnIndex = askColumnIndex(scanner, dataset.header);
        if (columnIndex == -1) {
            System.out.println("Invalid column selection.");
            return;
        }

        String selectedName = dataset.header.get(columnIndex);
        Map<String, Integer> frequency = new HashMap<>();

        for (List<String> row : dataset.rows) {
            String value = row.get(columnIndex).trim();
            if (!value.isEmpty()) {
                frequency.put(value, frequency.getOrDefault(value, 0) + 1);
            }
        }

        if (frequency.isEmpty()) {
            System.out.println("No non-empty values found in column: " + selectedName);
            return;
        }

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(frequency.entrySet());
        sorted.sort((a, b) -> {
            int byCount = Integer.compare(b.getValue(), a.getValue());
            if (byCount != 0) {
                return byCount;
            }
            return a.getKey().compareToIgnoreCase(b.getKey());
        });

        System.out.println();
        System.out.println("Column: " + selectedName);
        System.out.printf("%-5s | %-45s | %s%n", "No.", "Value", "Count");
        System.out.println("--------------------------------------------------------------------");

        int index = 1;
        for (Map.Entry<String, Integer> entry : sorted) {
            System.out.printf("%-5d | %-45s | %d%n", index++, shorten(entry.getKey(), 45), entry.getValue());
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Unique values: " + sorted.size());
    }

    // MP18: remove rows that have at least one empty field.
    private static void runMp18(Scanner scanner, Dataset dataset) {
        System.out.println();
        System.out.println("=== MP18: Remove Rows with Empty Fields ===");

        List<List<String>> cleanedRows = new ArrayList<>();

        for (List<String> row : dataset.rows) {
            if (hasNoEmptyField(row)) {
                cleanedRows.add(row);
            }
        }

        int originalCount = dataset.rows.size();
        int cleanedCount = cleanedRows.size();
        int removedCount = originalCount - cleanedCount;

        System.out.println("Original data rows: " + originalCount);
        System.out.println("Rows kept (no empty fields): " + cleanedCount);
        System.out.println("Rows removed: " + removedCount);

        System.out.println();
        System.out.println("Preview of cleaned rows (first 10):");
        int preview = Math.min(10, cleanedRows.size());
        for (int i = 0; i < preview; i++) {
            System.out.println(toCsvLine(cleanedRows.get(i)));
        }

        System.out.println();
        System.out.print("Enter output CSV file path to save cleaned rows (or press Enter to skip): ");
        String outputPath = scanner.nextLine().trim();

        if (outputPath.isEmpty()) {
            System.out.println("Export skipped.");
            return;
        }

        boolean written = writeCleanedCsv(outputPath, dataset.header, cleanedRows);
        if (written) {
            System.out.println("Cleaned CSV saved to: " + outputPath);
        }
    }

    // Finds row that contains "Candidate" (expected header row).
    private static int findHeaderRowIndex(List<List<String>> rows) {
        for (int i = 0; i < rows.size(); i++) {
            for (String cell : rows.get(i)) {
                if ("candidate".equalsIgnoreCase(cell.trim())) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Trims trailing empty header columns.
    private static int findEffectiveColumnCount(List<String> rawHeader) {
        int lastNonEmpty = -1;
        for (int i = 0; i < rawHeader.size(); i++) {
            if (!rawHeader.get(i).trim().isEmpty()) {
                lastNonEmpty = i;
            }
        }
        return lastNonEmpty + 1;
    }

    // Normalizes header names.
    private static List<String> normalizeHeader(List<String> rawHeader, int columns) {
        List<String> header = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            String name = i < rawHeader.size() ? rawHeader.get(i).trim() : "";
            if (name.isEmpty()) {
                name = "Column" + (i + 1);
            }
            header.add(name);
        }
        return header;
    }

    // Normalizes row length to match header length.
    private static List<String> normalizeRow(List<String> row, int columns) {
        List<String> normalized = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            String value = i < row.size() ? row.get(i).trim() : "";
            normalized.add(value);
        }
        return normalized;
    }

    // Returns true if all cells are empty.
    private static boolean isRowCompletelyEmpty(List<String> row) {
        for (String value : row) {
            if (!value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Returns true if no field is empty.
    private static boolean hasNoEmptyField(List<String> row) {
        for (String value : row) {
            if (value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Prints column list with numbers.
    private static void printColumns(List<String> header) {
        System.out.println("Available columns:");
        for (int i = 0; i < header.size(); i++) {
            System.out.printf("%2d - %s%n", i + 1, header.get(i));
        }
    }

    // Reads user column selection (number or exact name).
    private static int askColumnIndex(Scanner scanner, List<String> header) {
        System.out.print("Enter column number or exact column name: ");
        String input = scanner.nextLine().trim();

        if (input.matches("\\d+")) {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < header.size()) {
                return idx;
            }
            return -1;
        }

        for (int i = 0; i < header.size(); i++) {
            if (header.get(i).equalsIgnoreCase(input)) {
                return i;
            }
        }

        return -1;
    }

    // Writes cleaned rows to new CSV file.
    private static boolean writeCleanedCsv(String outputPath, List<String> header, List<List<String>> rows) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write(toCsvLine(header));
            bw.newLine();

            for (List<String> row : rows) {
                bw.write(toCsvLine(row));
                bw.newLine();
            }

            return true;
        } catch (IOException e) {
            System.out.println("Error writing output file: " + e.getMessage());
            return false;
        }
    }

    // Converts list of values to a valid CSV line.
    private static String toCsvLine(List<String> values) {
        List<String> escaped = new ArrayList<>();
        for (String value : values) {
            escaped.add(escapeCsvCell(value));
        }
        return String.join(",", escaped);
    }

    // Escapes one CSV cell safely.
    private static String escapeCsvCell(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        boolean needsQuotes = escaped.contains(",") || escaped.contains("\"")
                || escaped.contains("\n") || escaped.contains("\r");
        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }

    // Parses one CSV line and supports quoted commas.
    private static List<String> parseCsvLine(String line) {
        if (line == null) {
            return Collections.emptyList();
        }

        // Remove UTF-8 BOM if present.
        if (!line.isEmpty() && line.charAt(0) == '\uFEFF') {
            line = line.substring(1);
        }

        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        values.add(current.toString());
        return values;
    }

    // Shortens long table text.
    private static String shorten(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}

