/**
 * =====================================================
 * Student Name    : Justine Wayne S. Francia
 * Student ID      : 25-1671-710
 * Course          : BSCSIT 9338 Programming 2
 * Assignment      : Programming Assignment 1 - 3x3 Matrix Determinant Solver
 * School          : University of Perpetual Help System DALTA, Molino Campus
 * Date            : 2026-03-18
 * GitHub Repo     : https://github.com/jwayne0105/Prog2-9338-AY225-FRANCIA
 * Runtime         : Node.js (run with: node determinant_solver.js)
 *
 * Description:
 *   This program computes the determinant of a hardcoded 3x3 matrix
 *   using cofactor expansion along the first row and prints all steps.
 * =====================================================
 */

// Assigned 3x3 matrix (row-major)
const matrix = [
    [1, 4, 2],
    [3, 2, 5],
    [6, 1, 3]
];

// 2x2 determinant helper: ad - bc
function computeMinor(a, b, c, d) {
    return (a * d) - (b * c);
}

// Print matrix neatly
function printMatrix(m) {
    m.forEach((row) => {
        console.log(`  | ${String(row[0]).padStart(2)} ${String(row[1]).padStart(2)} ${String(row[2]).padStart(2)} |`);
    });
}

// Solve determinant via cofactor expansion along row 1
function solveDeterminant(m) {
    const line = "===================================================";
    console.log(line);
    console.log("  3x3 MATRIX DETERMINANT SOLVER");
    console.log("  Student: Justine Wayne S. Francia");
    console.log("  Assigned Matrix:");
    console.log(line);
    printMatrix(m);
    console.log(line);
    console.log();
    console.log("Expanding along Row 1 (cofactor expansion):");
    console.log();

    // Minor M11
    const minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
    const m11ad = m[1][1] * m[2][2];
    const m11bc = m[1][2] * m[2][1];
    console.log(
        `  Step 1 - Minor M11: det([${m[1][1]},${m[1][2]}],[${m[2][1]},${m[2][2]}]) = ` +
        `(${m[1][1]}*${m[2][2]}) - (${m[1][2]}*${m[2][1]}) = ${m11ad} - ${m11bc} = ${minor11}`
    );

    // Minor M12
    const minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
    const m12ad = m[1][0] * m[2][2];
    const m12bc = m[1][2] * m[2][0];
    console.log(
        `  Step 2 - Minor M12: det([${m[1][0]},${m[1][2]}],[${m[2][0]},${m[2][2]}]) = ` +
        `(${m[1][0]}*${m[2][2]}) - (${m[1][2]}*${m[2][0]}) = ${m12ad} - ${m12bc} = ${minor12}`
    );

    // Minor M13
    const minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
    const m13ad = m[1][0] * m[2][1];
    const m13bc = m[1][1] * m[2][0];
    console.log(
        `  Step 3 - Minor M13: det([${m[1][0]},${m[1][1]}],[${m[2][0]},${m[2][1]}]) = ` +
        `(${m[1][0]}*${m[2][1]}) - (${m[1][1]}*${m[2][0]}) = ${m13ad} - ${m13bc} = ${minor13}`
    );

    // Cofactor terms (+ - +)
    const c11 =  m[0][0] * minor11;
    const c12 = -m[0][1] * minor12;
    const c13 =  m[0][2] * minor13;

    console.log();
    console.log(`  Cofactor C11 = (+1) * ${m[0][0]} * ${minor11} = ${c11}`);
    console.log(`  Cofactor C12 = (-1) * ${m[0][1]} * ${minor12} = ${c12}`);
    console.log(`  Cofactor C13 = (+1) * ${m[0][2]} * ${minor13} = ${c13}`);

    // Final determinant
    const det = c11 + c12 + c13;
    console.log();
    console.log(`  det(M) = ${c11} + (${c12}) + (${c13})`);
    console.log(line);
    console.log(`  DETERMINANT = ${det}`);

    if (det === 0) {
        console.log("  The matrix is SINGULAR — it has no inverse.");
    }
    console.log(line);
}

// Entry point
solveDeterminant(matrix);
