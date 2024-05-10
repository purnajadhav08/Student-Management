document.addEventListener("DOMContentLoaded", function() {
    // Fetch courses when the page is loaded
    fetchCourses();
});

function fetchCourses() {
    fetch("/courses/view")
    .then(response => {
        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        return response.json();
    })
    .then(data => {
        // Call function to display courses on the page
        displayCourses(data);
    })
    .catch(error => {
        console.error("Error fetching courses:", error);
        // Handle error here
    });
}

function displayCourses(courses) {
    // Get the table body element where courses will be displayed
    const tbody = document.querySelector("table tbody");

    // Clear any existing content in the table body
    tbody.innerHTML = "";

    // Loop through the courses and create table rows
    courses.forEach(course => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${course.deptCode}</td>
            <td>${course.courseNumber}</td>
            <td>${course.title}</td>
        `;
        tbody.appendChild(row);
    });
}
