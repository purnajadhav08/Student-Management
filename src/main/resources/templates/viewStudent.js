document.addEventListener("DOMContentLoaded", function () {
    fetch("/students/view")
        .then(response => response.json())
        .then(data => {
            // Assuming data is an array of student objects
            displayStudents(data);
        })
        .catch(error => console.error("Error fetching student data:", error));
});

function displayStudents(students) {
    // Assuming there's a <tbody> element with id "studentTableBody" in your HTML
    const tableBody = document.getElementById("studentTableBody");

    // Clear existing table rows
    tableBody.innerHTML = "";

    // Iterate over each student and create table rows
    students.forEach(student => {
        const row = document.createElement("tr");

        // Iterate over each property of the student object and create table cells
        Object.values(student).forEach(value => {
            const cell = document.createElement("td");
            cell.textContent = value;
            row.appendChild(cell);
        });

        tableBody.appendChild(row);
    });
}
