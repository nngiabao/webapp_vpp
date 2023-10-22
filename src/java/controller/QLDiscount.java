/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.CategoryDAO;
import DAO.DiscountDAO;
import DAO.ProductDAO;
import Model.Discount;
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
@WebServlet(name = "QLDiscount", urlPatterns = {"/qlgg"})
public class QLDiscount extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DiscountDAO d_dao = new DiscountDAO();
    HttpSession session;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current_date=LocalDate.now().format(formatter);
    
    protected void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession();
        User u = (User) session.getAttribute("user");
        request.setAttribute("discount", d_dao.getAll());
        request.getRequestDispatcher("QLDiscount.jsp").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
                        PrintWriter out = response.getWriter();
        String action = null;
        if ((action = request.getParameter("action")) != null) {
            if (action.equals("add")) {
                Discount d = new Discount();
                d.setActive(Integer.parseInt(request.getParameter("active")));
                d.setCoupon(request.getParameter("coupon"));
                d.setName(request.getParameter("name"));
                d.setDiscount_percent(Integer.parseInt(request.getParameter("percent")));
                d.setCreated_at(current_date);
                if (d_dao.add(d)) {
                    request.setAttribute("nofi", "Add Success");
                    load(request, response);
                }else {
                    request.setAttribute("nofi", "Add Fail");
                    load(request, response);
                }
            } else if (action.equals("edit")) {
                Discount d = new Discount();
                d.setActive(Integer.parseInt(request.getParameter("active")));
                d.setCoupon(request.getParameter("coupon"));
                d.setName(request.getParameter("name"));
                d.setDiscount_percent(Integer.parseInt(request.getParameter("percent")));
                d.setModified_at(current_date);
                d.setId(Integer.parseInt(request.getParameter("id")));
                if (d_dao.edit(d)) {
                    request.setAttribute("nofi", "Edit Success");
                    load(request, response);
                } else {
                    request.setAttribute("nofi", "Fail");
                    load(request, response);

                }
            } else if (action.equals("checkCoupon")) {  

                String id;
                if(d_dao.checkCoupon(request.getParameter("coupon"))){
                    out.print("Invalid coupon");
                }else{
                    out.print("Valid coupon");
                }
             } else if (action.equals("getDiscount")) {  
                 Gson gson = new Gson();
                 out.print(gson.toJson(d_dao.getDiscount(Integer.parseInt(request.getParameter("id")))));
            } else {
                Discount d = new Discount();
                d.setDeleted_at(current_date);
                d.setId(Integer.parseInt(request.getParameter("id")));
                d_dao.delete(d);
                request.setAttribute("nofi", "Delete successfully");
                load(request, response);
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
