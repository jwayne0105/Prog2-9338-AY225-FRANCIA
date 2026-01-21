// Beep sound
const beep = new Audio("beep.mp3");

// Valid users
const users = {
    wayne: "1234",
    justine: "abcd",
    nigger: "123"
};

// Load attendance from localStorage (kung meron na)
let attendanceList = JSON.parse(localStorage.getItem("attendance")) || [];

document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const message = document.getElementById("message");
    const timestampDiv = document.getElementById("timestamp");

    if (users[username] && users[username] === password) {
        const now = new Date();
        const timestamp = formatDate(now);

        message.style.color = "green";
        message.textContent = "Login successful! Welcome, " + username;
        timestampDiv.textContent = "Login Time: " + timestamp;

        // Add to attendance list
        attendanceList.push({
            user: username,
            time: timestamp
        });

        // Save to localStorage
        localStorage.setItem("attendance", JSON.stringify(attendanceList));

        // Generate updated attendance file
        generateAttendanceFile();
    } else {
        message.style.color = "red";
        message.textContent = "Incorrect username or password!";
        timestampDiv.textContent = "";
        beep.play();
    }
});

// Date format
function formatDate(date) {
    const mm = String(date.getMonth() + 1).padStart(2, "0");
    const dd = String(date.getDate()).padStart(2, "0");
    const yyyy = date.getFullYear();
    const hh = String(date.getHours()).padStart(2, "0");
    const min = String(date.getMinutes()).padStart(2, "0");
    const ss = String(date.getSeconds()).padStart(2, "0");

    return `${mm}/${dd}/${yyyy} ${hh}:${min}:${ss}`;
}

// Generate attendance file (ALL LOGINS)
function generateAttendanceFile() {
    let data = "Attendance Summary\n";
    data += "------------------\n";

    attendanceList.forEach((record, index) => {
        data += `${index + 1}. Username: ${record.user}\n`;
        data += `   Time: ${record.time}\n\n`;
    });

    const blob = new Blob([data], { type: "text/plain" });
    const link = document.createElement("a");

    link.href = URL.createObjectURL(blob);
    link.download = "attendance_summary.txt";
    link.click();
}
