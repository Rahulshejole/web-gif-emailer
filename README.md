# web-gif-emailer

## 🚀 Project Overview

This project is a **Java Web Application** that allows users to:

* Manage contact lists using Excel files
* Send emails with **GIF templates**
* Use **OTP-based email verification**
* Run on **Apache Tomcat Server**

It is built using:

* Java (Servlets, JSP)
* Apache Tomcat
* Apache POI (for Excel handling)
* HTML, CSS (Frontend)

---

## 🛠️ Features

* 📋 Manage Contact List (via Excel upload)
* 📧 Send Emails with GIF templates
* 🔐 OTP Verification system
* 📊 Read email data from Excel file
* 🌐 Web-based dashboard

---

## 📂 Project Structure

```
web-gif-emailer/
│── src/
│   ├── com.mycompany.gifemailer/
│   │   ├── EmailManagerServlet.java
│   │   ├── ExcelManager.java
│   │   └── ...
│
│── WebContent/
│   ├── index.jsp
│   ├── dashboard.jsp
│   ├── css/
│   ├── js/
│
│── WEB-INF/
│   ├── web.xml
│
│── contacts.xlsx
```

---

## ⚙️ Requirements

* Java JDK 8 or higher
* Eclipse IDE (2025 recommended)
* Apache Tomcat 9
* Internet connection (for email sending)

---

## 🔧 Setup Instructions

### 1️⃣ Clone / Download Project

* Download ZIP and extract it
* Open Eclipse

---

### 2️⃣ Import Project in Eclipse

* File → Import
* Select **Existing Projects into Workspace**
* Choose project folder
* Click **Finish**

---

### 3️⃣ Configure Server

* Install Apache Tomcat
* In Eclipse:

  * Go to **Servers**
  * Add Tomcat Server
  * Add project to server

---

### 4️⃣ Run Project

* Right click project → Run As → Run on Server
* Open browser:

```
http://localhost:8081/web-gif-emailer
```

---

## 📊 Excel File Format (IMPORTANT ⚠️)

The project reads contacts from an Excel file.

✔ Correct format:

* File must be `.xlsx`
* Example:

| Email                                   |
| --------------------------------------- |
| [test@gmail.com](mailto:test@gmail.com) |
| [demo@gmail.com](mailto:demo@gmail.com) |

❌ Do NOT use:

* `.xls`
* corrupted file
* renamed text file

---

## ❗ Common Errors & Fix

### 🔴 HTTP 500 Error (Excel issue)

**Error:**

```
Not a valid OOXML file
```

**Solution:**

* Use proper `.xlsx` file
* Recreate Excel file in Microsoft Excel
* Check file path in code

---

### 🔴 File Not Found

* Check correct file path in `ExcelManager.java`

---

### 🔴 Server Not Starting

* Ensure Tomcat configured properly
* Check port (8080/8081)

---

## 📸 Screens

* Dashboard UI
* Contact List Manager
* Email sending interface

---

## 🧠 Technologies Used

* Java Servlets
* JSP
* Apache POI
* Apache Tomcat
* HTML/CSS

---

## 👨‍💻 Author

Rahul Shejole

---

## 📌 Notes

* Make sure Excel file is valid before running
* Restart server after changes
* Check console logs for errors

---

## ⭐ Future Improvements

* Add database (MySQL)
* UI enhancements
* Bulk email scheduling
* Better error handling

---
