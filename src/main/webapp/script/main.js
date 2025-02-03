// Function to show the popup
function showPopup() {
	document.getElementById("popup").style.display = "flex";
}

// Function to hide the popup
function hidePopup() {
	document.getElementById("popup").style.display = "none";
}

// Function to handle dropdown clicks
function dropdownClicked(targetId) {
    console.log("Dropdown clicked for : " + targetId);
    let display = document.getElementById(targetId).style.display;
    console.log("display:: ",display);
    // Hide all dropdown contents
    hideAllDropdowns();
    // Display the clicked dropdown content
    if(display == 'block'){
        document.getElementById(targetId).style.display = "none";
    }else{
        document.getElementById(targetId).style.display = "block";
    }

}

// Function to hide all dropdown contents
function hideAllDropdowns() {
    var dropdownContents = document.getElementsByClassName("dropdown-content");
    for (var i = 0; i < dropdownContents.length; i++) {
        dropdownContents[i].style.display = "none";
    }
}

//function to show password
function showEnteredPassword() {
    var passwordField = document.getElementsByName("password")[0];
    var showPasswordCheckbox = document.getElementById("showPassword");

    console.log("isChecked : ",showPasswordCheckbox.checked);

    if (showPasswordCheckbox.checked) {
      passwordField.type = "text";
    } else {
      passwordField.type = "password";
    }
  }

// function to validate password
function validatePassword(input) {
    var password = input.value;
    if (!password || password.length < 8) {
        alert("Password must be at least 8 characters long");
        return false;
    } else {
        var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/;
        if (!regex.test(password)) {
            alert("Password must contain at least one capital letter, one small letter, one number and one special character");
            return false;
        }
    }
    return true;
}

// Function to validate email id
function validateEmail(email) {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$/;
    if (!email || email.trim() === "") {
        alert("Email id required!");
        return false;
    } /*else if(!regex.test(email)){
        alert("Invalid email id!");
		return false;
    }*/
    return true;
}

// Function to max length for number input
function checkInputLength(input) {
    if (input.value.length > input.maxLength) {
        input.value = input.value.slice(0, input.maxLength);
    }
}

// Function to allow only 2 digit after decimal
function checkInput(input) {
	if (input.value.length > input.maxLength) {
		input.value = input.value.slice(0, input.maxLength);
		input.value = parseFloat(input.value).toFixed(2);
	}
}


// get minimum date for date field
function getMinDate() {
    var today = new Date();
    var yyyy = today.getFullYear();
    var mm = String(today.getMonth() + 1).padStart(2, '0');
    var dd = String(today.getDate()).padStart(2, '0');
    let minDate = yyyy + '-' + mm + '-' + dd;
    console.log("getMinDate >> date :: ",minDate);
    return minDate;
}

// Function to check if all form fields are filled
function checkFormFields() {
	console.log("inside checkFormFields..");
    const form = document.getElementById('myForm');
    const submitButton = document.getElementById('submitButton');

    const allFieldsFilled = Array.from(form.elements)
            .filter(field =>
              ['input'].includes(field.tagName.toLowerCase()) &&
              field.type !== 'hidden') // Exclude buttons
            .every(field => field.value.trim() !== '');

    submitButton.disabled = !allFieldsFilled;

    if (allFieldsFilled) {
        submitButton.style.cursor = 'pointer';
    } else {
        submitButton.style.cursor = 'not-allowed';
    }
}

// function to show hidden action buttons
let currentlyVisibleRow = null;
function showActionButtons(hiddenRowId) {
    console.log("inside showActionButtons..");

    if (currentlyVisibleRow && currentlyVisibleRow.id !== hiddenRowId) {
        currentlyVisibleRow.hidden = true;
    }

    let hiddenRow = document.getElementById(hiddenRowId);
    if (hiddenRow) {
        hiddenRow.hidden = !hiddenRow.hidden;
        if (!hiddenRow.hidden) {
            currentlyVisibleRow = hiddenRow;
        } else {
            currentlyVisibleRow = null;
        }
    }
}

function toggleRows() {
	let hiddenRows = document.getElementsByClassName("hidden-row");
	let showMoreLabel = document.getElementById("view-more-label");

	for (let i = 0; i < hiddenRows.length; i++) {
		hiddenRows[i].style.display = (hiddenRows[i].style.display === "none") ? "" : "none";
	}
	// Toggle the label based on the visibility of hidden rows
	showMoreLabel.textContent = (hiddenRows[0].style.display !== "none") ? "View Less" : "View More";
}

// function to call RemoveData API
function deleteRecord(recordType, recordNo) {
    console.log(`Deleting ${recordType} record for ${recordType}No: ${recordNo}`);
    let url;
    switch (recordType) {
        case 'consumer':
            url = `remove-consumer/${recordNo}`;
            break;
        case 'bill':
            url = `remove-bill/${recordNo}`;
            break;
        case 'connection':
            url = `remove-connection/${recordNo}`;
            break;
        case 'user':
            url = `remove-user/${recordNo}`;
            break;
        default:
            alert("Something went wrong! please try again later");
            return;
    }

	let csrf = document.getElementById("csrf").value;
    console.log("deleteRecord >> csrf : ", csrf);
	    fetch(url, {
	        method: 'DELETE',
	        headers: {
	            'X-CSRF-TOKEN': csrf
	        }
	    })
        .then(response => {
            if (!response.ok) {
                throw new Error("HTTP error! Status: " + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log("deleteRecord >> Data from server:", data);
			console.log("RESULT : ",data.RESULT);
            if (Object.keys(data).length !== 0 && data.RESULT === 'success') {
                alert(`${data.MSG}`);
                location.reload(true);
            } else {
                alert(`Failed to delete the ${recordType}.`);
            }
        })
        .catch(error => {
            console.error('Error occurred: ', error);
            alert(`An error occurred while deleting the ${recordType}.`);
        });
}


// search function
function searchData(numericIdx, alphabetIdx, tableId) {
	console.log("search function is called");
	console.log("numericIdx : " + numericIdx + " & alphabetIdx : " + alphabetIdx + " & tableId : " + tableId);

	let input, type, table, tr, td, i, txtValue;
	input = document.getElementById("searchText").value;
	table = document.getElementById(tableId);
	tr = table.getElementsByTagName("tr");

	let viewMoreRow = document.getElementById("view-more-row");
	if (!input) {
		console.log("no-input")

		for (i = 0; i < tr.length; i++) {
			tr[i].style.display = "";
		}
		if(viewMoreRow)
			viewMoreRow.style.display = "";

		toggleRows()
		return;
	}
	if (/^\d+$/.test(input)) {
		type = "numeric";
		console.log("type is numeric");
	} else {
		type = "alphabetic";
		console.log("type is alphabetic");
	}

	// Loop through all table rows, and hide those that don't match the search query
	for (i = 0; i < tr.length; i++) {
		if (type === "numeric") {
			td = tr[i].getElementsByTagName("td")[numericIdx]; // index of column in table
		} else {
			td = tr[i].getElementsByTagName("td")[alphabetIdx]; // index of column in table
		}

		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(input.toUpperCase()) > -1) {
				tr[i].style.display = "";
				console.log("ViewMoreRow1 : ", viewMoreRow);
				if (viewMoreRow)
					viewMoreRow.style.display = "";
			} else {
				tr[i].style.display = "none";
				console.log("ViewMoreRow2 : ", viewMoreRow);
				if (viewMoreRow)
					viewMoreRow.style.display = "none";
			}
		}
	}
}


//function to show password
function showEnteredPasswords() {
	let pass1 = document.getElementById("password1");
	let pass2 = document.getElementById("password2");
	let oldPass = document.getElementById("oldPassword");
	var showPasswordCheckbox = document.getElementById("showPassword");
	console.log("showEnteredPassword >> isChecked : ", showPasswordCheckbox.checked);

	let userId = document.getElementById("userId");
	console.log("showEnteredPassword >> userId : ", userId);

	if (showPasswordCheckbox.checked) {
		pass1.type = "text";
		pass2.type = "text";
		if(oldPass)
			oldPass.type = "text";
	} else {
		pass1.type = "password";
		pass2.type = "password";
		if(oldPass)
			oldPass.type = "password";
	}
}