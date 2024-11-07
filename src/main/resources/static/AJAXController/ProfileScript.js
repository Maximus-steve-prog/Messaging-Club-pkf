document.addEventListener('DOMContentLoaded', function() {
    const profileBtn = document.getElementById('profileBtn');
    const offCanvas = document.getElementById('offCanvas');
    const closeBtn = document.getElementById('closeBtn');

    profileBtn.addEventListener('click', function() {
        offCanvas.style.left = '3px';
        offCanvas.style.borderTopLeftRadius ='10px';
        offCanvas.style.borderBottomLeftRadius ='10px';
        offCanvas.style.borderTopRightRadius ='10px';
        offCanvas.style.borderBottomRightRadius ='10px';
        // Slide in the off-canvas
    });

    closeBtn.addEventListener('click', function() {
        offCanvas.style.left = '-320px'; // Slide out the off-canvas
    });




    const openModalBtn = document.getElementById('openModalBtn');
    const modal = document.getElementById('myModal');
    const closeBtnModal = document.querySelector('.closeModal');
    const fetchDataBtn = document.getElementById('fetchDataBtn');
    const modalBody = document.getElementById('modal-body');

    // Open the modal and add 'show' class
    openModalBtn.onclick = function() {
        modal.style.display = "flex"; // Use flex to center content
        setTimeout(() => {
            modal.classList.add('show'); // Trigger the show class after display
        }, 10); // Small delay to allow rendering
    }

    // Close the modal
    closeBtnModal.onclick = function() {
        modal.classList.remove('show'); // Remove show class for animation
        setTimeout(() => {
            modal.style.display = "none"; // Hide the modal after animation
        }, 900); // Match this with the CSS transition duration
    }

    // Close the modal when clicking outside of it
    window.onclick = function(event) {
        if (event.target === modal) {
            closeBtnModal.click();// Trigger the close button
        }
    }



    document.getElementById('InfoFriends').addEventListener('click', function() {
        document.getElementById('offcanvasInfo').classList.toggle('open');
    });

    document.getElementById('close-btn').addEventListener('click', function() {
        document.getElementById('offcanvasInfo').classList.remove('open');
    });


});