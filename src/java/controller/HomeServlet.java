/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.CategoryDAO;
import DAO.ProductDAO;
import Model.Cart_items;
import Model.Category;
import Model.Product;
import Model.Session;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author askm4
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/index"})
public class HomeServlet extends HttpServlet {

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
    ProductDAO p_dao = new ProductDAO();
    List<Product> list2;
    String page = null, category = null;

    protected void load(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ArrayList<Cart_items> cart = null;
        if ((cart = (ArrayList<Cart_items>) session.getAttribute("shoppingcart")) == null) {
            session.setAttribute("cart_quan", 0);
        }
        if(category!=null) request.setAttribute("cid", category); ;
        List<Category> list1 = c_dao.getAll();
        request.setAttribute("category", list1);
        request.setAttribute("products", list2);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        category = request.getParameter("cid");
        String action;
        if ((action = request.getParameter("action")) != null) {
            if (action.equals("login")) {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                String txt = request.getParameter("search_msg");
                if ((page = request.getParameter("page")) != null) {
                    list2 = p_dao.search(Integer.parseInt(page), txt);
                    request.setAttribute("current", page);
                    request.setAttribute("maxP", p_dao.getMaxPageSearch(txt));
                    request.setAttribute("search_msg", txt);

                } else {
                    list2 = p_dao.search(1, txt);
                    request.setAttribute("current", 1);
                    request.setAttribute("maxP", p_dao.getMaxPageSearch(txt));
                    request.setAttribute("search_msg", txt);
                }
                load(request, response);
            }
        } else {
            if ((page = request.getParameter("page")) != null) {
                list2 = p_dao.getFromPage(Integer.parseInt(page), category);
                request.setAttribute("current", page);
                request.setAttribute("maxP", p_dao.getMaxPage(category));               
            } else {
                list2 = p_dao.getFromPage(1, category);
                request.setAttribute("current", 1);
                request.setAttribute("maxP", p_dao.getMaxPage(category));
            }
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
