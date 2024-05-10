document.getElementById("deleteForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent the default form submission
    deleteStudent();
});

function deleteStudent() {
    var bNumber = document.getElementById("bNumber").value;

    // Make an AJAX request to delete the student
    fetch("/students/delete/" + bNumber, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            // Success message
            alert("Student deleted successfully.");
            // Redirect to the home page or any other page
            window.location.href = "/";
        } else {
            // Handle error response
            alert("Error deleting student. Please try again.");
        }
    })
    .catch(error => {
        console.error("Error deleting student:", error);
        alert("Error deleting student. Please try again.");
    });
}

