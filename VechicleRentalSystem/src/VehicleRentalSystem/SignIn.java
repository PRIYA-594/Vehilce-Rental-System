package VehicleRentalSystem;

import java.nio.charset.StandardCharsets;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import VehicleRentalSystem.Admin;
import VehicleRentalSystem.User;
import VehicleRentalSystem.Db;

public class SignIn {
	static Connection con = null;
	static Statement st = null;
	static ArrayList<String> role = new ArrayList<>();
	public static void main(String args[])throws Exception
	{
		Scanner s = new Scanner(System.in);
		Db.getConnection();
		System.out.println("\nWELCOME TO VEHICLE RENTAL SYSTEM...");
		int syin = 0;
		int yt =0;
		do
		{
			System.out.println("-----------------------------------------");
			System.out.println("\n\n1.LogIN as ADMIN\n2.LogIN as USER\n3.Sign-UP\n4.EXIT");
			System.out.println("\nEnter Your Choice :");
			syin = s.nextInt();
			System.out.println("*******************");
			switch(syin)
			{
			case 1:
				if(loginAdmin())
				{
					Admin admin = new Admin();
					admin.adminRole();
				}
				else
				{
					System.out.println("\nYour Credentials are incorrect");
				}
				break;
			case 2:
				int id=loginUser();
				if(id!=0)
				{
					User.userRole(id);
				}
				else
				{
					System.out.println("\nYour Credentials are incorrect");
				}
				break;
			case 3:
				if(signUP())
				{
					System.out.println("Successfully Signed up\n");
				}
				else
				{
					System.out.println("You were not signed up!!\n");
				}
				break;
			case 4:
				System.out.println("Thank You");
				break;
			default:
				yt=0;
			}
		}while(yt!=1 && syin!=4);
	}
	public  static boolean signUP()
	{
		try {
			Scanner s = new Scanner(System.in);
			System.out.println("To Sign up as admin enter 1");
			System.out.println("To Sign up as user enter 2");
			int r = s.nextInt();
			if(r==1)
			{
				System.out.println("You need to enter the passkey to sign up as admin");
				String passKey=s.next();
				if(passKey.equals("MostImportant"))
				{
					return signDetail(r);
				}
				else
				{
					System.out.println("You are not allowed to sign up as admin");
				}
			}
			else if(r==2)
			{
				return signDetail(r);
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception"+e);
		}
		return true;
	}
	public static boolean signDetail(int r)
	{
		try {
			Scanner s = new Scanner(System.in);
			ArrayList<String> al = new ArrayList<>();
			al.add("admin");
			al.add("user");
			String role = al.get(r-1);
			
			System.out.println("\nEnter Your Name");
			String name = getValue();
			
			System.out.println("\nEnter Your Email");
			String email = s.next();
			
			System.out.println("\n->At least one lowercase letter\n->At least one uppercase letter \n->At least one digit\n->At least one special character \n->Minimum length of 8 characters ");
			System.out.println("\nEnter the Password");
			String password = s.next();
			
			System.out.println("\nEnter Confirm Password");
			String confirmPassword = s.next();
			
			if(isExistEmail(email,role))
			{
				if(isValidEmail(email)){
					String emailF = email;
					
					if(isValidPassword(password,confirmPassword))
					{
						String passwordF=password;
						String passEncrypt = passwordEncrpty(passwordF);
						String queryInsert = "insert into "+role+"(name,email,password) values('"+name+"','"+emailF+"','"+passEncrypt+"');";
						Db.UpdateQuery(queryInsert);
						return true;
					}
					else
					{
						System.out.println("Password is not Valid");
					}
				}
				else
				{
					System.out.println("Email is not Valid");
				}
			}
			else
			{
				System.out.println("Email or password is not valid");
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR!!!"+e);
		}
		return false;
	}
	
	public static boolean loginAdmin()
	{
		try {
			Scanner s = new Scanner(System.in);
			System.out.println("WELCOME TO ADMIN LOGIN");
			System.out.println("----------------------");
			System.out.println("\nEnter username / emailId :");
			String val = s.nextLine();
			System.out.println("\nEnter Password");
			String pass = s.nextLine();
			
				if(isValidAdmin(val))
				{
					int id = getAdminId(val);
					if(correctPassword(pass,id,"admin"))
					{
						return true;
					}
					else
					{
						System.out.println("Password is incorrect");
					}
				}
				else
				{
					System.out.println("Inavalid username or password");
				}
		}
		catch(Exception e)
		{
			System.out.println("Exception->"+e);
		}
		return false;
	}
	public static int loginUser()
	{
		try {
			Scanner s = new Scanner(System.in);
			System.out.println("WELCOME TO USER LOGIN");
			System.out.println("----------------------");
			System.out.println("\nEnter username / emailId :");
			String val = s.nextLine();
			System.out.println("\nEnter Password");
			String pass = s.nextLine();
			
				if(isValidUser(val))
				{
					int id = getUserId(val);
					if(correctPassword(pass,id,"user"))
					{
						return id;
					}
					else
					{
						System.out.println("Password is incorrect");
					}
				}
				else
				{
					System.out.println("Inavalid username or password");
				}
			
		}
		catch(Exception e)
		{
			System.out.println("Exception->"+e);
		}
		return 0;
	}
	
	public static int getAdminId(String val)throws SQLException
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		String querySelect = "select* from admin where name ='"+val+"' OR email='"+val+"';";
		st = (Statement) con.createStatement();
		ResultSet rs = st.executeQuery(querySelect);
		int id = 0;
		if(!rs.next())
		{
			id=0;
		}
		else
		{
			id=rs.getInt("_id");
		}
		return id;
	}
	
	public static int getUserId(String val)throws SQLException
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		String querySelect = "select* from user where name ='"+val+"' OR email='"+val+"';";
		st = (Statement) con.createStatement();
		ResultSet rs = st.executeQuery(querySelect);
		int id = 0;
		if(!rs.next())
		{
			id=0;
		}
		else
		{
			id=rs.getInt("u_id");
		}
		return id;
	}
	
	public static boolean isValidAdmin(String val)throws SQLException
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String querySelect = "select* from admin where name ='"+val+"' or email='"+val+"'";
		ResultSet rs =st.executeQuery(querySelect);
		return (rs.next());
	}
	
	public static boolean isValidUser(String val)throws SQLException
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String querySelect = "select* from user where name ='"+val+"' or email='"+val+"'";
		ResultSet rs =st.executeQuery(querySelect);
		return (rs.next());
	}
	
	
	
	
	public static boolean isExistEmail(String email,String role)throws Exception
	{
		
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query = "select email from "+role+" where email = '"+email+"';";
		ResultSet res = st.executeQuery(query);
		return !(res.next());
	}
	
	
	
	public static String passwordEncrpty(String a) throws Exception
	{
		String encrypt = "";
		for(int i=0;i<a.length();i++)
		{
			encrypt +=(char)(a.charAt(i)+'5');
		}
		return encrypt;
	}
	public static String passwordDecrpty(String str) throws Exception
	{
		String decrypt = "";
		for(int i=0;i<str.length();i++)
		{
			decrypt +=(char)(str.charAt(i)-'5');
		}
		return decrypt;
	}
	
	public static boolean isValidEmail(String email)
	{
	    String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	    Pattern pattern = Pattern.compile(EMAIL_REGEX);
	    //System.out.println(pattern.matcher(email).matches());
	    return pattern.matcher(email).matches();   
	}
	
	public static boolean isValidPassword(String a,String b)
	{
		if(a.equals(b))
		{
			String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
			Pattern pattern = Pattern.compile(regexPattern);
		    return(pattern.matcher(a).matches());
		}
		else
		{
			System.out.println("Password and Confirm password must be same");
			return false;
		}
	}
	
	public static boolean correctPassword(String str,int id,String role)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		String querySelect = "select password from "+role+" where _id ='"+id+"';";
		st = (Statement) con.createStatement();
		ResultSet rs = st.executeQuery(querySelect);
		String password ="";
		if(rs.next())
		{
			password=rs.getString("password");
		}
		else
		{
			return false;
		}
		//System.out.println(password);
		String decryptPassword = passwordDecrpty(password);
		//System.out.println(decryptPassword);
		return (decryptPassword.equals(str));
	}
	public static String getValue()
	{
		Scanner s = new Scanner(System.in);
		String str = s.nextLine();
		return str;
	}
}
