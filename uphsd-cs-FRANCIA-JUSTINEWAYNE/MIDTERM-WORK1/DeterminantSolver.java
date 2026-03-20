
/**
 * =====================================================
 * Student Name    : Justine Wayne S. Francia
 * Student ID      : 25-1671-710
 * Course          : BSCSIT 9338 Programming 2
 * Assignment      : Programming Assignment 1 - 3x3 Matrix Determinant Solver
 * School          : University of Perpetual Help System DALTA, Molino Campus
 * Date            : 2026-03-18
 * GitHub Repo     : https://github.com/jwayne0105/Prog2-9338-AY225-FRANCIA
 *
 * Description:
 *   This program computes the determinant of a hardcoded 3x3 matrix assigned
 *   to Justine Wayne S. Francia using cofactor expansion along the first row.
 * =====================================================
 */
public class DeterminantSolver {

    // Assigned 3x3 matrix (row-major)
    static int[][] matrix = {
        {1, 4, 2},
        {3, 2, 5},
        {6, 1, 3}
    };

    // 2x2 determinant helper: ad - bc
    static int computeMinor(int a, int b, int c, int d) {
        return (a * d) - (b * c);
    }

    // Print matrix neatly
    static void printMatrix(int[][] m) {
        for (int[] row : m) {
            System.out.printf("  | %2d %2d %2d |%n", row[0], row[1], row[2]);
        }
    }

    // Solve determinant via cofactor expansion along row 1
    static void solveDeterminant(int[][] m) {
        String line = "===================================================";
        System.out.println(line);
        System.out.println("  3x3 MATRIX DETERMINANT SOLVER");
        System.out.println("  Student: Justine Wayne S. Francia");
        System.out.println("  Assigned Matrix:");
        System.out.println(line);
        printMatrix(m);
        System.out.println(line);
        System.out.println();
        System.out.println("Expanding along Row 1 (cofactor expansion):");
        System.out.println();

        // Minor M11
        int minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
        int m11ad = m[1][1] * m[2][2];
        int m11bc = m[1][2] * m[2][1];
        System.out.printf(
            "  Step 1 - Minor M11: det([%d,%d],[%d,%d]) = (%d*%d) - (%d*%d) = %d - %d = %d%n",
            m[1][1], m[1][2], m[2][1], m[2][2],
            m[1][1], m[2][2], m[1][2], m[2][1],
            m11ad, m11bc, minor11
        );

        // Minor M12
        int minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
        int m12ad = m[1][0] * m[2][2];
        int m12bc = m[1][2] * m[2][0];
        System.out.printf(
            "  Step 2 - Minor M12: det([%d,%d],[%d,%d]) = (%d*%d) - (%d*%d) = %d - %d = %d%n",
            m[1][0], m[1][2], m[2][0], m[2][2],
            m[1][0], m[2][2], m[1][2], m[2][0],
            m12ad, m12bc, minor12
        );

        // Minor M13
        int minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
        int m13ad = m[1][0] * m[2][1];
        int m13bc = m[1][1] * m[2][0];
        System.out.printf(
            "  Step 3 - Minor M13: det([%d,%d],[%d,%d]) = (%d*%d) - (%d*%d) = %d - %d = %d%n",
            m[1][0], m[1][1], m[2][0], m[2][1],
            m[1][0], m[2][1], m[1][1], m[2][0],
            m13ad, m13bc, minor13
        );

        // Cofactor terms (+ - +)
        int c11 =  m[0][0] * minor11;
        int c12 = -m[0][1] * minor12;
        int c13 =  m[0][2] * minor13;

        System.out.println();
        System.out.printf("  Cofactor C11 = (+1) * %d * %d = %d%n", m[0][0], minor11, c11);
        System.out.printf("  Cofactor C12 = (-1) * %d * %d = %d%n", m[0][1], minor12, c12);
        System.out.printf("  Cofactor C13 = (+1) * %d * %d = %d%n", m[0][2], minor13, c13);

        // Final determinant
        int det = c11 + c12 + c13;
        System.out.println();
        System.out.printf("  det(M) = %d + (%d) + (%d)%n", c11, c12, c13);
        System.out.println(line);
        System.out.printf("  DETERMINANT = %d%n", det);

        if (det == 0) {
            System.out.println("  The matrix is SINGULAR - it has no inverse.");
        }
        System.out.println(line);
    }

    public static void main(String[] args) {
        solveDeterminant(matrix);
    }
}