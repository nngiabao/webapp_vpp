/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Discount;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author askm4
 */
public class DiscountDAO extends DBContext {
    
    public List<Discount> getAll() {
        List<Discount> list = new ArrayList();
        try {
            String sql = "select * from discount";
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Discount(rs.getInt("id"),rs.getInt("discount_percent"),rs.getInt("active") ,rs.getString("name"), 
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"), rs.getString("coupon")));
            }
            return list;
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public Discount getDiscount(int id) {
        List<Discount> list = new ArrayList();
        try {
            String sql = "select * from discount where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Discount(rs.getInt("id"),rs.getInt("discount_percent"),rs.getInt("active") ,rs.getString("name"), 
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"), rs.getString("coupon"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public boolean checkCoupon(String coupon){
        try {
            String sql = "select * from discount where coupon=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, coupon);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            } 
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    public float getDiscountPrice(String coupon,int id){
        try {
            String sql = "select p.price*d.discount_percent/100 as discount_price from discount as d,product as p where d.coupon=? and p.id=? and p.discount_id=d.id";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(2, id);
            st.setString(1, coupon);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getFloat("discount_price"); 
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public boolean add(Discount d){
        String sql="INSERT INTO [dbo].[discount]\n" +
"           ([name]\n" +
"           ,[discount_percent]\n" +
"           ,[active]\n" +
"           ,[created_at]\n" +
"           ,[coupon])\n" +
"     VALUES\n" +
"           (?,?,?,?,?)";
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, d.getName());
            st.setInt(2, d.getDiscount_percent());
            st.setInt(3, d.getActive());
            st.setString(4, d.getCreated_at());
            st.setString(5, d.getCoupon());
            st.executeUpdate();
            return true;
        }catch(Exception e){
            
        }
        return false;
    }
    
    public boolean edit(Discount d){
        String sql="UPDATE [dbo].[discount]\n" +
"   SET [name] = ?\n" +
"      ,[discount_percent] = ?\n" +
"      ,[active] = ?\n" +
"      ,[modified_at] = ?\n" +
"      ,[coupon] = ?\n" +
" WHERE id=?";
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, d.getName());
            st.setInt(2, d.getDiscount_percent());
            st.setInt(3, d.getActive());
            st.setString(4, d.getModified_at());
            st.setString(5, d.getCoupon());
            st.setInt(6, d.getId());
            st.executeUpdate();
        }catch(Exception e){
            
        }
        return false;
    }
    
    public boolean delete(Discount d){
        String sql="UPDATE [dbo].[discount] SET deleted_at = ? WHERE id=?";
        try{
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, d.getDeleted_at());
            st.setInt(2, d.getId());
            st.executeUpdate();
        }catch(Exception e){
            
        }
        return false;
    }
}
