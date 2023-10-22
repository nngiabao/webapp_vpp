/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import DAO.ShoppingDAO;
import DAO.UserDAO;
import Model.Cart_items;
import Model.Order;
import Model.Order_item;
import Model.Session;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author askm4
 */
@WebServlet(name = "QLSessionServlet", urlPatterns = {"/qlsession"})
public class QLSessionServlet extends HttpServlet {

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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String action = null;
        ShoppingDAO s_dao = new ShoppingDAO();
        PrintWriter out = response.getWriter();
        if ((action = request.getParameter("action")) != null) {
            if (action.equals("getSessionDetail")) {
                int u_id;
                ArrayList<Cart_items> list = (ArrayList<Cart_items>) s_dao.getSession((u_id = Integer.parseInt(request.getParameter("id"))));
                String listCartitem = "";
                for (Cart_items i : list) {
                    listCartitem += "<tr> "
                            + "<td>" + i.getId() + "</td>\n"
                            + "<td>" + i.getP_id() + "</td>\n"
                            + "<td>" + i.getQuan() + "</td>\n"
                            + "<td>" + i.getCreated_at() + "</td>\n"
                            + "<td>" + i.getModified_at() + "</td>\n"
                            + "<input type='hidden'  id='d_u_id' value='" + u_id + "'>\n"
                            + "<td><button class='btn btn-default' id='editbutton' value='" + i.getId() + "' data-target='#editModal' data-toggle='modal'><em class='fa fa-pencil-ruler'></em></button></tr>";
                }
                out.print(listCartitem);
            } else if (action.equals("getSession")) {
                Gson gson = new Gson();
                out.print(gson.toJson(s_dao.getCart(Integer.parseInt(request.getParameter("id")))));
            } else if (action.equals("edit")) {
                Cart_items i = new Cart_items();
                i.setId(Integer.parseInt(request.getParameter("id")));
                i.setModified_at(current_date);
                i.setQuan(Integer.parseInt(request.getParameter("quan")));
                i.setP_id(Integer.parseInt(request.getParameter("p_id")));
                s_dao.updateCart(i, Integer.parseInt(request.getParameter("u_id")));
            } else if (action.equals("checkQuan")) {
                int new_quan = Integer.parseInt(request.getParameter("new_quan"));
                int current_stock;
                if ((current_stock = (s_dao.checkQuan(Integer.parseInt(request.getParameter("id"))))) >= new_quan) {
                    out.print("Valid quantity");
                } else {
                    out.print("Not enough in stock - Current stock: " + current_stock);
                };
            }
        } else {
            request.setAttribute("session", s_dao.getAll());
            request.getRequestDispatcher("QLSession.jsp").forward(request, response);
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
