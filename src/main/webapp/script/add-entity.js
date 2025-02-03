document.addEventListener("DOMContentLoaded", function() {
    // Get the current date
    let currentDate = new Date();
    currentDate = currentDate.toISOString().split('T')[0];
    console.log("currentDate : ",currentDate);

    // Set the maximum attribute of the start date input to the current date
    let startDate = document.getElementById('startDate');
    if(startDate){
        startDate.setAttribute('max', currentDate);
        startDate.value = currentDate;
        console.log("startDateValue : "+startDate.value)
    }
});

// CONNECTION
// function to validate consumer no and set name,mobNumber, emailID and address based on consumerNum
function validateConsumerNo() {
    let consumerNum = document.getElementById("consumerNum").value;
    console.log("Inside validateConsumerNo => consumerNum : ", consumerNum);
    if (!consumerNum || consumerNum.length < 8) {
        alert("Invalid consumer number!");
        let inputElements = document.getElementsByTagName("input");
        for (let i = 0; i < inputElements.length; i++) {
            inputElements[i].value = "";
        }
        return;
    }

    let regex = /^\d+$/;
    if (!regex.test(consumerNum)) {
        alert("Invalid consumer no!");
        return;
    }

    fetch("consumer/" + consumerNum)
    .then(response => {
        if (!response.ok) {
            throw new Error("HTTP error! Status: "+ response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log("Data from server:", data);

        if(JSON.stringify(data) !== '{}'){
        let name = document.getElementsByName("fullName")[0];
        let mob = document.getElementsByName("mobNumber")[0];
        let address = document.getElementsByName("address")[0];
		let email = document.getElementsByName("emailId")[0];

        name.value = data.fullName;
        name.readOnly=true;
        mob.value = data.mobNumber;
        mob.readOnly=true;
        address.value = data.address;
        address.readOnly=true;
        email.value = data.emailId;
        email.readOnly=true;

        } else{
            alert("Invalid consumer number!")
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });

}

// function to validate meter number
function validateMeterNo() {
	let meterNo = document.getElementById("meterNum").value;
	console.log("Inside validateMeterNo => meterNum : ", meterNo);

	if (!meterNo || meterNo.trim().length == 0) {
	     alert("Meter number can not be empty!");
	     return;
	}

	 if (meterNo && meterNo.trim().length < 8) {
	     alert("Invalid meter no!");
	     return;
	 }

	 let alphabetRegex = /^[a-zA-Z]+$/;
	 let numericalRegex = /^\d+$/;
	 let firstChar = meterNo.charAt(0);
	 console.log("firstChar : ", firstChar);
	 let remainingChars = meterNo.substring(1);
	 console.log("remainingChars : ", remainingChars);

	 if (!(alphabetRegex.test(firstChar) && numericalRegex.test(remainingChars))) {
	     meterNoAlertShown = true;
	     alert("Invalid meter no!");
	 } else {
	     document.getElementById("addConnectionForm").submit();
	 }
}


// BILL
function setMeterNumAndPrevBal(){
	let consumerNum = document.getElementById("consumerNum").value;
	let meterNum = document.getElementsByName("meterNum")[0];
    let previousBal = document.getElementsByName("previousBalance")[0];
    let previousRead = document.getElementsByName("previousReading")[0];

    console.log("Inside setMeterNumAndPrevBal => consumerNum : ", consumerNum);
    if (!consumerNum || consumerNum.length < 8) {
        alert("Invalid consumer number!");
        let inputElements = document.getElementsByTagName("input");
        for (let i = 0; i < inputElements.length; i++) {
            inputElements[i].value = "";
        }
        return;
    }

    let regex = /^\d+$/;
    if (!regex.test(consumerNum)) {
        alert("Invalid consumer number!");
        return;
    }

    fetch("previous-bill/" + consumerNum)
        .then(response => {
            if (!response.ok) {
                throw new Error('HTTP error! Status: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log("Data from server:", data);

            // Check if data is not empty and status is not 'fail'
            if (Object.keys(data).length !== 0 && data.status !== 'fail') {
                meterNum.value = '';
                previousBal.value = 0;
                previousRead.value = 0;

                if (data.found === 'Bill') {
                    meterNum.value = data.bill.meterNum;
                    previousRead.value = data.bill.currentReading;
                     console.log("setMeterNumAndPrevBal >> previousRead :: ",previousRead);
                    console.log("setMeterNumAndPrevBal >> prev bill status :: ",data.bill.status);
                    if(data.bill.status !== 'Paid'){
                        previousBal.value = data.bill.totalAmount;
                    }
                } else if (data.found === 'Connection') {
                    meterNum.value = data.connection.meterNum;
                }
            } else {
                meterNum.value = '';
                previousBal.value = '';
                previousRead.value = '';
                document.getElementById("currentReading").value = '';
                document.getElementById("totalUnits").value = '';
                document.getElementById("currentAmount").value = '';
                document.getElementById("totalAmount").value = '';
                document.getElementById("totalUnits").value = '';
                alert(data.message);
            }
        })
        .catch(error => {
            console.error(error);
            alert("Error fetching data. Please try again.");
            meterNum.value = '';
            previousBal.value = 0;
        });

}


// Function to calculate bill amount
function calculateBillAmount() {
	let costMap = window.costMap;

	// Accessing values from the costMap
	let unitsZeroToHundred = costMap.get('unitsZeroToHundred');
	let unitsOneHundredOneToThreeHundred = costMap.get('unitsOneHundredOneToThreeHundred');
	let unitsThreeHundredOneToFiveHundred = costMap.get('unitsThreeHundredOneToFiveHundred');
	let unitsFiveHundredOneAndAbove = costMap.get('unitsFiveHundredOneAndAbove');

	console.log("Cost : [" + unitsZeroToHundred + "],[" + unitsOneHundredOneToThreeHundred + "]," +
		"[" + unitsThreeHundredOneToFiveHundred + "],[" + unitsFiveHundredOneAndAbove + "]");

	var curReadingElement = document.getElementsByName('currentReading')[0];
    var curReading =  curReadingElement ? Number(curReadingElement.value) : 0;
    var prevReadingElement = document.getElementsByName('previousReading')[0];
    var prevReading =  prevReadingElement ? Number(prevReadingElement.value) : 0;

	if(curReading < prevReading){
		alert("Current reading cannot be less than Previous reading");
		return;
	}

	// Calculate the total units
	var units = '';
	if( curReadingElement.value && prevReadingElement.value && curReading != prevReading && curReading > prevReading){
		var units = curReading - prevReading;
	}

	// Calculate the bill amount
	var billAmount = 0;
	var finalAmount = 0;
	var tempUnits = units;

	while (tempUnits > 0) {
		if (tempUnits <= 100) {
			billAmount += tempUnits * unitsZeroToHundred;
			tempUnits = 0;
		} else if (tempUnits <= 300) {
			billAmount += 100 * unitsZeroToHundred + (tempUnits - 100) * unitsOneHundredOneToThreeHundred;
			tempUnits = 0;
		} else if (tempUnits <= 500) {
			billAmount += 100 * unitsZeroToHundred + 200 * unitsOneHundredOneToThreeHundred + (tempUnits - 300) * unitsThreeHundredOneToFiveHundred;
			tempUnits = 0;
		} else {
			billAmount += 100 * unitsZeroToHundred + 200 * unitsOneHundredOneToThreeHundred + 200 * unitsThreeHundredOneToFiveHundred + (tempUnits - 500) * unitsFiveHundredOneAndAbove;
			tempUnits = 0;
		}
	}
	console.log("current amount : ", billAmount);

	let prevBalElement = document.getElementsByName('previousBalance')[0];
	let prevBalance =  prevBalElement ? Number(prevBalElement.value) : 0;
	if (prevBalance) {
		console.log("prev bal : ", prevBalance);
		finalAmount = billAmount + prevBalance;
	} else {
		finalAmount = billAmount;
	}
	console.log("finalAmount : ", finalAmount);
	console.log("units : ",units);
	document.getElementById('totalUnits').value = units;//.toFixed(0);
	document.getElementById('currentAmount').value = billAmount.toFixed(2);
	document.getElementById('totalAmount').value = finalAmount.toFixed(2);
}


// CONSUMER
function valRegisterFormData() {
    let fullName = document.getElementById("fullName").value;
    let emailId = document.getElementById("emailId").value;
    let mobNumber = document.getElementById("mobNumber").value;
    console.log("fullName: " + fullName + ", emailId: " + emailId + ", mobNumber: " + mobNumber);

    let regex = /^\d+$/;
    if (!regex.test(consumerNum)) {
        alert("Invalid consumer no!");
        return;
    }

    fetch("consumer/" + consumerNum)
    .then(response => {
        if (!response.ok) {
            throw new Error("HTTP error! Status: "+ response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log("Data from server:", data);

        if(JSON.stringify(data) !== '{}'){
        let name = document.getElementsByName("fullName")[0];
        let mob = document.getElementsByName("mobNumber")[0];
        let address = document.getElementsByName("address")[0];
		let email = document.getElementsByName("emailId")[0];

        name.value = data.fullName;
        name.readOnly=true;
        mob.value = data.mobNumber;
        mob.readOnly=true;
        address.value = data.address;
        address.readOnly=true;
        email.value = data.emailId;
        email.readOnly=true;

        } else{
            alert("Invalid consumer number!")
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });

}