/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.cipher;
import Model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author askm4
 */
public class UserDAO extends DBContext {

    public ArrayList<User> getAll() {
        ArrayList<User> list = new ArrayList();
        String sql = "select * from users";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"), cipher.decode(rs.getString("pass")),
                        rs.getString("name"), rs.getString("phone"), rs.getString("created_at"), rs.getString("modified_at"), rs.getInt("u_c_id")));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public User getUser(int u_id) {
        try {
            String sql = "select * from users where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, u_id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), cipher.decode(rs.getString("pass")),
                        rs.getString("name"), rs.getString("phone"), rs.getString("created_at"), rs.getString("modified_at"), rs.getInt("u_c_id"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public User login(String email, String pass) {
        String sql = "select * from users where username=? and pass=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("pass"),
                        rs.getString("name"), rs.getString("phone"), rs.getString("created_at"), rs.getString("modified_at"), rs.getInt("u_c_id"));
            }
        } catch (Exception e) {

        }
        return null;
    }

    public boolean signup(User u) {
        String sql = "insert into users(username,pass,name,phone,created_at) values (?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, u.getEmail());
            st.setString(2, u.getPass());
            st.setString(3, u.getName());
            st.setString(4, u.getPhone());
            st.setString(5, u.getCreated_at());
            st.executeUpdate();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean checkEmail(String email) {
        String sql = "select * from users where username=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean checkPhone(String phone) {
        String sql = "select * from users where phone=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, phone);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean add(User u) {
        String sql = "INSERT INTO [dbo].[users]\n"
                + "           ([username]\n"
                + "           ,[pass]\n"
                + "           ,[name]\n"
                + "           ,[phone]\n"
                + "           ,[created_at])\n "
                + "     VALUES\n"
                + "           (?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, u.getEmail());
            st.setString(2, u.getPass());
            st.setString(3, u.getName());
            st.setString(4, u.getPhone());
            st.setString(5, u.getCreated_at());
            st.executeUpdate();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean edit(User u) {
        String sql = "UPDATE [dbo].[users]\n"
                + "   SET [username] = ?\n"
                + "      ,[pass] = ?\n"
                + "      ,[name] = ?\n"
                + "      ,[phone] = ?\n"
                + "      ,[modified_at] = ?\n"
                + "      ,[u_c_id] = ?\n"
                + " WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, u.getEmail());
            st.setString(2, u.getPass());
            st.setString(3, u.getName());
            st.setString(4, u.getPhone());
            st.setString(5, u.getModified_at());
            st.setInt(6, u.getU_c_id());
            st.setInt(7, u.getId());
            st.executeUpdate();
        } catch (Exception e) {

        }
        return false;
    }
    
    public boolean delete(User u) {
        String sql = "delete from users where id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, u.getId());
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
