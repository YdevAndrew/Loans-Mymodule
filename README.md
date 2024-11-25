# Loan Module

This module provides loan functionalities for the banking system, allowing customers to apply for loans, manage their loan applications, and make payments.

## Minimum Requirements

* **Operating System:** Windows 10
* **RAM:** 2GB
* **Storage:** 20GB
* **Application Type:** Desktop application
* **Java:** Version 17.0.2 
* **Maven:** Installed (any version)

## Download

* **JAR file:** [https://drive.google.com/file/d/1GimmCj6sJ_eD-bK3D751-laXm7gKQwnU/view?usp=drive_link]
* **Pipeline artifacts:** [https://gitlab.com/jala-university1/cohort-3/oficial-pt-desenvolvimento-de-software-2-iso-124.ga.t2.24.m2/se-o-b/Capstone/loans-module/-/jobs/8457934229/artifacts/browse]

## Documentation

* **BRD:** [https://1drv.ms/w/c/023ff02a594f9011/ET9s4zx0oupEnAweAzrd6p8BsqoddpTsqXR7pPvPzFzMLg?e=G8Nbek]
* **Wiki:** [https://gitlab.com/jala-university1/cohort-3/oficial-pt-desenvolvimento-de-software-2-iso-124.ga.t2.24.m2/se-o-b/Capstone/loans-module/-/wikis/Loans]

## Functionalities

### Loan Application

* Access the loan page from the central page.
* View loan conditions offered by the bank.
* Apply for a loan by clicking the "Apply for Loan" button.
* Enter financial details (income and proof of income) in the application form.
* Validate income input as a positive number.
* Upload proof of income in JPEG, PNG, or PDF format.
* Validate all required fields.
* Receive confirmation upon successful form submission.
* Get redirected to the payment plan page if approved.
* Receive a pop-up informing loan rejection if not approved.
* Receive clear error messages with instructions in case of form submission failure.

### Loan Management

* View a list of loans on the "My Loans" page with details:
    * Loan status (Under review, Approved, Finalized, Rejected)
    * Date of request
    * Loan amount
    * Total interest
    * Total loan amount (loan amount + total interest)
    * Installments paid and total installments (e.g., 1/12)
    * Debit balance
    * Due date
    * Payment method (bank slip or automatic debit)
* View a "Next Payment" card for loans with an "Approved" status.
* Display zero or blank outstanding balance for "Finalized" loans.
* Update installment and debit balance information in real time.

### Loan Payment

* Deposit borrowed amount into the account upon loan approval.
* Manually pay installments for approved loans.
* Decrease account balance accordingly when paying installments manually.
* Prevent installment payment and display a warning if the account balance is insufficient.
* Schedule automatic payment of installments on their due dates.

## Non-Functional Requirements

* Pages should follow the provided wireframe for visual consistency and user experience.
* The "Apply for Loan" button must be clearly visible and easily accessible.
* The loan page should load efficiently without significant delays.
* Redirection after clicking the button must be secure, protecting user data.
* Data validation should be clear, with informative error messages.
* Uploading proof of income should be simple and intuitive.
* Data submission and validation should be quick and efficient.
* Financial data and proof of income must be handled securely, protecting user privacy.
* Confirmation and error messages should be clear, prominent, and detailed.

## Setup

This module requires a PostgreSQL database (latest version) to be installed and configured. You can use any IDE of your choice for development.

