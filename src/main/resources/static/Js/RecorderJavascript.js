let start = true,
    mediaRecorder,
    audioChunks = [];
   const btnRecorder = document.getElementById("startBtnRecord");

    btnRecorder.addEventListener("click",function (event) {
        alert("btn Record");
    });


// document.getElementById('stopBtn').addEventListener('click', () => {
//     mediaRecorder.stop();
//     document.getElementById('startBtn').disabled = false;
//     document.getElementById('stopBtn').disabled = true;
// });