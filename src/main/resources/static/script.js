
const API_URL = "http://localhost:8080/employee";

const tableBody = document.getElementById("employeeTableBody");
const message = document.getElementById("message");

let currentPage = 0;
let totalPages = 0;
let editingEmployeeId = null;
let sortAttribute = "";
let sortOrder = "asc";

async function loadEmployees() {
	try {
		message.textContent = "Loading employees...";

		const query = document
			.getElementById("searchInput")
			.value.trim()
			.toLowerCase();

		let employees = [];

		if (query !== "") {
			// Fetch the first backend page
			const firstResponse = await fetch(
				`${API_URL}/getEmployeeBypageNumber/0`
			);

			if (!firstResponse.ok) {
				throw new Error("Failed to load employees.");
			}

			const firstResult = await firstResponse.json();
			const data = firstResult.data;

			employees = [...data.content];

			// Fetch the remaining backend pages
			for (let page = 1; page < data.totalPages; page++) {
				const response = await fetch(
					`${API_URL}/getEmployeeBypageNumber/${page}`
				);

				if (!response.ok) {
					throw new Error("Failed to load employees.");
				}

				const result = await response.json();
				employees.push(...result.data.content);
			}

			// Search across all employees by name or email
			employees = employees.filter(employee =>
				(employee.name ?? "").toLowerCase().includes(query) ||
				(employee.email ?? "").toLowerCase().includes(query)
			);

			if (sortAttribute !== "") {
				employees.sort((a, b) => {
					let valueA = a[sortAttribute];
					let valueB = b[sortAttribute];

					if (sortAttribute === "id") {
						valueA = Number(valueA);
						valueB = Number(valueB);
						return sortOrder === "asc"
							? valueA - valueB
							: valueB - valueA;
					}

					valueA = String(valueA ?? "").toLowerCase();
					valueB = String(valueB ?? "").toLowerCase();

					const comparison = valueA.localeCompare(valueB);

					return sortOrder === "asc" ? comparison : -comparison;
				});
			}

			// Paginate the filtered results locally
			totalPages = Math.ceil(employees.length / 4);

			if (currentPage >= totalPages) {
				currentPage = Math.max(0, totalPages - 1);
			}

			employees = employees.slice(
				currentPage * 4,
				(currentPage + 1) * 4
			);

		} else {
			// Normal backend pagination when search is empty
			const response = await fetch(
				`${API_URL}/getEmployeeBypageNumber/${currentPage}`
			);

			if (!response.ok) {
				throw new Error("Failed to load employees.");
			}

			const result = await response.json();

			employees = result.data.content;
			totalPages = result.data.totalPages;
		}


		if (sortAttribute !== "") {
			employees.sort((a, b) => {
				const valueA = a[sortAttribute];
				const valueB = b[sortAttribute];

				let comparison;

				if (sortAttribute === "id") {
					comparison = Number(valueA) - Number(valueB);
				} else {
					comparison = String(valueA ?? "")
						.localeCompare(String(valueB ?? ""), undefined, {
							sensitivity: "base"
						});
				}

				return sortOrder === "asc" ? comparison : -comparison;
			});
		}

		renderPagination();

		tableBody.replaceChildren();

		employees.forEach(employee => {
			const row = document.createElement("tr");

			[
				employee.id,
				employee.name,
				employee.email,
				employee.phone
			].forEach(value => {
				const cell = document.createElement("td");
				cell.textContent = value ?? "";
				row.appendChild(cell);
			});

			const actions = document.createElement("td");


			const viewButton = document.createElement("button");
			viewButton.textContent = "View";
			viewButton.className = "view-btn";
			viewButton.onclick = () => viewEmployee(employee);

			actions.appendChild(viewButton);


			const editButton = document.createElement("button");
			editButton.textContent = "Edit";
			editButton.className = "action-btn";
			editButton.onclick = () => editEmployee(employee);

			const deleteButton = document.createElement("button");
			deleteButton.textContent = "Delete";
			deleteButton.className = "delete-btn";
			deleteButton.onclick = () => deleteEmployee(employee.id);

			actions.append(editButton, deleteButton);
			row.appendChild(actions);
			tableBody.appendChild(row);
		});

		message.textContent = query !== ""
			? `${employees.length} matching employees on this page.`
			: `${employees.length} employees loaded.`;

		if (query !== "" && employees.length === 0) {
			message.textContent = "No matching employees found.";
		}

	} catch (error) {
		message.textContent = error.message;
	}
}


function viewEmployee(employee) {
	document.getElementById("detailId").textContent =
		employee.id ?? "";

	document.getElementById("detailName").textContent =
		employee.name ?? "";

	document.getElementById("detailEmail").textContent =
		employee.email ?? "";

	document.getElementById("detailPhone").textContent =
		employee.phone ?? "";

	document.getElementById("employeeDetails").style.display = "block";

	document.getElementById("employeeDetails").scrollIntoView({
		behavior: "smooth",
		block: "nearest"
	});
}

document.getElementById("closeDetailsBtn").onclick = () => {
	document.getElementById("employeeDetails").style.display = "none";
};


function renderPagination() {
	const pagination = document.getElementById("pagination");
	pagination.replaceChildren();

	if (totalPages === 0) {
		return;
	}

	// Previous button
	const prevButton = document.createElement("button");
	prevButton.textContent = "‹";
	prevButton.title = "Previous page";
	prevButton.disabled = currentPage === 0;

	prevButton.onclick = () => {
		if (currentPage > 0) {
			currentPage--;
			loadEmployees();
		}
	};

	pagination.appendChild(prevButton);

	// Numbered page buttons
	for (let page = 0; page < totalPages; page++) {
		const pageButton = document.createElement("button");

		pageButton.textContent = page + 1;
		pageButton.className =
			page === currentPage ? "active" : "";

		pageButton.setAttribute(
			"aria-current",
			page === currentPage ? "page" : "false"
		);

		pageButton.onclick = () => {
			currentPage = page;
			loadEmployees();
		};

		pagination.appendChild(pageButton);
	}

	// Next button
	const nextButton = document.createElement("button");
	nextButton.textContent = "›";
	nextButton.title = "Next page";
	nextButton.disabled = currentPage >= totalPages - 1;

	nextButton.onclick = () => {
		if (currentPage < totalPages - 1) {
			currentPage++;
			loadEmployees();
		}
	};

	pagination.appendChild(nextButton);
}


function editEmployee(employee) {
	editingEmployeeId = employee.id;

	employeeForm.reset();

	// Clear old validation errors
	document.querySelectorAll(".error").forEach(error => {
		error.textContent = "";
	});

	document.querySelectorAll("#employeeForm input").forEach(input => {
		input.classList.remove("invalid");
	});

	document.getElementById("formTitle").textContent = "Edit Employee";
	document.getElementById("saveEmployeeBtn").textContent = "Update Employee";

	// Fill the form with existing employee details
	document.getElementById("employeeId").value = employee.id;
	document.getElementById("employeeName").value = employee.name;
	document.getElementById("employeeEmail").value = employee.email;
	document.getElementById("employeePhone").value = employee.phone;

	document.getElementById("employeeFormContainer").style.display = "block";
}


async function deleteEmployee(id) {
	if (!confirm("Are you sure you want to delete this employee?")) {
		return;
	}

	try {
		const response = await fetch(
			`${API_URL}/deleteEmployeeById/${id}`,
			{ method: "DELETE" }
		);

		if (!response.ok) {
			throw new Error("Failed to delete employee");
		}

		message.textContent = "Employee deleted successfully.";

		// Reload the current page, unless it became empty after deletion.
		if (currentPage > 0 && tableBody.children.length === 1) {
			currentPage--;
		}

		await loadEmployees();

	} catch (error) {
		message.textContent = error.message;
	}
}



const employeeFormContainer = document.getElementById("employeeFormContainer");
const employeeForm = document.getElementById("employeeForm");

document.getElementById("addEmployeeBtn").onclick = () => {
	editingEmployeeId = null;

	employeeForm.reset();

	document.querySelectorAll(".error").forEach(error => {
		error.textContent = "";
	});

	document.querySelectorAll("#employeeForm input").forEach(input => {
		input.classList.remove("invalid");
	});

	document.getElementById("formTitle").textContent = "Add Employee";
	document.getElementById("saveEmployeeBtn").textContent = "Save Employee";

	employeeFormContainer.style.display = "block";
};

document.getElementById("cancelFormBtn").onclick = () => {
	employeeForm.reset();
	employeeFormContainer.style.display = "none";
	editingEmployeeId = null;

	document.getElementById("formTitle").textContent = "Add Employee";
	document.getElementById("saveEmployeeBtn").textContent = "Save Employee";
};



function validateEmployeeForm() {
	const idInput = document.getElementById("employeeId");
	const nameInput = document.getElementById("employeeName");
	const emailInput = document.getElementById("employeeEmail");
	const phoneInput = document.getElementById("employeePhone");

	const errors = {
		employeeId: "",
		employeeName: "",
		employeeEmail: "",
		employeePhone: ""
	};

	const id = idInput.value.trim();
	const name = nameInput.value.trim();
	const email = emailInput.value.trim();
	const phone = phoneInput.value.trim();

	if (!/^[1-9]\d*$/.test(id)) {
		errors.employeeId = "ID must be a positive integer.";
	}

	if (name === "") {
		errors.employeeName = "Name cannot be empty.";
	}

	if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
		errors.employeeEmail = "Enter a valid email address.";
	}

	if (!/^\d{10}$/.test(phone)) {
		errors.employeePhone = "Phone must contain exactly 10 digits.";
	}

	Object.entries(errors).forEach(([field, error]) => {
		document.getElementById(field + "Error").textContent = error;

		document.getElementById(
			field === "employeeId" ? "employeeId" :
				field === "employeeName" ? "employeeName" :
					field === "employeeEmail" ? "employeeEmail" :
						"employeePhone"
		).classList.toggle("invalid", error !== "");
	});

	return Object.values(errors).every(error => error === "");
}


employeeForm.addEventListener("submit", async (event) => {
	event.preventDefault();
	if (!validateEmployeeForm()) {
		return;
	}
	const employee = {
		id: Number(document.getElementById("employeeId").value),
		name: document.getElementById("employeeName").value.trim(),
		email: document.getElementById("employeeEmail").value.trim(),
		phone: Number(document.getElementById("employeePhone").value)
	};

	const isEditing = editingEmployeeId !== null;

	const url = isEditing
		? `${API_URL}/updateEmployeeById/${editingEmployeeId}`
		: `${API_URL}/saveEmployee`;

	try {
		const response = await fetch(url, {
			method: isEditing ? "PUT" : "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(employee)
		});

		if (!response.ok) {
			const errorText = await response.text();
			throw new Error(errorText || "Operation failed.");
		}

		alert(isEditing
			? "Employee updated successfully!"
			: "Employee added successfully!");

		employeeForm.reset();
		employeeFormContainer.style.display = "none";
		editingEmployeeId = null;

		document.getElementById("formTitle").textContent = "Add Employee";
		document.getElementById("saveEmployeeBtn").textContent = "Save Employee";

		currentPage = 0;
		await loadEmployees();

	} catch (error) {
		message.textContent = error.message;
	}
});


document.getElementById("searchBtn").onclick = () => {
	currentPage = 0;
	loadEmployees();
};

document.getElementById("clearSearchBtn").onclick = () => {
	document.getElementById("searchInput").value = "";
	currentPage = 0;
	loadEmployees();
};

document.getElementById("sortBtn").onclick = () => {
	alert("Sort button clicked!");
	sortAttribute = document.getElementById("sortAttribute").value;
	sortOrder = document.getElementById("sortOrder").value;

	currentPage = 0;
	loadEmployees();
};


loadEmployees();
