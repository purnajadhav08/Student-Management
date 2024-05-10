// This function fetches enrollments data from the server and updates the HTML table
function fetchEnrollments() {
    // Make an AJAX request to fetch enrollments data
    fetch('/enrollments') // Replace '/enrollments' with your actual endpoint
        .then(response => response.json()) // Parse the JSON response
        .then(data => {
            // Select the table body element
            const tbody = document.querySelector('tbody');

            // Clear any existing rows in the table
            tbody.innerHTML = '';

            // Loop through the enrollments data and populate the table
            data.forEach(enrollment => {
                // Create a new table row
                const row = document.createElement('tr');

                // Create table data cells for each attribute of the enrollment
                const studentIdCell = document.createElement('td');
                studentIdCell.textContent = enrollment['Student ID'];
                const classIdCell = document.createElement('td');
                classIdCell.textContent = enrollment['Class ID'];
                const scoreCell = document.createElement('td');
                scoreCell.textContent = enrollment['Score'];

                // Append the cells to the row
                row.appendChild(studentIdCell);
                row.appendChild(classIdCell);
                row.appendChild(scoreCell);

                // Append the row to the table body
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching enrollments:', error);
        });
}

// Call the fetchEnrollments function when the page is loaded
window.onload = fetchEnrollments;
