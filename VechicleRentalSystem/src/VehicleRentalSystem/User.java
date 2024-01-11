package VehicleRentalSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
public class User{
	static Connection con = null;
	static Statement st = null;
	public static void userRole(int s_id)throws Exception
	{
		System.out.println("\n--------------WELCOME USER------------------\n");
		String username = getUserName(s_id);
		System.out.println("\n-----"+username+"-------\n");
		Vehicle vh = new Vehicle();
		Rent re = new Rent();
		if(!isInserted(s_id))
		{
			insertContactUser(s_id);
		}
		int ch=0;
		do
		{
			Scanner s = new Scanner(System.in);
			System.out.println("-----------------------------------------");
			System.out.println("\n\n1.Vehicle details\n2.Add to cart\n3.View vehicle in Cart\n4.Delete vehicle in cart\n5.Rent vehicle in the cart\n6.Return rented vehicle\n7.Exit");
			System.out.println("\nEnter Your Choice :");
			ch = s.nextInt();
			System.out.println("*******************");
			switch(ch)
			{
			case 1:
				vh.viewVehicleDetails();
				break;
			case 2:
				re.addToCart(s_id);
				break;
			case 3:
				re.viewCart(s_id);
				break;
			case 4:
				re.deleteCart(s_id);
				break;
			case 5:
				re.rentVehicle(s_id);
				break;
			case 6:
				re.returnVehicle(s_id);
			case 7:
				System.out.println("Thank You");
				break;
			default:
				System.out.println("Your choice is incorrect");
			}
		}while(ch!=7);
	}
	public static String getUserName(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		String querySelect = "select* from user where u_id='"+id+"';";
		st = (Statement) con.createStatement();
		ResultSet rs = st.executeQuery(querySelect);
		String name ="";
		if(!rs.next())
		{
			name="";
		}
		else
		{
			name=rs.getString("name");
		}
		return name;
	}
	public static boolean insertContactUser(int s_id)throws Exception
	{
		Scanner s = new Scanner(System.in);
		System.out.println("\nEnter your personal_details details mentioned below...");
		System.out.println("\nEnter your Age :");
		int age = s.nextInt();
		System.out.println("\nChoose Gender :\n\t1. Male \n\t2. Female \n\t3. Other");
		int gen = s.nextInt();
		System.out.println("\nEnter your address :");
		String add = getAddress();
		System.out.println("\nEnter your Phone Number :");
		String phno = s.next();
		
		String queryContact = "insert into personal_details(u_id,age,gender_id,phone_no,address) values("+s_id+","+age+",'"+gen+"','"+phno+"','"+add+"')";
		int r = Db.UpdateQuery(queryContact);
		if(r==1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean isInserted(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String queryContactSel = "select * from personal_details where s_id="+id+"";
		ResultSet rs = st.executeQuery(queryContactSel);
		if(!rs.next())
		{
			return false;
		}
		return true;
	}
	public static String getAddress()
	{
		Scanner s = new Scanner(System.in);
		String str = s.nextLine();
		return str;
	}
	
}
