package com.company;
import java.sql.*;
import java.util.*;
public class Main {
      int points=0;
    public static void main(String[] args) throws  SQLException{
        int Bill= (int)(Math.random() * 3000) + 1001;
        System.out.println("Total bill : "+Bill);

        int points=Bill/100;
        Scanner sc=new Scanner(System.in);
        System.out.println("Your points : "+points);
        System.out.println("You already have an Account [y/n]");
        String opt=sc.nextLine();
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/AccountDetails", "root", "safeek2003");
           if (opt.charAt(0) == 'y' || opt.charAt(0) == 'Y')
               update(con,sc,points,Bill);
           else
               create(con,sc,points,Bill);
       }
       catch(ClassNotFoundException CE) {
           System.out.println(CE);
       }
	// write your code here
    }
    public static void update(Connection con,Scanner sc,int points,int bill) throws SQLException {
        int opoints = 0;
        System.out.println("Enter your mobile number ");
        String Number = sc.nextLine();
        PreparedStatement qe1 = con.prepareStatement("select * from CustomerDetails where Number=?");
        qe1.setString(1, Number);
        ResultSet ans = qe1.executeQuery();
        if (ans.next() == false) {
            System.out.println("This number not exist please create the account press (1) or retype the number correctly press(2)");
            int opt=sc.nextInt();
            if(opt==1)
            create(con, sc, points, bill);
            else{
                sc.nextLine();
                update(con, sc, points, bill);
            }
        }
        else {
            opoints = ans.getInt(4);
            PreparedStatement qe = con.prepareStatement("UPDATE CustomerDetails SET Points = ? WHERE Number = ?");
            points += opoints;
            qe.setInt(1, points);
            qe.setString(2, Number);
            qe.executeUpdate();
            System.out.println("Total available points " + points);
            if (points > 49) {
                System.out.println("If you wants to use your points now press [y/n]");
                String opt = sc.nextLine();
                if (opt.charAt(0) == 'y' || opt.charAt(0) == 'Y') {
                    bill -= points;
                    PreparedStatement qe2 = con.prepareStatement("UPDATE CustomerDetails SET Points = 0 WHERE Number = ?");
                    qe2.setString(1, Number);
                    qe2.executeUpdate();
                    System.out.println("Total payable amount : " + bill);
                    System.out.println("Total available points : 0");
                }
            }
        }
    }
    public static void create(Connection con,Scanner sc,int points,int Bill) throws SQLException{

        System.out.println("Enter your name :");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.println("Enter your phone Number");
        String num = sc.nextLine();

        PreparedStatement qe1=con.prepareStatement("select * from CustomerDetails where Number=?");
        qe1.setString(1, num);
        ResultSet ans=qe1.executeQuery();
        if (ans.next() == false) {
            System.out.println("name : " + name + "\n" + "num : " + num + "\n" + " Above details are correct press (1)" + "\n" + " else type (2) for retype ");
            int resignup = sc.nextInt();
            if (resignup == 1) {
                PreparedStatement qe = con.prepareStatement("insert into CustomerDetails(Name,Number,Points) values(?,?,?)");
                qe.setString(1, name);
                qe.setString(2, num);
                qe.setInt(3, points);
                qe.executeUpdate();
                System.out.println("Account created and Total available points : "+points);

            } else {
                create(con, sc, points,Bill);
            }
        }
        else {
            System.out.println("This mobile number already exist please type the number again");
            update(con,sc,points,Bill);
        }
    }
}
