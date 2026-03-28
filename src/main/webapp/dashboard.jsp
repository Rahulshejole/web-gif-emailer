
<%
    Boolean verified = (Boolean) session.getAttribute("otpVerified");
    if (verified == null || !verified) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<%@ page import="java.util.Properties" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - GIF Emailer</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 20px; background: #f8f9fa; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 40px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        h1 { color: #333; text-align: center; margin-bottom: 10px; }
        .welcome-msg { text-align: center; color: #666; margin-bottom: 30px; }
        
        .section { margin-bottom: 30px; background: #f8f9fa; padding: 20px; border-radius: 8px; border: 1px solid #e9ecef; }
        h3 { margin-top: 0; color: #495057; font-size: 1.1em; }
        
        label { font-weight: 600; display: block; margin-top: 15px; margin-bottom: 5px; color: #333; }
        input[type="file"] { width: 100%; padding: 10px; background: white; border: 1px solid #ced4da; border-radius: 4px; }
        
        .btn-send { background: #28a745; color: white; padding: 15px 30px; border: none; border-radius: 6px; font-size: 18px; font-weight: bold; cursor: pointer; width: 100%; transition: background 0.2s; }
        .btn-send:hover { background: #218838; }
        
        .btn-manage { background: #17a2b8; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 15px; display: inline-block; margin-bottom: 20px; }
        .btn-manage:hover { background: #138496; }
        
        .logout { float: right; color: #dc3545; text-decoration: none; font-weight: 500; }
        .logout:hover { text-decoration: underline; }

        .helper-text { font-size: 0.85em; color: #6c757d; margin-top: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <a href="index.jsp" class="logout">Logout</a>
        <h1>Campaign Dashboard</h1>
        <p class="welcome-msg">Logged in as: <b><%= session.getAttribute("userEmail") %></b></p>
        
        <a href="EmailManagerServlet" class="btn-manage">Manage Contact List</a>
        
        <!-- IMPORTANT: Action points to PreviewServlet now -->
        <form action="PreviewServlet" method="post" enctype="multipart/form-data">
            
            <div class="section">
                <h3>Step 1: Contact List</h3>
                <p class="helper-text">If you have already added emails via "Manage Contact List", you can skip this.</p>
                <label>Upload New Excel File (.xlsx):</label>
                <input type="file" name="excelFile" accept=".xlsx">
            </div>

            <div class="section">
                <h3>Step 2: Creative Assets</h3>
                <label>Select Images for Animation:</label>
                <input type="file" name="imageFiles" accept="image/*" multiple required>
                <p class="helper-text">Hold <strong>Ctrl</strong> (Windows) or <strong>Cmd</strong> (Mac) to select multiple images.</p>
            </div>


            <div class="section">
                <h3>Step 3: Email Content</h3>
	                <label>Select Email Template:</label>
	                <select id="templateSelect" onchange="loadTemplate()" style="width:100%; padding:10px; border:1px solid #ced4da; border-radius:4px;">
	                    <option value="">-- Select Template --</option>
	                    <option value="welcome">Welcome Email</option>
	                    <option value="offer">Special Offer</option>
	                    <option value="event">Event Invitation</option>
	                    <option value="thankyou">Thank You</option>
	                </select>

	                <!-- ✅ Template Preview Box -->
	                <div id="templatePreviewBox" style="display:none; margin-top:15px; padding:15px; border:1px solid #ccc; border-radius:10px; background:#f9f9f9;">
	                    <h4 style="margin:0 0 10px 0;">Template Preview</h4>
	                    <p style="margin:0 0 8px 0;"><b>Subject:</b> <span id="previewSubject"></span></p>
	                    <p style="margin:0 0 6px 0;"><b>Message:</b></p>
	                    <div id="previewBody" style="white-space:pre-wrap; padding:10px; border:1px solid #ddd; border-radius:8px; background:white;"></div>
	                </div>
                <label>Email Subject:</label>
	                <input type="text" id="emailSubject" name="emailSubject" required placeholder="e.g., New Year Offer 🎉">

                <label>Email Message:</label>
	                <textarea id="emailBody" name="emailBody" rows="6" style="width:100%; padding:10px; border:1px solid #ced4da; border-radius:4px; resize:vertical;" required
                          placeholder="Write your email message here..."></textarea>
                <p class="helper-text">Your selected GIF will be embedded below this message.</p>

	                <script>
	                    const templates = {
	                        welcome: {
	                            subject: "Welcome to Our Service 🎉",
	                            body: "Hi,\n\nWelcome to our service! We are excited to have you.\n\nRegards,\nTeam"
	                        },
	                        offer: {
	                            subject: "Special Offer Just for You 🔥",
	                            body: "Hi,\n\nWe have a special offer available for you today.\n\nGrab it before it expires!\n\nThanks,\nTeam"
	                        },
	                        event: {
	                            subject: "You're Invited to Our Event 📅",
	                            body: "Hi,\n\nYou are invited to our upcoming event.\n\nDate: \nTime: \nLocation: \n\nRegards,\nTeam"
	                        },
	                        thankyou: {
	                            subject: "Thank You 🙏",
	                            body: "Hi,\n\nThank you for your time and support.\n\nRegards,\nTeam"
	                        }
	                    };

	                    function loadTemplate() {
	                        const selected = document.getElementById("templateSelect").value;
	                        const previewBox = document.getElementById("templatePreviewBox");
	                        const previewSubject = document.getElementById("previewSubject");
	                        const previewBody = document.getElementById("previewBody");

	                        if (selected && templates[selected]) {
	                            document.getElementById("emailSubject").value = templates[selected].subject;
	                            document.getElementById("emailBody").value = templates[selected].body;

	                            previewBox.style.display = "block";
	                            previewSubject.innerText = templates[selected].subject;
	                            previewBody.innerText = templates[selected].body;
	                        } else {
	                            previewBox.style.display = "none";
	                        }
	                    }

	                    // ✅ Live update preview while editing
	                    document.addEventListener("DOMContentLoaded", function () {
	                        const subj = document.getElementById("emailSubject");
	                        const body = document.getElementById("emailBody");
	                        const previewSubject = document.getElementById("previewSubject");
	                        const previewBody = document.getElementById("previewBody");
	                        const previewBox = document.getElementById("templatePreviewBox");

	                        subj.addEventListener("input", function () {
	                            if (subj.value.trim() !== "" || body.value.trim() !== "") previewBox.style.display = "block";
	                            previewSubject.innerText = subj.value;
	                        });
	                        body.addEventListener("input", function () {
	                            if (subj.value.trim() !== "" || body.value.trim() !== "") previewBox.style.display = "block";
	                            previewBody.innerText = body.value;
	                        });
	                    });
	                </script>
            </div>

            </div>

            <div class="section" style="background: transparent; border: none; padding: 0;">
                <button type="submit" class="btn-send"> Generate Previews & Select Style</button>
            </div>
            
        </form>
        
        <!-- Status Message Block -->
        <% if(request.getAttribute("message") != null) { %>
            <div style="margin-top: 20px; padding: 15px; background: #d4edda; color: #155724; border-radius: 5px; border: 1px solid #c3e6cb;">
                <strong>Status:</strong> <%= request.getAttribute("message") %>
            </div>
        <% } %>
    </div>
</body>
</html>