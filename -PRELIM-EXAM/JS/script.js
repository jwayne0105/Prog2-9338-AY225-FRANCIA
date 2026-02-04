/**
 * Programmer: [Your Name] - [Student ID]
 */

const rawData = `StudentID,first_name,last_name,LAB1,LAB2,LAB3,PRELIM_EXAM,ATTENDANCE
073900438,Osbourne,Wakenshaw,85,88,90,89,92
114924014,Albie,Gierardi,78,92,85,94,97
111901632,Eleen,Pentony,88,81,84,86,90
084000084,Arie,Okenden,75,85,80,85,88
272471551,Alica,Muckley,90,86,97,95,95`;

let studentArray = [];

function init() {
    const savedData = localStorage.getItem('studentRecords'); //
    if (savedData) {
        studentArray = JSON.parse(savedData); //
    } else {
        const lines = rawData.trim().split('\n').slice(1);
        lines.forEach(line => {
            const cols = line.split(',');
            studentArray.push({
                id: cols[0],
                name: `${cols[1]} ${cols[2]}`,
                grade: cols[6]
            });
        });
    }
    renderTable();
}

function renderTable() {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = studentArray.map((student, index) => `
        <tr>
            <td style="color:#4834d4; font-weight:bold;">${student.id}</td>
            <td>${student.name}</td>
            <td>${student.grade}</td>
            <td><button class="btn-delete" onclick="deleteRecord(${index})">Delete</button></td>
        </tr>
    `).join('');
}

function addRecord() {
    const id = document.getElementById('stuID').value;
    const name = document.getElementById('stuName').value;
    const grade = document.getElementById('stuGrade').value;

    if (id && name && grade) {
        // Removed 100 max grade check as requested
        studentArray.push({ id, name, grade });
        localStorage.setItem('studentRecords', JSON.stringify(studentArray)); //
        renderTable();
        
        // Clear inputs
        document.getElementById('stuID').value = '';
        document.getElementById('stuName').value = '';
        document.getElementById('stuGrade').value = '';
    } else {
        alert("Please complete the form.");
    }
}

function deleteRecord(index) {
    if(confirm("Delete this record?")) {
        studentArray.splice(index, 1);
        localStorage.setItem('studentRecords', JSON.stringify(studentArray)); //
        renderTable();
    }
}

init();