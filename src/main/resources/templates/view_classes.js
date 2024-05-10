document.addEventListener("DOMContentLoaded", function() {
    const table = document.getElementById("classesTable");
    const tbody = table.querySelector("tbody");

    // Get all table rows except the header row
    const rows = Array.from(tbody.querySelectorAll("tr:nth-child(n+2)"));

    // Sort the rows by class ID
    rows.sort((a, b) => {
        const classIdA = parseInt(a.cells[0].textContent);
        const classIdB = parseInt(b.cells[0].textContent);
        return classIdA - classIdB;
    });

    // Reorder the rows in the table
    rows.forEach(row => tbody.appendChild(row));
});
