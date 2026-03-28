package com.mycompany.gifemailer;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/PreviewServlet")
@MultipartConfig
public class PreviewServlet extends HttpServlet {
    
    // The 10 Styles we defined
    private static final String[] STYLES = {
        "standard", "fast", "slow", "boomerang", "reverse", 
        "slideshow", "stutter", "flash", "blink", "chaos"
    };

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        
        // Save customizable email content in session
        session.setAttribute("emailSubject", request.getParameter("emailSubject"));
        session.setAttribute("emailBody", request.getParameter("emailBody"));
// 1. Setup Session Directory (Unique per user to avoid collisions)
        String appPath = request.getServletContext().getRealPath("");
        String sessionDirName = "session_" + session.getId();
        String savePath = appPath + "uploads" + File.separator + sessionDirName;
        new File(savePath).mkdirs();

        // 2. Save Excel File (if provided)
        Part excelPart = request.getPart("excelFile");
        if (excelPart != null && excelPart.getSize() > 0) {
            String excelPath = savePath + File.separator + "contacts.xlsx";
            excelPart.write(excelPath);
            session.setAttribute("excelPath", excelPath); // Save path for later
        }

        // 3. Save Images
        List<String> imagePaths = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals("imageFiles") && part.getSize() > 0) {
                String fileName = getFileName(part);
                String filePath = savePath + File.separator + fileName;
                part.write(filePath);
                imagePaths.add(filePath);
            }
        }
        
        if (imagePaths.isEmpty()) {
            response.sendRedirect("dashboard.jsp"); // Error: No images
            return;
        }
        
        session.setAttribute("imagePaths", imagePaths); // Save paths for later

        // 4. Generate 10 Preview GIFs
        GifGenerator gen = new GifGenerator();
        List<String> previewUrls = new ArrayList<>();

        for (String style : STYLES) {
            String filename = "preview_" + style + ".gif";
            String fullPath = savePath + File.separator + filename;
            
            // Generate Small Preview (isPreview = true)
            gen.createGifFromImages(imagePaths.toArray(new String[0]), fullPath, 500, true, style, true);
            
            // Add URL for the JSP to display
            previewUrls.add("uploads/" + sessionDirName + "/" + filename);
        }
        
        request.setAttribute("styles", STYLES);
        request.setAttribute("previewUrls", previewUrls);

        // 5. Go to Selection Page
        request.getRequestDispatcher("select_style.jsp").forward(request, response);
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "unknown_" + System.currentTimeMillis() + ".jpg";
    }
}