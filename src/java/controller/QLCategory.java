/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.CategoryDAO;
import DAO.DiscountDAO;
import DAO.ProductDAO;
import Model.Category;
import Model.Product;
import Model.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author askm4
 */
@WebServlet(name = "QLCategory", urlPatterns = {"/qlloai"})
@MultipartConfig
public class QLCategory extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    CategoryDAO c_dao = new CategoryDAO();
    String fileName;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String current_date = LocalDate.now().format(formatter);
    HttpSession session;

    protected void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession();
        request.setAttribute("category", c_dao.getAll());
        request.getRequestDispatcher("QLCategory.jsp").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = null;

        if ((action = request.getParameter("action")) != null) {
            if (action.equals("add")) {
                Category c = new Category();
                c.setName(request.getParameter("name"));
                c.setCreated_at(current_date);
                if (c_dao.add(c)) {
                    load(request, response);
                }
            } else if (action.equals("edit")) {
                Category c = new Category();
                c.setName(request.getParameter("name"));
                c.setModified_at(current_date);
                c.setId(Integer.parseInt(request.getParameter("id")));
                if (c_dao.edit(c)) {
                    load(request, response);
                }
            } else if (action.equals("delete")) {
                Category c = new Category();
                c.setId(Integer.parseInt(request.getParameter("id")));
                c.setDeleted_at(current_date);
                if (c_dao.delete(c)) {
                    load(request, response);
                }
            } else if (action.equals("getCategory")) {
                Category c;
                request.setAttribute("product", (c = c_dao.getCategory(Integer.parseInt(request.getParameter("id")))));
                PrintWriter out = response.getWriter();
                Gson gson = new Gson();
                String id;
                out.print(gson.toJson(c));

            }
        } else {
            load(request, response);
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
