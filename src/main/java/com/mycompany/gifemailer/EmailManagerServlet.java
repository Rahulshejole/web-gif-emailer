package com.mycompany.gifemailer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/EmailManagerServlet")
public class EmailManagerServlet extends HttpServlet {

    // GET: Display the list of emails
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appPath = request.getServletContext().getRealPath("");
        String excelPath = appPath + "uploads" + File.separator + "contacts.xlsx";
        File excelFile = new File(excelPath);

        List<String> emails = new ArrayList<>();

        if (excelFile.exists()) {
            emails = ExcelManager.readEmailsFromExcel(excelFile);
        }

        request.setAttribute("emailList", emails);
        request.getRequestDispatcher("manage_emails.jsp").forward(request, response);
    }

    // POST: Add or Remove email
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newEmail = request.getParameter("newEmail");
        String removeEmail = request.getParameter("removeEmail");

        String appPath = request.getServletContext().getRealPath("");
        String saveDir = appPath + "uploads";
        new File(saveDir).mkdirs();

        String excelPath = saveDir + File.separator + "contacts.xlsx";
        File excelFile = new File(excelPath);

        // ✅ REMOVE EMAIL (added logic)
        if (removeEmail != null && !removeEmail.trim().isEmpty() && excelFile.exists()) {
            removeEmailFromExcel(excelFile, removeEmail.trim());
        }

        // ADD EMAIL (existing logic – unchanged)
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            ExcelManager.addEmailToExcel(excelFile, newEmail.trim());
        }

        response.sendRedirect("EmailManagerServlet");
    }

    // 🔹 ONLY REQUIRED helper (no ExcelManager change)
    private void removeEmailFromExcel(File excelFile, String emailToRemove) throws IOException {
        List<String> emails = ExcelManager.readEmailsFromExcel(excelFile);
        emails.removeIf(e -> e.equalsIgnoreCase(emailToRemove));

        // rewrite excel
        excelFile.delete();
        for (String email : emails) {
            ExcelManager.addEmailToExcel(excelFile, email);
        }
    }
}
