/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Cart_items;
import Model.Product;
import Model.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author askm4
 */
public class ShoppingDAO extends DBContext {
    public ArrayList<Session> getAll() {
        ArrayList<Session> list = new ArrayList();
        try {
            String sql = "select * from shopping_session";
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Session(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("total"),rs.getString("created_at"),rs.getString("modified_at")));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public ArrayList<Cart_items> getSession(int u_id) {
        ArrayList<Cart_items> list = new ArrayList();
        try {
            String sql = "select c.id,c.s_id,c.p_id,c.created_at,c.modified_at,c.quan from users as u,shopping_session as s,cart_items as c where u.id=s.user_id and s.id=c.s_id and u.id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, u_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Cart_items(rs.getInt("id"),rs.getInt("s_id"),rs.getInt("quan"),rs.getInt("p_id"),rs.getString("created_at"),rs.getString("modified_at")));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Cart_items getCart(int id) {
        try {
            String sql = "select * from cart_items where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Cart_items(rs.getInt("id"), rs.getInt("s_id"), rs.getInt("quan"), rs.getInt("p_id"),
                        rs.getString("created_at"), rs.getString("modified_at"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public void addCart(Cart_items i, int u_id) {
        try {
            String sql = "insert into cart_items(s_id,p_id,quan,created_at) values((select id from shopping_session where user_id=?),?,?,?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, u_id);
            st.setInt(2, i.getP_id());
            st.setInt(3, i.getQuan());
            st.setString(4, i.getCreated_at());
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println("addcart" + e);
        }
    }

    public void addNewSession(Session s) {
        try {
            String sql = "INSERT INTO [dbo].[shopping_session]\n"
                    + "           ([user_id]\n"
                    + "           ,[created_at])\n"
                    + "     VALUES\n"
                    + "           (?,?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, s.getUser_id());
            st.setString(2, s.getCreated_at());
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateSession(Session s) {
        try {
            String sql = "INSERT INTO [dbo].[shopping_session]\n"
                    + "           ([user_id]\n"
                    + "           ,[created_at])\n"
                    + "     VALUES\n"
                    + "           (?,?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, s.getUser_id());
            st.setString(2, s.getCreated_at());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateCart(Cart_items i,int u_id) {
        try {
            connection.setAutoCommit(false);
            String sql = "UPDATE [dbo].[cart_items]\n"
                    + "   SET [quan] = ?\n"
                    + "      ,[modified_at] = ?\n"
                    + "  where p_id = ?";
            String sql2 = "update shopping_session set total="
                    + "(select sum(p.price*i.quan) from product as p,cart_items as i "
                    + "where p.id=i.p_id and i.s_id=(select s_id from shopping_session where user_id="+u_id+")) "
                    + "where user_id="+u_id;
            connection.setAutoCommit(false);
            PreparedStatement st = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st.setInt(1, i.getQuan());
            st.setString(2, i.getModified_at());
            st.setInt(3, i.getP_id());
            st.executeUpdate();
            st2.executeUpdate();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeCart(Cart_items i) {
        try {
            String sql = "delete from cart_items where p_id=?";
            String sql2 = "update shopping_session set total="
                    + "(select sum(p.price*i.quan) from product as p,cart_items as i "
                    + "where p.id=i.p_id and i.s_id=?) "
                    + "where id=?";
            connection.setAutoCommit(false);
            PreparedStatement st = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st2.setInt(1, i.getS_id());
            st2.setInt(2, i.getS_id());
            st.setInt(1, i.getP_id());
            st.executeUpdate();
            st2.executeUpdate();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int checkQuan(int p_id) {
        try {
            String sql = "select quantity from product_inventory as i,product as p where p.inventory_id=i.id and p.id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, p_id );
            ResultSet rs = st.executeQuery();
            if(rs.next()) return rs.getInt("quantity");
        }catch(Exception e){
            System.out.println(e);
        }
        return 0;
    }
    
    public void removeShoppingSession(int u_id) {
        try {
            connection.setAutoCommit(false);
            String sql = "delete from cart_items where s_id=(select id from shopping_session where user_id=?)";
            String sql2 = "delete from shopping_session where user_id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st.setInt(1, u_id);
            st2.setInt(1, u_id);
            st.executeUpdate();
            st2.executeUpdate();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateTotalSession(int u_id) {
        try {
            String sql = "update shopping_session set total="
                    + "(select sum(p.price*i.quan) from product as p,cart_items as i,shopping_session as s "
                    + "where p.id=i.p_id and i.s_id=s.id and s.user_id=?) "
                    + "where user_id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, u_id);
            st.setInt(2, u_id);
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println("updateses" + e);
        }
    }

}
