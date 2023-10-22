/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Category;
import Model.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author askm4
 */
public class CategoryDAO extends DBContext {

    public List<Category> getAll() {
        List<Category> list = new ArrayList();
        try {
            String sql = "select * from product_category";
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name"),
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at")));
            }
            return list;
        } catch (Exception e) {

        }
        return null;
    }
   public Category getCategory(int id) {
        try {
            String sql = "select * from product_category where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) 
                return new Category(rs.getInt("id"), rs.getString("name"),
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    } 
   
    public boolean add(Category c) {
        try {
            String sql = "INSERT INTO [dbo].[product_category]\n"
                    + "           ([name]\n"
                    + "           ,[created_at])\n"
                    + "     VALUES\n"
                    + "           (?,?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getName());
            st.setString(2, c.getCreated_at());
            st.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean edit(Category c) {
        try {
            String sql = "UPDATE [dbo].[product_category]\n" +
"   SET [name] = ?\n" +
"      ,[modified_at] = ?\n" +
" WHERE id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getName());
            st.setString(2, c.getModified_at());
            st.setInt(3, c.getId());
            st.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
     public boolean delete(Category c) {
        try {
            String sql = "UPDATE [dbo].[product_category]\n" +
"   SET deleted_at = ? WHERE id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getDeleted_at());
            st.setInt(2, c.getId());
            st.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
