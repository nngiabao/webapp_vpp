/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.ShoppingDAO;
import DAO.UserDAO;
import  Model.cipher;
import Model.Cart_items;
import Model.Order;
import Model.Session;
import Model.User;
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
import java.util.Calendar;


/**
 *
 * @author askm4
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

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
    String current_date = LocalDate.now().format(formatter);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        UserDAO dao = new UserDAO();
        PrintWriter out = response.getWriter();
        ShoppingDAO s_dao = new ShoppingDAO();
        if (request.getParameter("action").equals("login")) {
            User u = dao.login(request.getParameter("email"), cipher.encode(request.getParameter("pass")));
            int u_id;
            HttpSession session = request.getSession();
            if (u != null) {
                session.setAttribute("user", u);
                if (u.getU_c_id() > 2) {
                    ArrayList<Cart_items> cart;
                    if ((cart = s_dao.getSession(u.getId())).size() != 0) {
                        session.removeAttribute("shoppingcart");
                        session.removeAttribute("cart_quan");
                        session.setAttribute("shoppingcart", cart);
                        session.setAttribute("cart_quan", cart.stream()
                                .mapToInt(x -> x.getQuan())
                                .sum());
                    } else {
                        if ((cart = (ArrayList<Cart_items>) session.getAttribute("shoppingcart")) != null) {
                            Session s = new Session();
                            s.setCreated_at(current_date);
                            s.setUser_id(u_id = u.getId());
                            s_dao.addNewSession(s);
                            for (Cart_items i : cart) {
                                s_dao.addCart(i, u_id);
                            }
                            s_dao.updateTotalSession(u_id);
                            session.setAttribute("shoppingcart", cart);
                            session.setAttribute("cart_quan", cart.size());
                        }
                    }
                    response.sendRedirect("index");
                } else {
                    response.sendRedirect("qlgg");
                }
            } else {
                session.setAttribute("error", "Tai khoan khong ton tai");
                response.sendRedirect("login.jsp");
            }
        } else if (request.getParameter("action").equals("logout")) {
            HttpSession session = request.getSession();
            session.removeAttribute("user");
            session.removeAttribute("shoppingcart");
            session.removeAttribute("cart_quan");
            response.sendRedirect("index");
        } else if (request.getParameter("action").equals("update")) {

        } else if (request.getParameter("action").equals("checkEmail")) {
            
            if (!dao.checkEmail(request.getParameter("email"))) {
                out.print("Valid Email");
            } else {
                out.print("Invalid Email");
            }
        } else if (request.getParameter("action").equals("checkPhone")) {
            if (!dao.checkPhone(request.getParameter("phone"))) {
                out.print("Valid Phone");
            } else {
                out.print("Invalid Phone");
            }
        } else if (request.getParameter("action").equals("signup")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            if (dao.signup(new User(0, request.getParameter("email"), cipher.encode(request.getParameter("pass")), request.getParameter("name"),
                    request.getParameter("phone"), LocalDate.now().toString(), null, 3))) {
                request.setAttribute("result", "Success");
            } else {
                request.setAttribute("result", "Fail");
            }
            //request.getRequestDispatcher("login.jsp").forward(request,response);
        } else {
            response.sendRedirect("index");
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
