/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.CategoryDAO;
import DAO.DiscountDAO;
import DAO.ProductDAO;
import Model.Inventory;
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
import static java.util.Objects.nonNull;

/**
 *
 * @author askm4
 */
@WebServlet(name = "QLProduct", urlPatterns = {"/qlsp"})
@MultipartConfig
public class QLProduct extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ProductDAO p_dao = new ProductDAO();
    DiscountDAO d_dao = new DiscountDAO();
    CategoryDAO c_dao = new CategoryDAO();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String current_date = LocalDate.now().format(formatter);
    String fileName;
    HttpSession session;

    protected void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session = request.getSession();
        request.setAttribute("discount", d_dao.getAll());
        request.setAttribute("products", p_dao.getAll());
        request.setAttribute("category", c_dao.getAll());
        request.getRequestDispatcher("QLProduct.jsp").forward(request, response);
    }

    protected boolean saveImg(HttpServletRequest request) throws ServletException, IOException {//luu hinh
        Part part;
        if (!(part = request.getPart("image")).getSubmittedFileName().equals("")) {
            String realPath = request.getServletContext().getRealPath("/img");
            fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            if (!Files.exists(Paths.get(realPath))) {
                Files.createDirectory(Paths.get(realPath));
            }
            part.write(realPath + "/" + fileName);
        }
        return true;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        Gson gson = new Gson();
        String action = null;

        if ((action = request.getParameter("action")) != null) {
            if (action.equals("add")) {
                Product p = new Product();
                p.setPrice(Float.parseFloat(request.getParameter("price")));
                p.setName(request.getParameter("name"));
                p.setD_id(request.getParameter("d_id"));
                p.setC_id(request.getParameter("c_id"));
                p.setCreated_at(current_date);
                p.setImg(fileName);
                saveImg(request);
                if (p_dao.add(p, Math.abs(Integer.parseInt(request.getParameter("quant"))))) {
                    request.setAttribute("nofi", "Success");
                    load(request, response);
                } else {
                    request.setAttribute("nofi", "Fail");
                    load(request, response);
                }
            } else if (action.equals("edit")) {
                Product p = new Product();
                p.setId(Integer.parseInt(request.getParameter("id")));
                p.setPrice(Float.parseFloat(request.getParameter("e_price")));
                p.setName(request.getParameter("e_name"));
                p.setD_id(request.getParameter("e_d_id"));
                p.setI_id(request.getParameter("e_i_id"));
                p.setC_id(request.getParameter("e_c_id"));
                p.setModified_at(current_date);
                if (saveImg(request)) {
                    p.setImg(fileName);
                }
                if (p_dao.edit(p)) {
                    request.setAttribute("nofi", "Edit Successfully");
                } else {
                    request.setAttribute("nofi", "Fail");
                }
                load(request, response);
            } else if (action.equals("getProduct")) {
                request.setAttribute("product", p_dao.getProduct(Integer.parseInt(request.getParameter("id"))));
                PrintWriter out = response.getWriter();
                out.print(gson.toJson(p_dao.getProduct(Integer.parseInt(request.getParameter("id")))));
            } else if (action.equals("updateStock")) {
                Inventory i = new Inventory();
                i.setId(Integer.parseInt(request.getParameter("id")));
                i.setQuan(Integer.parseInt(request.getParameter("quan")));
                i.setModified_date(current_date);
                if (p_dao.updateInventory(i)) {
                    request.setAttribute("nofi", "Update Successfully");
                } else {
                    request.setAttribute("nofi", "Fail");
                }
                load(request, response);
            } else if (action.equals("getStock")) {
                PrintWriter out = response.getWriter();
                out.print(gson.toJson(p_dao.getStock(Integer.parseInt(request.getParameter("id")))));
            } else {
                Product p = new Product();
                p.setId(Integer.parseInt(request.getParameter("id")));
                p.setDeleted_at(current_date);
                p.setI_id(request.getParameter("i_id"));
                p_dao.delete(p);
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
