<!DOCTYPE html>
<html>
<head>
    <title>Login - GIF Emailer</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: #f0f2f5;
        }
        .card {
            background: white;
            padding: 45px 40px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
            width: 340px;
        }
        h2 {
            margin-bottom: 25px;
            color: #333;
        }
        label {
            font-size: 14px;
            font-weight: 600;
            color: #444;
        }
        input {
            width: 100%;
            padding: 12px;
            margin: 8px 0 18px 0;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
        }
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        button {
            width: 100%;
            padding: 12px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            transition: background 0.2s ease;
        }
        button:hover {
            background: #5563d6;
        }
    </style>
</head>
<body>
    <div class="card">
        <h2 style="text-align:center">Emailer Login</h2>
        <form action="AuthServlet" method="post">
            <label>Gmail Address:</label>
            <input type="email" name="email" required placeholder="user@gmail.com">
            
            <label>App Password:</label>
            <input type="password" name="password" required placeholder="16-char password">
            
            <button type="submit">Login</button>
        </form>
    </div>
</body>
</html>
