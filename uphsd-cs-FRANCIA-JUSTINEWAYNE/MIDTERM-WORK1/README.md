# 3x3 Matrix Determinant Solver - Assignment 01

## Student Information
- Name: Justine Wayne S. Francia
- Student ID: 25-1671-710
- Section: BSCSIT 9338 Programming 2
- Course: BSCSIT 1203 Programming 2
- School: University of Perpetual Help System DALTA, Molino Campus
- Date: 2026-03-18
- GitHub Repository: https://github.com/jwayne0105/Prog2-9338-AY225-FRANCIA

## Assignment Summary
This project computes the determinant of a fixed 3x3 matrix using cofactor expansion along the first row in both Java and JavaScript (Node.js), with step-by-step output.

## Assigned Matrix
```text
| 1  4  2 |
| 3  2  5 |
| 6  1  3 |
```

## Project Files
- `DeterminantSolver.java`
- `determinant_solver.js`
- `README.md`

## How to Run (Java)
Run inside the `MIDTERM-work1` folder:

```bash
javac DeterminantSolver.java
java DeterminantSolver
```

## How to Run (JavaScript)
Run inside the `MIDTERM-work1` folder:

```bash
node determinant_solver.js
```

## Sample Output (Java)
```text
===================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: Justine Wayne S. Francia
  Assigned Matrix:
===================================================
  |  1  4  2 |
  |  3  2  5 |
  |  6  1  3 |
===================================================

Expanding along Row 1 (cofactor expansion):

  Step 1 - Minor M11: det([2,5],[1,3]) = (2*3) - (5*1) = 6 - 5 = 1
  Step 2 - Minor M12: det([3,5],[6,3]) = (3*3) - (5*6) = 9 - 30 = -21
  Step 3 - Minor M13: det([3,2],[6,1]) = (3*1) - (2*6) = 3 - 12 = -9

  Cofactor C11 = (+1) * 1 * 1 = 1
  Cofactor C12 = (-1) * 4 * -21 = 84
  Cofactor C13 = (+1) * 2 * -9 = -18

  det(M) = 1 + (84) + (-18)
===================================================
  DETERMINANT = 67
===================================================
```

## Sample Output (JavaScript)
```text
===================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: Justine Wayne S. Francia
  Assigned Matrix:
===================================================
  |  1  4  2 |
  |  3  2  5 |
  |  6  1  3 |
===================================================

Expanding along Row 1 (cofactor expansion):

  Step 1 - Minor M11: det([2,5],[1,3]) = (2*3) - (5*1) = 6 - 5 = 1
  Step 2 - Minor M12: det([3,5],[6,3]) = (3*3) - (5*6) = 9 - 30 = -21
  Step 3 - Minor M13: det([3,2],[6,1]) = (3*1) - (2*6) = 3 - 12 = -9

  Cofactor C11 = (+1) * 1 * 1 = 1
  Cofactor C12 = (-1) * 4 * -21 = 84
  Cofactor C13 = (+1) * 2 * -9 = -18

  det(M) = 1 + (84) + (-18)
===================================================
  DETERMINANT = 67
===================================================
```

## Final Determinant Value
- det(M) = 67

## Notes
- Both programs compute the same determinant value.
- No math library is used for determinant calculation.
- Both programs include a singular matrix check (`det == 0` or `det === 0`).
