// viewLogs.js

// Function to render logs table
function renderLogsTable(logs) {
    const tableBody = document.querySelector('tbody');

    logs.forEach(log => {
        const row = document.createElement('tr');

        // Create table cells for each log attribute
        Object.keys(log).forEach(key => {
            const cell = document.createElement('td');
            cell.textContent = log[key];
            row.appendChild(cell);
        });

        tableBody.appendChild(row);
    });
}

// Fetch logs data from the server
function fetchLogs() {
    // Assuming you have an API endpoint to fetch logs data
    fetch('/api/logs')
        .then(response => response.json())
        .then(data => renderLogsTable(data))
        .catch(error => console.error('Error fetching logs:', error));
}

// Call fetchLogs function when the page is loaded
document.addEventListener('DOMContentLoaded', fetchLogs);
