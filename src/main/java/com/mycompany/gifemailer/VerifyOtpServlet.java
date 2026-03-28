package com.mycompany.gifemailer;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/VerifyOtpServlet")
public class VerifyOtpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginOtp") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String enteredOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("loginOtp");
        long expiry = (long) session.getAttribute("otpExpiry");

        if (System.currentTimeMillis() > expiry) {
            session.removeAttribute("loginOtp");
            request.setAttribute("error", "OTP expired. Please login again.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        if (sessionOtp != null && sessionOtp.equals(enteredOtp)) {
            session.setAttribute("otpVerified", true);
            session.removeAttribute("loginOtp");
            session.removeAttribute("otpExpiry");
            response.sendRedirect("dashboard.jsp");
        } else {
            request.setAttribute("error", "Invalid OTP. Try again.");
            request.getRequestDispatcher("otp.jsp").forward(request, response);
        }
    }
}