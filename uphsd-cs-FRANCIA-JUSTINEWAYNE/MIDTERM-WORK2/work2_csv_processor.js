/**
 * =====================================================
 * Student Name    : Justine Wayne S. Francia
 * Student ID      : 25-1671-710
 * Course          : BSCSIT 9338 Programming 2
 * Machine Problem : MP05, MP11, MP18 (CSV Dataset Processing)
 * Date            : 2026-03-18
 *
 * Description:
 *   This script asks for a CSV file path first, then processes:
 *   MP05 - Extract and display selected column
 *   MP11 - Frequency count for selected column values
 *   MP18 - Remove rows with empty fields
 * =====================================================
 */

const fs = require("fs");
const readline = require("readline");

// Helper for user input prompts.
function ask(rl, question) {
    return new Promise((resolve) => rl.question(question, (ans) => resolve(ans.trim())));
}

// CSV parser with support for quoted commas.
function parseCsvLine(line) {
    if (line == null) return [];
    if (line.length > 0 && line.charCodeAt(0) === 0xfeff) {
        line = line.slice(1); // remove BOM
    }

    const cells = [];
    let current = "";
    let inQuotes = false;

    for (let i = 0; i < line.length; i++) {
        const ch = line[i];

        if (ch === "\"") {
            if (inQuotes && i + 1 < line.length && line[i + 1] === "\"") {
                current += "\"";
                i++;
            } else {
                inQuotes = !inQuotes;
            }
        } else if (ch === "," && !inQuotes) {
            cells.push(current);
            current = "";
        } else {
            current += ch;
        }
    }

    cells.push(current);
    return cells;
}

// Find index of header row containing "Candidate".
function findHeaderIndex(rows) {
    for (let i = 0; i < rows.length; i++) {
        for (const cell of rows[i]) {
            if (cell.trim().toLowerCase() === "candidate") return i;
        }
    }
    return -1;
}

// Find last non-empty column index.
function lastNonEmptyIndex(row) {
    let idx = -1;
    for (let i = 0; i < row.length; i++) {
        if (row[i].trim() !== "") idx = i;
    }
    return idx;
}

// Normalize row to fixed number of columns.
function normalizeRow(row, columns) {
    const out = [];
    for (let i = 0; i < columns; i++) {
        out.push(i < row.length ? row[i].trim() : "");
    }
    return out;
}

// Check if every cell is empty.
function isCompletelyEmpty(row) {
    return row.every((v) => v.trim() === "");
}

// Check if no cell is empty.
function hasNoEmptyField(row) {
    return row.every((v) => v.trim() !== "");
}

// Escape cell for writing CSV.
function escapeCsvCell(cell) {
    const value = cell == null ? "" : String(cell);
    const escaped = value.replace(/"/g, "\"\"");
    const needsQuotes = /[,"\n\r]/.test(escaped);
    return needsQuotes ? `"${escaped}"` : escaped;
}

// Join row into CSV line.
function toCsvLine(row) {
    return row.map(escapeCsvCell).join(",");
}

// Print columns with index.
function printColumns(header) {
    console.log("Available columns:");
    header.forEach((name, i) => {
        console.log(`${String(i + 1).padStart(2, " ")} - ${name}`);
    });
}

// Resolve column input by number or exact name.
function resolveColumnIndex(input, header) {
    if (/^\d+$/.test(input)) {
        const idx = Number(input) - 1;
        return idx >= 0 && idx < header.length ? idx : -1;
    }

    for (let i = 0; i < header.length; i++) {
        if (header[i].toLowerCase() === input.toLowerCase()) return i;
    }

    return -1;
}

// Load and prepare dataset.
function loadDataset(filePath) {
    if (!fs.existsSync(filePath)) {
        throw new Error("File not found.");
    }

    const content = fs.readFileSync(filePath, "utf8");
    const lines = content.split(/\r?\n/);
    const rawRows = lines.map(parseCsvLine);

    if (rawRows.length === 0) {
        throw new Error("Empty CSV.");
    }

    const headerIndex = findHeaderIndex(rawRows);
    if (headerIndex < 0) {
        throw new Error("Header row not found (expected 'Candidate').");
    }

    const rawHeader = rawRows[headerIndex];
    const columns = lastNonEmptyIndex(rawHeader) + 1;
    if (columns <= 0) {
        throw new Error("Invalid header.");
    }

    const header = [];
    for (let i = 0; i < columns; i++) {
        let col = i < rawHeader.length ? rawHeader[i].trim() : "";
        if (col === "") col = `Column${i + 1}`;
        header.push(col);
    }

    const rows = [];
    for (let i = headerIndex + 1; i < rawRows.length; i++) {
        const normalized = normalizeRow(rawRows[i], columns);
        if (!isCompletelyEmpty(normalized)) {
            rows.push(normalized);
        }
    }

    console.log("\nDataset loaded successfully.");
    console.log(`Columns: ${header.length}`);
    console.log(`Data rows: ${rows.length}`);

    return { header, rows };
}

// MP05
async function runMp05(rl, ds) {
    console.log("\n=== MP05 ===");
    printColumns(ds.header);

    const input = await ask(rl, "Enter column number or exact name: ");
    const index = resolveColumnIndex(input, ds.header);

    if (index < 0) {
        console.log("Invalid column.");
        return;
    }

    const colName = ds.header[index];
    let count = 0;

    console.log(`\nColumn: ${colName}`);
    console.log("-------------------------------------------");
    for (const row of ds.rows) {
        const value = row[index].trim();
        if (value !== "") {
            count++;
            console.log(`${String(count).padStart(4, " ")}. ${value}`);
        }
    }
    console.log("-------------------------------------------");
    console.log(`Total non-empty values: ${count}`);
}

// MP11
async function runMp11(rl, ds) {
    console.log("\n=== MP11 ===");
    printColumns(ds.header);

    const input = await ask(rl, "Enter column number or exact name: ");
    const index = resolveColumnIndex(input, ds.header);

    if (index < 0) {
        console.log("Invalid column.");
        return;
    }

    const freq = new Map();
    for (const row of ds.rows) {
        const value = row[index].trim();
        if (value !== "") {
            freq.set(value, (freq.get(value) || 0) + 1);
        }
    }

    if (freq.size === 0) {
        console.log("No non-empty values found.");
        return;
    }

    const entries = [...freq.entries()].sort((a, b) => {
        if (b[1] !== a[1]) return b[1] - a[1];
        return a[0].localeCompare(b[0]);
    });

    console.log("No.  | Value                                         | Count");
    console.log("------------------------------------------------------------------");
    entries.forEach(([value, count], i) => {
        const v = value.length > 45 ? `${value.slice(0, 42)}...` : value;
        console.log(`${String(i + 1).padEnd(4, " ")} | ${v.padEnd(45, " ")} | ${count}`);
    });
    console.log("------------------------------------------------------------------");
    console.log(`Unique values: ${entries.length}`);
}

// MP18
async function runMp18(rl, ds) {
    console.log("\n=== MP18 ===");
    const cleaned = ds.rows.filter((row) => hasNoEmptyField(row));

    const removed = ds.rows.length - cleaned.length;
    console.log(`Original rows: ${ds.rows.length}`);
    console.log(`Rows kept: ${cleaned.length}`);
    console.log(`Rows removed: ${removed}`);

    const preview = Math.min(10, cleaned.length);
    console.log(`\nPreview (first ${preview} cleaned rows):`);
    for (let i = 0; i < preview; i++) {
        console.log(toCsvLine(cleaned[i]));
    }

    const outPath = await ask(rl, "\nEnter output CSV path (or press Enter to skip): ");
    if (outPath === "") {
        console.log("Export skipped.");
        return;
    }

    const lines = [toCsvLine(ds.header), ...cleaned.map((row) => toCsvLine(row))];
    fs.writeFileSync(outPath, lines.join("\n"), "utf8");
    console.log(`Cleaned CSV saved: ${outPath}`);
}

async function main() {
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });

    try {
        console.log("=== WORK 2 CSV PROCESSOR (MP05, MP11, MP18) ===");
        const filePath = await ask(rl, "Enter CSV dataset file path: ");

        const ds = loadDataset(filePath);

        while (true) {
            const choice = await ask(
                rl,
                "\nChoose MP:\n" +
                "5  - MP05: Extract and display selected column\n" +
                "11 - MP11: Frequency count for column values\n" +
                "18 - MP18: Remove rows with empty fields\n" +
                "0  - Exit\n" +
                "Enter choice: "
            );

            if (choice === "5") {
                await runMp05(rl, ds);
            } else if (choice === "11") {
                await runMp11(rl, ds);
            } else if (choice === "18") {
                await runMp18(rl, ds);
            } else if (choice === "0") {
                console.log("Program ended.");
                break;
            } else {
                console.log("Invalid choice.");
            }
        }
    } catch (err) {
        console.log(`Error: ${err.message}`);
    } finally {
        rl.close();
    }
}

main();