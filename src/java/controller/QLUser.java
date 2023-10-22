/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.UserDAO;
import Model.cipher;
import Model.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import static java.lang.System.out;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author askm4
 */
@WebServlet(name = "QLUser", urlPatterns = {"/qluser"})
public class QLUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String current_date= LocalDate.now().format(formatter);
    UserDAO u_dao = new UserDAO();
    
    protected void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute("users", u_dao.getAll());
        request.getRequestDispatcher("QLUser.jsp").forward(request, response);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String action = null;
        if ((action = request.getParameter("action")) != null) {
            if (action.equals("add")) {
                User u = new User();
                u.setEmail(request.getParameter("email"));
                u.setName(request.getParameter("name"));
                u.setPass(cipher.encode(request.getParameter("pass")));
                u.setPhone(request.getParameter("phone"));
                u.setCreated_at(current_date);
                u_dao.add(u);
            } else if (action.equals("edit")) {
                User u = new User();
                u.setEmail(request.getParameter("email"));
                u.setName(request.getParameter("name"));
                u.setPass(cipher.encode(request.getParameter("pass")));
                u.setPhone(request.getParameter("phone"));
                u.setModified_at(current_date);
                u.setU_c_id(Integer.parseInt(request.getParameter("u_c_id")));
                u.setId(Integer.parseInt(request.getParameter("id")));
                u_dao.edit(u);
            } else if (action.equals("getUser")) {
                Gson gson = new Gson();
                out.print(gson.toJson(u_dao.getUser(Integer.parseInt(request.getParameter("id")))));
            } else {
                User u = new User();
                u.setId(Integer.parseInt(request.getParameter("id")));
                u_dao.delete(u);
                load(request,response);
            }
        } else {
            load(request,response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
