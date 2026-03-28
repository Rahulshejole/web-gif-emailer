package com.mycompany.gifemailer;

import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * AuthServlet: Step-1 login (Gmail + App Password) then sends OTP to the same email.
 */
@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Gmail App Password

        HttpSession session = request.getSession();
        session.setAttribute("userEmail", email);
        session.setAttribute("userPass", password);

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));
        session.setAttribute("loginOtp", otp);
        session.setAttribute("otpExpiry", System.currentTimeMillis() + 2 * 60 * 1000); // 2 minutes
        session.setAttribute("otpVerified", false);

        // Send OTP to email
        try {
            EmailService emailService = new EmailService();
            emailService.sendOtpEmail(email, password, email, otp);
            response.sendRedirect("otp.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "OTP send failed: " + e.getMessage()
                    + "<br><small>Make sure you entered a valid Gmail App Password.</small>");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}