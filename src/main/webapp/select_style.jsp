<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Select Style - GIF Emailer</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: #f0f2f5; padding: 20px; }
        h1 { text-align: center; color: #333; }
        .grid-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }
        .card {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: transform 0.2s;
            text-align: center;
            padding-bottom: 15px;
        }
        .card:hover { transform: translateY(-5px); box-shadow: 0 10px 15px rgba(0,0,0,0.2); }
        .card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-bottom: 1px solid #eee;
        }
        .card h3 { margin: 10px 0 5px; text-transform: capitalize; font-size: 18px; }
        .btn-select {
            background: #28a745; color: white; border: none; padding: 8px 20px;
            border-radius: 20px; cursor: pointer; font-weight: bold;
        }
        .btn-select:hover { background: #218838; }
    </style>
</head>
<body>
    <h1>Select Animation Style</h1>
    <p style="text-align:center; color:#666;">Click "Select" on the version you like best to send your campaign.</p>

    <div class="grid-container">
        <% 
            String[] styles = (String[]) request.getAttribute("styles");
            List<String> urls = (List<String>) request.getAttribute("previewUrls");
            
            if (styles != null && urls != null) {
                for(int i=0; i<styles.length; i++) {
        %>
            <div class="card">
                <img src="<%= urls.get(i) %>" alt="<%= styles[i] %>">
                <h3><%= styles[i] %></h3>
                
                <form action="CampaignServlet" method="post">
                    <!-- Pass the chosen style to the backend -->
                    <input type="hidden" name="chosenStyle" value="<%= styles[i] %>">
                    <button type="submit" class="btn-select">Select & Send</button>
                </form>
            </div>
        <%      } 
            } else { %>
            <p>No previews generated. Please go back and upload images.</p>
        <% } %>
    </div>
</body>
</html>