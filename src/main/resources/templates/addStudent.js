document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");

    // Event listener for form submission
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        // Check if the form action is for adding a new student
        if (form.action.includes("/students/add")) {
            // If it's for adding a new student, perform a POST request
            fetch("/students/add", {
                method: "POST",
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    // Redirect to home page after successful submission
                    window.location.href = "/";
                } else {
                    console.error("Error:", response.statusText);
                }
            })
            .catch(error => console.error("Error:", error));
        } else {
            // For other form actions, perform a GET request
            const url = form.action;
            window.location.href = url;
        }
    });
});
