document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector("form");

    form.addEventListener("submit", function(event) {
        event.preventDefault(); // Prevent default form submission

        // Get form inputs
        const deptCode = document.getElementById("deptCode").value;
        const courseNumber = document.getElementById("courseNumber").value;
        const title = document.getElementById("title").value;

        // Perform any client-side validation here
        if (deptCode.trim() === "" || title.trim() === "") {
            alert("Please fill in all fields.");
            return;
        }

        // Create a new FormData object to send form data to the server
        const formData = new FormData();
        formData.append("deptCode", deptCode);
        formData.append("courseNumber", courseNumber);
        formData.append("title", title);

        // Send a POST request to the server
        fetch("/courses/add", {
            method: "POST",
            body: formData
        })
        .then(response => {
            if (response.ok) {
                // Display success message and clear form inputs
                alert("Course added successfully.");
                form.reset();
            } else {
                // Display error message if something went wrong
                alert("Failed to add course. Please try again later.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("An unexpected error occurred. Please try again later.");
        });
    });
});
