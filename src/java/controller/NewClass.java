/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import Model.Cart_items;
import Model.MD5;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author askm4
 */
public class NewClass {
    
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(LocalDate.now().format(formatter));
        ArrayList<Cart_items> list = new ArrayList();
        Cart_items a = new Cart_items();
        a.setCreated_at("hehe13");
        Cart_items b = new Cart_items();
        b.setCreated_at("hehe2");
        Cart_items c = new Cart_items();
        c.setCreated_at("hehe1");
        list.add(a);
        list.add(b);
        list.add(c);
        int kq = 0;
    
        ArrayList<Cart_items> listkq = (ArrayList<Cart_items>) list.stream().filter((Cart_items Cart_items) -> {
            return Cart_items.getQuan() > 4;
        }).collect(Collectors.toList());
        

           
                }
}
