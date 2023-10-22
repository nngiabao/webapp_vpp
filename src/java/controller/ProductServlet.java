/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Model.Cart_items;
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

/**
 *
 * @author askm4
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/product"})
public class ProductServlet extends HttpServlet {

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
    ArrayList<Cart_items> cart = null;

    protected void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request.setAttribute("discount", d_dao.getAll());
        //request.setAttribute("products", p_dao.getAll());
        //request.setAttribute("category", c_dao.getAll());
        request.getRequestDispatcher("QLProduct.jsp").forward(request, response);
    }

    protected void addNewProduct(int id) {
        Cart_items i = new Cart_items();
        i.setP_id(id);
        i.setQuan(1);
        i.setModified_at(LocalDate.now().format(formatter));
        cart.add(i);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action;

        if (!(action = request.getParameter("action")).equals("")) {
            HttpSession session = request.getSession();
            if (action.equals("addCart")) {//shopping session
                PrintWriter out = response.getWriter();
                int id = Integer.parseInt(request.getParameter("id"));
                if (session.getAttribute("shoppingcart") != null) {
                    cart = (ArrayList<Cart_items>) session.getAttribute("shoppingcart");
                    int cart_quan = 0;
                    if (session.getAttribute("cart-quan") != null) {
                        cart_quan = Integer.parseInt(session.getAttribute("cart-quan").toString());
                    }
                    boolean check = false;
                    for (Cart_items items : cart) {
                        if (items.getP_id() == id) {
                            items.setQuan(items.getQuan() + 1);
                            session.setAttribute("cart-quan", (cart_quan + 1));
                            check = true;
                            break;
                        }
                    }
                    if (!check) addNewProduct(id);
                    session.setAttribute("shoppingcart", cart);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    cart = new ArrayList();
                    addNewProduct(id);
                    session.setAttribute("shoppingcart", cart);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
                if (cart.get(0).getS_id() != 0) {
                    System.out.println("hehe");
                }

                //}
                /*if (session.getAttribute("user") != null) {
                    //Integer.toString(Integer.parseInt((String) session.getAttribute("cart-quan"))+1)
                } else {
                    session.setAttribute("cart-quan", "3");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }*/
            } else if (request.getParameter("action").equals("del")) {

            } else if (request.getParameter("action").equals("edit")) {

            } else {

            }
        } else {
            //load();
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
