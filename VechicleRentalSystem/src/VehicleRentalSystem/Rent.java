package VehicleRentalSystem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Rent{
	public Connection con = null;
	public Statement st = null;
	//public static HashMap<Integer,Integer> hmap = new HashMap<>();
	public void addToCart(int id)throws Exception
	{
		try {
			Scanner s = new Scanner(System.in);
			insertUserCart(id);
			String name = User.getUserName(id);
			System.out.println("\nWELCOME "+name);
			System.out.println("You can rent only one vehicle (one car /one bike");
			if(isEmptyCart(id) && isRented(id))
			{
				System.out.println("Sorry,you can't add on vehicle into the cart");
				System.out.println("You are allowed only to rent one car and one bike");
			}
			else
			{
				System.out.println("Enter the vehicle name or id of the vehicle");
				String str = s.nextLine();
				con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
				st = (Statement) con.createStatement();
				if(!isRented(id))
				{
					String query ="Select* from vehicle where v_id='"+str+"' or v_name='"+str+"'";
					ResultSet rs = st.executeQuery(query);
					if(rs.next())
					{
						int vid=rs.getInt(1);
						String inQ = "Update cart set vehicle_id = "+vid+" where user_id="+id+"";
						st.executeUpdate(inQ);
						//hmap.put(id, rs.getInt(1));	
					}
					else
					{
						//hmap.put(id, 0);
					}
				}
				else
				{
					System.out.println("Vehicle is not available for rent");
				}
			}
			//System.out.print(hmap);
		}
		catch(SQLIntegrityConstraintViolationException e)
		{
			System.out.println("You cannot add items to the cart");
			System.out.println("Because you can rent only one car and one bike\n\n");
		}
	}
	
	public boolean isRented(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query ="Select* from renter where user_id="+id+"";
		ResultSet rs = st.executeQuery(query);
		int c =0;
		if(rs.next())
		{
			c = rs.getInt(2);
		}
		else
		{
			c=0;
		}
		if(c!=0)
		{
			String query1 = "Select* from vehicle where v_id="+c+"";
			ResultSet rs1 = st.executeQuery(query1);
			if(rs1.next())
			{
				String status = rs1.getString("V_status_isAvailable");
				if(status.equals("NO"))
				{
					return true;
				}
			}
		}
		return false;
	}
	public void deleteCart(int id)throws Exception
	{
		System.out.println(" vehicle will be removed from the cart");
		//hmap.remove(id);
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query ="delete from cart where user_id="+id+"";
		st.executeUpdate(query);
		int v_id = getCartVehicleId(id);
		String query2 = "Update vehicle set V_status_isAvailable='YES' where v_id="+v_id+"";
		st.executeUpdate(query2);
	}

	public void rentVehicle(int id)throws Exception
	{
		Scanner s = new Scanner(System.in);
		System.out.println("if you want to rent the vehicle in the cart you must pay 30000 as security deposit \nif yes enter y or Y");
		char ch = s.next().charAt(0);
		
		System.out.println("Enter the renting date (yyyy-mm-dd)");
		String date = s.next();
        
		if(ch=='y'||ch=='Y')
		{
			int v_id=getCartVehicleId(id);
			String querySelect = "Insert into renter(vehicle_id,user_id,deposit,rent_date) values("+v_id+","+id+",30000,'"+date+"')";
			Db.UpdateQuery(querySelect);
			String query1 = "Update vehicle set V_status_isAvailable='NO' where v_id = "+v_id+";";
			Db.UpdateQuery(query1);
		}
	}
	public void returnVehicle(int id)throws Exception
	{
		Scanner s = new Scanner(System.in);
		int[] damage = new int[3];
		damage[0]=20;
		damage[1]=50;
		damage[2]=75;
		String rentdate = getRentDate(id);
		System.out.println("\nReturn the vehicle by fill the following information");
		System.out.println("\nEnter the return date (yyyy-mm-dd)");
		String returndate = s.next();
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate date1 = LocalDate.parse(rentdate, formatter);
	    LocalDate date2 = LocalDate.parse(returndate, formatter);

	    // Calculate the difference between the two dates
	    long daysDifference = ChronoUnit.DAYS.between(date1, date2);
	    
		System.out.println("\nhow many kms does the vehicle run");
		int kms = s.nextInt();
		
		System.out.println("\nEnter the damage level of the vehicle \n1.LOW\n2.MEDIUM\n3.HIGH");
		int damageLevel = s.nextInt();
		int v_id = getVehicleId(id);
		float total = 0.0f;
		float rent_amt = getRentalAmt(v_id);
		float deposit = getSecurityDeposit(v_id);
		
		String query = "Update vehicle set kms=kms+"+kms+",V_status_isAvailable='YES' where v_id="+v_id+"";
		
		Db.UpdateQuery(query);
		
		total=rent_amt+deposit;
		if((int)kms/(daysDifference)>500)
		{
			total *=0.15f;
		}
		
		float damagefee = total*(damage[damageLevel-1]/100);
		total+=damagefee;
		System.out.println("The total amount to be paid by you is "+total);
		System.out.println("Do you want to pay cash or reduce from caution security deposit");
		System.out.println("choose \n1.To reduce from caution security deposit\n2.Pay by cash");
		int choice = s.nextInt();
		String query3="update renter set return_date='"+returndate+"',kilometer="+kms+",tot_amt="+total+" where vehicle_id="+v_id+" ";
		Db.UpdateQuery(query3);
		if(choice==1)
		{
			if(total>30000)
			{
				
				System.out.println("The Final amount to be paid is greater than caution security deposit amount");
				System.out.println("You can pay cash for the remaining amount "+(total-30000));
			}
			else
			{
				Db.UpdateQuery(query3);
				System.out.println("The amount "+(30000-total)+"is returned to you");
			}
		}
		else
		{
			System.out.println("The caution security deposit 30000 is returned to you");
		}
		System.out.println("Do you want to rent/exchange another vehicle if yes enter y or Y");
		char chy = s.next().charAt(0);
		if(chy=='y'||chy=='Y')
		{
			addToCart(id);
			rentVehicle(id);
		}
		else
		{
			System.out.println("Thank you for renting");
		}
	}
	public int getVehicleId(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query ="Select* from renter where user_id="+id+"";
		ResultSet rs = st.executeQuery(query);
		int c =0;
		if(rs.next())
		{
			c=rs.getInt("vehicle_id");
		}
		else
		{
			c=0;
		}
		return c;
	}
	public float getSecurityDeposit(int v_id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query ="Select* from vehicle where v_id="+v_id+"";
		ResultSet rs = st.executeQuery(query);
		float c = 0.0f;
		if(rs.next())
		{
			c=rs.getFloat("secure_deposit");
		}
		return c;
	}
	public float getRentalAmt(int v_id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query = "select * from vehicle where v_id="+v_id+"";
		ResultSet rs = st.executeQuery(query);
		float rent = 0.0f;
		if(rs.next())
		{
			rent = rs.getFloat("rent_amt");
		}
		return rent;
	}
	public boolean isEmptyCart(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query = "select* from cart where user_id="+id+"";
		ResultSet rs = st.executeQuery(query);
		if(rs.next())
		{
			int u_id = rs.getInt("user_id");
			int v_id=rs.getInt("vehicle_id");
			if(u_id==id)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}
	
	public String getRentDate(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query = "select * from renter where user_id="+id+"";
		ResultSet rs = st.executeQuery(query);
		String date="";
		if(rs.next())
		{
			date = rs.getString("rent_date");
		}
		return date;
	}
	
	public void insertUserCart(int id)throws Exception
	{
		
		String qu = "insert into cart(user_id)values("+id+")";
		Db.UpdateQuery(qu);
	}
	public int getCartVehicleId(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query ="Select* from cart where user_id="+id+"";
		ResultSet rs = st.executeQuery(query);
		int c =0;
		if(rs.next())
		{
			c=rs.getInt("vehicle_id");
		}
		else
		{
			c=0;
		}
		return c;
	}
	public void viewCart(int id)throws Exception
	{
		String qu = "select * from cart where user_id="+id+";";
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		ResultSet rs = st.executeQuery(qu);
		if(rs.next())
		{
			int v_id = rs.getInt("vehicle_id");
			System.out.println("vehicle_id = "+v_id);
			System.out.println("user id = "+rs.getInt("user_id"));
			String queryin = "select * from vehicle where v_id="+v_id+"";
			Vehicle vh = new Vehicle();
			vh.display(queryin);
			
		}
	}
}
