const beep = new Audio("beep.mp3");

let users = JSON.parse(localStorage.getItem("users")) || {
    wayne:"1234",
    justine:"abcd"
};

let attendance = JSON.parse(localStorage.getItem("attendance")) || [];

const loginBox = document.getElementById("loginBox");
const attendanceBox = document.getElementById("attendanceBox");
const attendanceList = document.getElementById("attendanceList");
const message = document.getElementById("message");
const passwordInput = document.getElementById("password");
const formTitle = document.getElementById("formTitle");
const submitBtn = document.getElementById("submitBtn");
const switchBtn = document.getElementById("switchBtn");

let registerMode = false;

/* SHOW PASSWORD */
document.getElementById("togglePassword").onclick = () =>{
    passwordInput.type =
    passwordInput.type === "password" ? "text" : "password";
};

/* LOGIN / REGISTER */
document.getElementById("loginForm").onsubmit = e =>{
    e.preventDefault();

    const u = username.value.trim();
    const p = passwordInput.value.trim();

    if(registerMode){
        if(users[u]){
            message.style.color="red";
            message.textContent="Username already exists";
            return;
        }
        users[u]=p;
        localStorage.setItem("users",JSON.stringify(users));
        message.style.color="green";
        message.textContent="Registered successfully!";
        toggleMode(false);
        return;
    }

    if(users[u] && users[u]===p){
        const time = new Date().toLocaleString();
        attendance.push({u,time});
        localStorage.setItem("attendance",JSON.stringify(attendance));

        loginBox.style.display="none";
        attendanceBox.style.display="block";
        document.body.classList.replace("login-bg","attendance-bg");

        welcomeText.textContent="Welcome, "+u;
        showAttendance();
        message.textContent="";
        passwordInput.value="";
    }else{
        message.style.color="red";
        message.textContent="Wrong username or password";
        beep.play();
    }
};

/* SWITCH LOGIN / REGISTER */
switchBtn.onclick = ()=>{
    toggleMode(!registerMode);
};

function toggleMode(state){
    registerMode = state;
    formTitle.textContent = state ? "Register" : "Login";
    submitBtn.textContent = state ? "Register" : "Log In";
    switchBtn.textContent = state ? "Back to Login" : "Register";
    message.textContent="";
}

/* ATTENDANCE DISPLAY */
function showAttendance(){
    attendanceList.innerHTML="";
    attendance.forEach((a,i)=>{
        attendanceList.innerHTML += `
        <div class="attendance-item">
        ${i+1}. <b>${a.u}</b><br>${a.time}
        </div>`;
    });
}

/* CLEAR */
clearAttendanceBtn.onclick = ()=>{
    if(confirm("Clear attendance?")){
        attendance=[];
        localStorage.removeItem("attendance");
        attendanceList.innerHTML="";
    }
};

/* BACK */
backBtn.onclick = ()=>{
    attendanceBox.style.display="none";
    loginBox.style.display="block";
    document.body.classList.replace("attendance-bg","login-bg");
};

/* FORGOT */
forgotBtn.onclick = ()=>{
    alert("Contact admin to reset password.");
};
