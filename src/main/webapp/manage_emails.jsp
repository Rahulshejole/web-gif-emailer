<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Contacts - GIF Emailer</title>
    <style>
        body { font-family: sans-serif; padding: 20px; background: #f8f9fa; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { text-align: left; padding: 12px; border-bottom: 1px solid #ddd; }
        th { background-color: #f2f2f2; }
        .form-inline { display: flex; gap: 10px; margin-bottom: 20px; background: #e9ecef; padding: 15px; border-radius: 5px; }
        input[type="email"] { flex-grow: 1; padding: 10px; border: 1px solid #ccc; border-radius: 4px; }
        .btn-add { background: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; }
        .btn-back { display: inline-block; margin-bottom: 20px; color: #666; text-decoration: none; }
    </style>
</head>
<body>
    <div class="container">
        <a href="dashboard.jsp" class="btn-back"> Back to Dashboard</a>
        <h1>Manage Contact List</h1>

        <!-- Add Email Form -->
        <form action="EmailManagerServlet" method="post" class="form-inline">
            <input type="email" name="newEmail" placeholder="Enter new email address..." required>
            <button type="submit" class="btn-add">Add Email</button>
        </form>

        <h3>Current List</h3>
        <% 
            List<String> emails = (List<String>) request.getAttribute("emailList");
            if (emails == null || emails.isEmpty()) { 
        %>
            <p style="color: #666; font-style: italic;">No emails found. Add one above!</p>
        <% } else { %>
            <table>
                <thead>
                    <tr>
                        <th width="50">#</th>
                        <th>Email Address</th>
                        <th width="100">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for(int i=0; i<emails.size(); i++) { %>
                    <tr>
                        <td><%= i + 1 %></td>
                        <td><%= emails.get(i) %></td>
                        <td>
                            <form action="EmailManagerServlet" method="post" style="display:inline;">
                                <input type="hidden" name="removeEmail" value="<%= emails.get(i) %>">
                                <button type="submit"
                                        style="background:#dc3545;color:white;border:none;
                                               padding:6px 12px;border-radius:4px;cursor:pointer;">
                                    Remove
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
    </div>
</body>
</html>
