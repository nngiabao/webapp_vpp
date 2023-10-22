/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Cart_items;
import Model.Inventory;
import Model.Product;
import Model.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author askm4
 */
public class ProductDAO extends DBContext {

    public List<Product> getAll() {
        List<Product> list = new ArrayList();
        try {
            String sql = "select p.id,p.name,p.price,p.discount_id,p.category_id,p.inventory_id,p.created_at,"
                    + "p.deleted_at,p.modified_at,p.img,i.quantity from product as p,product_inventory as i  where p.inventory_id=i.id ";
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("img"),
                        rs.getString("category_id"), rs.getString("discount_id"), rs.getString("inventory_id"),
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"), rs.getFloat("price"), rs.getInt("quantity")));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Inventory getStock(int id) {
        try {
            String sql = "select * from product_inventory where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Inventory(rs.getInt("id"), rs.getInt("quantity"),
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Product> search(int index, String txt) {
        List<Product> list = new ArrayList();
        try {
            String sql = "with x as (select ROW_NUMBER() over (order by p.id desc) as y,p.id,p.name,p.price,p.discount_id,p.category_id,p.inventory_id,p.created_at,p.deleted_at,p.modified_at,p.img,i.quantity from product as p,product_inventory as i where p.inventory_id=i.id and p.deleted_at is null and p.name like '%" + txt + "%' )\n"
                    + "select * from x where y between ? and ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, index * 8 - 7);
            st.setInt(2, index * 8);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("img"),
                        rs.getString("category_id"), rs.getString("discount_id"), rs.getString("inventory_id"),
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"), rs.getFloat("price"), rs.getInt("quantity")));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Product getProduct(int u_id) {
        try {
            String sql = "select p.id,p.name,p.price,p.discount_id,p.category_id,p.inventory_id,p.created_at,"
                    + "p.deleted_at,p.img,p.modified_at,i.quantity from product as p,product_inventory as i where p.inventory_id=i.id and p.id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, u_id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Product(rs.getInt("id"), rs.getString("name"), rs.getString("img"),
                        rs.getString("category_id"), rs.getString("discount_id"), rs.getString("inventory_id"),
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"), rs.getFloat("price"), rs.getInt("quantity"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String getName(int u_id) {
        try {
            String sql = "select name from product where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, u_id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean updateInventory(Inventory i) {
        try {
            String sql = "update product_inventory set quantity=?,modified_at=? where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, i.getQuan());
            st.setString(2, i.getModified_date());
            st.setInt(3, i.getId());
            st.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public List<Product> getFromPage(int index, String category) {
        List<Product> list = new ArrayList();
        try {
            String sql;
            if (category != null) {
                sql = "select p.id,p.name,p.price,p.discount_id,p.category_id,p.inventory_id,p.created_at,p.deleted_at,p.img,p.modified_at,i.quantity from product as p,product_inventory as i where p.inventory_id=i.id and p.deleted_at is null and category_id=" + category + "order by id offset ? rows fetch next 8 rows only";
            } else {
                sql = "select p.id,p.name,p.price,p.discount_id,p.category_id,p.inventory_id,p.created_at,p.deleted_at,p.img,p.modified_at,i.quantity from product as p,product_inventory as i  where p.inventory_id=i.id and p.deleted_at is null order by p.id offset ? rows fetch next 8 rows only";
            }
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (index - 1) * 8);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("img"),
                        rs.getString("category_id"), rs.getString("discount_id"), rs.getString("inventory_id"),
                        rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"), rs.getFloat("price"), rs.getInt("quantity")));
            }
            return list;
        } catch (Exception e) {

        }
        return null;
    }

    public int getMaxPage(String category) {

        try {
            String sql;
            if (category == null) {
                sql = "SELECT COUNT(*) FROM product where deleted_at is null";
            } else {
                sql = "SELECT COUNT(*) FROM product where category_id='" + category + "' and deleted_at is null";
            }
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return (int) Math.ceil((double) rs.getInt(1) / 8);
            }
        } catch (Exception e) {
            System.out.println("loi");
        }
        return 0;
    }

    public int getMaxPageSearch(String txt) {

        try {
            String sql = "SELECT COUNT(*) FROM product where name like '%" + txt + "%' and deleted_at is null";
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return (int) Math.ceil((double) rs.getInt(1) / 8);
            }
        } catch (Exception e) {
            System.out.println("loi");
        }
        return 0;
    }

    public Session getSession(int user_id) {
        try {
            String sql = "select * from shopping_session where id=?";
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Session(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("total"),
                        rs.getString("created_at"), rs.getString("modified_at"));
            }
        } catch (Exception e) {

        }
        return null;
    }

    public String getQuantity(String id) {

        try {
            String sql = "select i.quantity from product as p ,product_inventory as i where p.id=? and p.inventory_id = i.id";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("quantity");
            }
        } catch (Exception e) {

        }
        return null;
    }

    public List<Product> getCartProduct(ArrayList<Cart_items> cart) {
        try {
            ArrayList<Product> list = new ArrayList();
            for (Cart_items i : cart) {
                String sql = "select * from product where id=?";
                PreparedStatement st = connection.prepareStatement(sql);
                st.setInt(1, i.getP_id());
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("img"),
                            rs.getString("category_id"), rs.getString("discount_id"), rs.getString("inventory_id"),
                            rs.getString("created_at"), rs.getString("modified_at"), rs.getString("deleted_at"), rs.getFloat("price"), i.getQuan()));
                }
            }
            return list;
        } catch (Exception e) {

        }
        return null;
    }

    public boolean add(Product p, int quant) {
        String sql = "INSERT INTO [dbo].[product_inventory]\n"
                + "           ([quantity]\n"
                + "           ,[created_at])\n"
                + "     VALUES\n"
                + "           (?,?)";

        String sql2 = "INSERT INTO [dbo].[product]\n"
                + "           ([name]\n"
                + "           ,[price]\n"
                + "           ,[discount_id]\n"
                + "           ,[inventory_id]\n"
                + "           ,[category_id]\n"
                + "           ,[created_at]\n"
                + "           ,[img])\n"
                + "     VALUES\n"
                + "           (?,?,?,(select max(id) from product_inventory),?,?,?)";
        try {
            connection.setAutoCommit(false);
            PreparedStatement st = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st.setInt(1, quant);
            st.setString(2, p.getCreated_at());
            st2.setString(1, p.getName());
            st2.setFloat(2, p.getPrice());
            st2.setString(3, p.getD_id());
            st2.setString(4, p.getC_id());
            st2.setString(5, p.getCreated_at());
            st2.setString(6, p.getImg());
            st.executeUpdate();
            st2.executeUpdate();
            connection.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean edit(Product p) {
        String sql2 = "UPDATE [dbo].[product]\n"
                + "   SET [name] = ?\n"
                + "      ,[price] = ?\n"
                + "      ,[discount_id] = ?\n"
                + "      ,[category_id] = ?\n"
                + "      ,[modified_at] = ?\n";
        if (p.getImg() != null) {
            sql2 += ",[img] = ?\n";
        }
        sql2 += " WHERE id=" + p.getId();
        try {
            connection.setAutoCommit(false);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st2.setString(1, p.getName());
            st2.setFloat(2, p.getPrice());
            st2.setString(3, p.getD_id());
            st2.setString(4, p.getC_id());
            st2.setString(5, p.getModified_at());
            if (p.getImg() != null) {
                st2.setString(6, p.getImg());
            }
            st2.execute();
            connection.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean delete(Product p) {
        try {
            String sql = "UPDATE [dbo].[product]\n"
                    + "   SET [deleted_at] = ?\n"
                    + " WHERE id=?";
            String sql2 = "update product_inventory set deleted_at=? where id=?";
            connection.setAutoCommit(false);
            PreparedStatement st = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st.setString(1, p.getDeleted_at());
            st.setInt(2, p.getId());
            st2.setString(1, p.getDeleted_at());
            st2.setString(2, p.getI_id());
            st.executeUpdate();
            st2.executeUpdate();
            connection.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
