package com.mycompany.gifemailer;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CampaignServlet")
public class CampaignServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1. Retrieve Data from Session (Saved earlier by PreviewServlet)
        String senderEmail = (String) session.getAttribute("userEmail");
        String senderPass = (String) session.getAttribute("userPass");   // ✅ kept only once
        String emailSubject = (String) session.getAttribute("emailSubject");
        String emailBody = (String) session.getAttribute("emailBody");
        String excelPath = (String) session.getAttribute("excelPath"); // Might be null if they rely on default
        List<String> imagePaths = (List<String>) session.getAttribute("imagePaths");

        // 2. Get User's Chosen Style (Sent from select_style.jsp)
        String style = request.getParameter("chosenStyle");

        // Basic Validation: If session expired or data missing, go back to start
        if (senderEmail == null || imagePaths == null || style == null) {
            request.setAttribute("message", "Session expired or invalid data. Please start over.");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        try {
            // 3. Generate Final High-Quality GIF
            File firstImg = new File(imagePaths.get(0));
            String saveDir = firstImg.getParent();
            String gifOutput = saveDir + File.separator
                    + "final_campaign_" + System.currentTimeMillis() + ".gif";

            GifGenerator gifGen = new GifGenerator();
            gifGen.createGifFromImages(
                    imagePaths.toArray(new String[0]),
                    gifOutput,
                    500,
                    true,
                    style,
                    false
            );

            // 4. Determine Contact List Source
            if (excelPath == null) {
                String appPath = request.getServletContext().getRealPath("");
                String defaultPath = appPath + "uploads" + File.separator + "contacts.xlsx";
                File defaultFile = new File(defaultPath);
                if (defaultFile.exists()) {
                    excelPath = defaultPath;
                }
            }

            if (excelPath == null || !new File(excelPath).exists()) {
                throw new Exception("No contact list found! Please upload an Excel file or use 'Manage Contact List'.");
            }

            // 5. Read Emails
            List<String> recipients = ExcelManager.readEmailsFromExcel(new File(excelPath));
            if (recipients.isEmpty()) {
                throw new Exception("Contact list is empty.");
            }

            // 6. Send Emails
            EmailService emailService = new EmailService();
            int count = 0;
            for (String recipient : recipients) {
                emailService.sendEmailWithEmbeddedGif(
                        senderEmail,
                        senderPass,
                        recipient,
                        gifOutput,
                        emailSubject,
                        emailBody
                );
                count++;
            }

            request.setAttribute(
                    "message",
                    "Campaign Success! Sent to " + count
                            + " recipients using style: <b>" + style + "</b>"
            );

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error: " + e.getMessage());
        }

        // Return to dashboard with success/error message
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
