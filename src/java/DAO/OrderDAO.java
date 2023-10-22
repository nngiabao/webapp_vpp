/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Cart_items;
import Model.Order;
import Model.Order_item;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author askm4
 */
public class OrderDAO extends DBContext {

    public ArrayList<Order> getAll() {
        ArrayList<Order> list = new ArrayList();
        try {
            String sql = "select * from order_detail";
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Order(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("total"), rs.getString("created_at"), rs.getString("modified_at")));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public ArrayList<Order_item> getOrderDetail(int o_id) {
        ArrayList<Order_item> list = new ArrayList();
        try {
            String sql = "select * from order_item where order_id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, o_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Order_item(rs.getInt("id"), rs.getInt("order_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getString("created_at"), rs.getString("modified_at")));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Order_item getOrder(int o_id) {
        ArrayList<Order_item> list = new ArrayList();
        try {
            String sql = "select * from order_item where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, o_id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Order_item(rs.getInt("id"), rs.getInt("order_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getString("created_at"), rs.getString("modified_at"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void addOrder(Order o) {
        try {
            String sql = "INSERT INTO [dbo].[order_detail]\n"
                    + "           ([user_id]\n"
                    + "           ,[total]\n"
                    + "           ,[created_at])\n"
                    + "     VALUES\n"
                    + "           (?,?,?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, o.getU_id());
            st.setInt(2, o.getTotal());
            st.setString(3, o.getCreated_at());
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addOrderItem(Order_item i) {
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO [dbo].[order_item]\n"
                    + "           ([product_id]\n"
                    + "           ,[quantity]\n"
                    + "           ,[created_at]\n"
                    + "           ,[order_id])\n"
                    + "     VALUES\n"
                    + "           (?,?,?,(select max(id) from order_detail))";
            String sql2 = "update product_inventory set quantity=(quantity-?) where id=(select inventory_id from product where id=?)";
            PreparedStatement st = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st.setInt(1, i.getP_id());
            st.setInt(2, i.getQuan());
            st.setString(3, i.getCreated_at());
            st2.setInt(1, i.getQuan());
            st2.setInt(2, i.getP_id());
            st.executeUpdate();
            st2.executeUpdate();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateOrder(Order_item o) {
        try {
            connection.setAutoCommit(false);
            String sql2 = "update product_inventory set quantity=(quantity+(select quantity from order_item where id=?)-?) where id=(select inventory_id from product where id=?)";
            String sql = "UPDATE [dbo].[order_item]\n"
                    + "   SET [quantity] = ?\n"
                    + "      ,[modified_at] = ?\n"
                    + " WHERE id=?";
            String sql3 = "update order_detail set total=(select sum(p.price*o.quantity) from product as p,order_item as o where o.order_id=? and p.id=o.product_id) where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            PreparedStatement st3 = connection.prepareStatement(sql3);
            st.setInt(3, o.getId());
            st.setInt(1, o.getQuan());
            st.setString(2, o.getModified_at());
            st2.setInt(2, o.getQuan());
            st2.setInt(1, o.getId());
            st2.setInt(3, o.getP_id());
            st3.setInt(1, o.getO_id());
            st3.setInt(2, o.getO_id());
            st2.executeUpdate();
            st.executeUpdate();
            st3.executeUpdate();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
