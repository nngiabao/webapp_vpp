/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import DAO.ShoppingDAO;
import DAO.UserDAO;
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
@WebServlet(name = "QLUserServlet", urlPatterns = {"/qlorder"})
public class QLOrderServlet extends HttpServlet {

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
        OrderDAO o_dao = new OrderDAO();
        PrintWriter out = response.getWriter();
        if ((action = request.getParameter("action")) != null) {
            if (action.equals("getOrderDetail")) {
                ArrayList<Order_item> list = o_dao.getOrderDetail(Integer.parseInt(request.getParameter("id")));
                String listOrderitem = "";
                for (Order_item o : list) {
                    listOrderitem += "<tr> "
                            + "<td>" + o.getO_id() + "</td>\n"
                            + "<td>" + o.getP_id() + "</td>\n"
                            + "<td>" + o.getQuan() + "</td>\n"
                            + "<td>" + o.getCreated_at() + "</td>\n"
                            + "<td>" + o.getModified_at() + "</td>\n"
                            + "<td><button class='btn btn-default' id='editbutton' value=" + o.getId() + " data-target='#editModal' data-toggle='modal'><em class='fa fa-pencil-ruler'></em></button></tr>";
                }
                out.print(listOrderitem);
            } else if (action.equals("getOrder")) {
                request.setAttribute("product", o_dao.getOrder(Integer.parseInt(request.getParameter("id"))));
                Gson gson = new Gson();
                out.print(gson.toJson(o_dao.getOrder(Integer.parseInt(request.getParameter("id")))));
            } else if (action.equals("edit")) {
                Order_item o = new Order_item();
                o.setId(Integer.parseInt(request.getParameter("id")));
                o.setModified_at(current_date);
                o.setQuan(Integer.parseInt(request.getParameter("quan")));
                o.setP_id(Integer.parseInt(request.getParameter("p_id")));
                o.setO_id(Integer.parseInt(request.getParameter("o_id")));
                o_dao.updateOrder(o);
            } else if (action.equals("checkQuan")) {
                ShoppingDAO s_dao = new ShoppingDAO();
                int old_quan = Integer.parseInt(request.getParameter("old_quan"));
                int new_quan = Integer.parseInt(request.getParameter("new_quan"));
                int current_stock;
                if (((current_stock = (s_dao.checkQuan(Integer.parseInt(request.getParameter("id"))) + old_quan)) - new_quan) >= 0) {
                    out.print("Valid quantity");
                } else {
                    out.print("Not enough in stock - Current stock: " + current_stock);
                };
            }
        } else {
            request.setAttribute("orders", o_dao.getAll());
            request.getRequestDispatcher("QLOrder.jsp").forward(request, response);
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
