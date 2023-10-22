/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.CategoryDAO;
import DAO.DiscountDAO;
import DAO.OrderDAO;
import DAO.ProductDAO;
import DAO.ShoppingDAO;
import Model.Cart_items;
import Model.Category;
import Model.Order;
import Model.Order_item;
import Model.Product;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author askm4
 */
@WebServlet(name = "ShoppingServlet", urlPatterns = {"/cart"})
public class ShoppingServlet extends HttpServlet {

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
    ArrayList<Cart_items> cart = null;
    ShoppingDAO s_dao = new ShoppingDAO();
    int cart_quan;

    protected void addNewProduct(int id, int u_id) {
        Cart_items i = new Cart_items();
        i.setP_id(id);
        i.setQuan(1);
        i.setCreated_at(current_date);
        cart.add(i);
        if (u_id != 0) {
            s_dao.addCart(i, u_id);
        }
    }

    protected void setQuan(int id, int quan, int u_id) {
        for (Cart_items i : cart) {
            if (i.getP_id() == id) {
                cart_quan -= i.getQuan();
                i.setQuan(quan);
                cart_quan += i.getQuan();
                i.setModified_at(current_date);
                System.out.println(i.getS_id());
                if (u_id != 0) {
                    s_dao.updateCart(i,u_id);
                }
                break;
            }
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String action;
        PrintWriter out = response.getWriter();
        if ((action = request.getParameter("action")) != null) {
            int u_id = 0;
            cart_quan = (int) session.getAttribute("cart_quan");
            User u = (User) session.getAttribute("user");
            cart = (ArrayList<Cart_items>) session.getAttribute("shoppingcart");
            if (u != null) {
                u_id = u.getId();
            }
            if (action.equals("setQuan")) {
                setQuan(Integer.parseInt(request.getParameter("id")), Integer.parseInt(request.getParameter("quan")), u_id);
                session.setAttribute("cart_quan", cart_quan);
                out.print(cart_quan);
            } else if (action.equals("addCart")) {//shopping session
                int id = Integer.parseInt(request.getParameter("id"));
                if (s_dao.checkQuan(id) < 1) {
                    out.print("Out of stock");
                } else {
                    if (session.getAttribute("shoppingcart") != null) {
                        boolean check = false;
                        for (Cart_items items : cart) {
                            if (items.getP_id() == id) {
                                items.setQuan(items.getQuan() + 1);
                                check = true;
                                items.setModified_at(current_date);
                                if (u_id != 0) {
                                    s_dao.updateCart(items,u_id);
                                }
                                break;
                            }
                        }
                        if (!check) {
                            addNewProduct(id, u_id);
                        }
                        session.setAttribute("shoppingcart", cart);
                        session.setAttribute("cart_quan", (cart_quan += 1));
                        out.print(cart_quan);
                    } else {
                        cart = new ArrayList();
                        if (u_id != 0) {
                            Session s = new Session();
                            s.setUser_id(u_id);
                            s.setCreated_at(current_date);
                            s_dao.addNewSession(s);
                        }
                        addNewProduct(id, u_id);
                        session.setAttribute("cart_quan", (cart_quan += 1));
                        session.setAttribute("shoppingcart", cart);
                        out.print(cart_quan);
                    }
                }
            } else if (action.equals("getPromotion")) { //giam gia
                DiscountDAO d_dao = new DiscountDAO();
                String coupon;
                float discount_price, discount_total = 0;
                if (d_dao.checkCoupon(coupon = request.getParameter("coupon"))) {
                    for (Cart_items i : cart) {
                        System.out.println("id"+i.getP_id());
                        if ((discount_price = d_dao.getDiscountPrice(coupon, i.getP_id())) != 0) {
                            discount_total += discount_price * i.getQuan();
                        }
                    }
                    out.print(discount_total);
                } else {
                    out.print("Invalid coupon");
                }
            } else if (action.equals("checkOut")) {// thanh toan
                ArrayList<Cart_items> cartFilter;
                if (u_id != 0) {
                    if ((cartFilter = (ArrayList<Cart_items>) cart.stream().filter((Cart_items i) -> {
                        return i.getQuan() > s_dao.checkQuan(i.getP_id());
                    }).collect(Collectors.toList())).size() != 0) {
                        ProductDAO p_dao = new ProductDAO();
                        out.print("List product is not enough in stock:\n");
                        cartFilter.forEach((Cart_items) -> { Product p =  p_dao.getProduct(Cart_items.getP_id());
                        out.print(p.getName() +"- Current in stock: "+p.getQuant()+"\n");
                        });
                    } else {
                        OrderDAO o_dao = new OrderDAO();
                        Order o = new Order();
                        o.setU_id(u_id);
                        o.setCreated_at(current_date);
                        o.setTotal((int) Float.parseFloat(request.getParameter("total")));
                        o_dao.addOrder(o);
                        for (Cart_items i : cart) {
                            Order_item o_item = new Order_item();
                            o_item.setCreated_at(current_date);
                            o_item.setP_id(i.getP_id());
                            o_item.setQuan(i.getQuan());
                            o_dao.addOrderItem(o_item);
                        }
                        session.removeAttribute("shoppingcart");
                        session.setAttribute("cart_quan", cart_quan = 0);
                        s_dao.removeShoppingSession(u_id);
                        out.print("Order successfully");
                    }
                } else {
                    out.print("Login to checkout");
                }
            } else if (action.equals("removeCart")) {
                for (Cart_items i : cart) {
                    if (i.getP_id() == Integer.parseInt(request.getParameter("p_id"))) {
                        if (u_id != 0) {
                            s_dao.removeCart(i);
                        }
                        cart_quan -= i.getQuan();
                        cart.remove(i);
                        session.setAttribute("shoppingcart", cart);
                        session.setAttribute("cart_quan", cart_quan);
                        break;
                    }
                }
                out.print(cart_quan);
            }
        } else {
            if ((cart = (ArrayList<Cart_items>) session.getAttribute("shoppingcart")) != null) {
                ProductDAO p_dao = new ProductDAO();
                request.setAttribute("listCartProduct", p_dao.getCartProduct(cart));
            }
            CategoryDAO c_dao = new CategoryDAO();
            List<Category> list = c_dao.getAll();
            request.setAttribute("category", list);
            request.getRequestDispatcher("shoppingcart.jsp").forward(request, response);
        }

        //ShoppingDAO dao = new ShoppingDAO();
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
        /*if(request.getParameter("action").equals("addCart")){
            /*if(dao.getSession(request.getParameter("id").equals(""))){
                //dao.add(new );
            }else{
                //dao.update();
            }
        }else{
            
        }*/
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
