function computeGrades() {
    const TOTAL_MEETINGS = 30;

    let attendance = Number(document.getElementById("attendance").value);
    let lw1 = Number(document.getElementById("lw1").value);
    let lw2 = Number(document.getElementById("lw2").value);
    let lw3 = Number(document.getElementById("lw3").value);

    if (isNaN(attendance) || isNaN(lw1) || isNaN(lw2) || isNaN(lw3)) {
        alert("Please enter valid numbers!");
        return;
    }

    let attendanceScore = (attendance / TOTAL_MEETINGS) * 100;
    let labAverage = (lw1 + lw2 + lw3) / 3;
    let classStanding = (attendanceScore * 0.40) + (labAverage * 0.60);

    let requiredPass = (75 - (classStanding * 0.70)) / 0.30;
    let requiredExcellent = (100 - (classStanding * 0.70)) / 0.30;

    let remarks = classStanding >= 75
        ? "You are currently PASSING based on Class Standing."
        : "You need a higher Prelim Exam score to PASS.";

    document.getElementById("output").textContent =
        `ATTENDANCE SCORE: ${attendanceScore.toFixed(2)}
LAB WORK 1: ${lw1.toFixed(2)}
LAB WORK 2: ${lw2.toFixed(2)}
LAB WORK 3: ${lw3.toFixed(2)}

LAB WORK AVERAGE: ${labAverage.toFixed(2)}
CLASS STANDING: ${classStanding.toFixed(2)}

REQUIRED PRELIM EXAM TO PASS (75): ${requiredPass.toFixed(2)}
REQUIRED PRELIM EXAM FOR EXCELLENT (100): ${requiredExcellent.toFixed(2)}

${remarks}`;
}
