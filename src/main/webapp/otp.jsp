<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>OTP Verification - GIF Emailer</title>
    <style>
        body { font-family: sans-serif; display:flex; justify-content:center; align-items:center; height:100vh; background:#f0f2f5; }
        .card { background:white; padding:40px; border-radius:8px; box-shadow:0 4px 12px rgba(0,0,0,0.1); width:320px; }
        input { width:100%; padding:10px; margin:10px 0; box-sizing:border-box; }
        button { width:100%; padding:10px; background:#28a745; color:white; border:none; border-radius:4px; cursor:pointer; }
        button:hover { background:#1f8a38; }
        .msg { margin-top:10px; }
    </style>
</head>
<body>
<div class="card">
    <h2 style="text-align:center">OTP Verification</h2>
    <p style="text-align:center; font-size:14px; color:#555;">
        We sent a 6-digit OTP to your Gmail.
    </p>

    <form action="VerifyOtpServlet" method="post">
        <label>Enter OTP:</label>
        <input type="text" name="otp" maxlength="6" pattern="\d{6}" required placeholder="123456">
        <button type="submit">Verify OTP</button>
    </form>

    <div class="msg">
        <% if (request.getAttribute("error") != null) { %>
            <p style="color:red;"><%= request.getAttribute("error") %></p>
        <% } %>
    </div>

    <p style="text-align:center; margin-top:15px;">
        <a href="index.jsp">Back to Login</a>
    </p>
</div>
</body>
</html>