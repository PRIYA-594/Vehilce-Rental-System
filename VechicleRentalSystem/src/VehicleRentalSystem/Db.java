package VehicleRentalSystem;

import java.sql.*;
import java.util.Scanner;

public class Db {
	static String passwordRoot;
	static String url;
	static Connection con = null;
	public static int UpdateQuery(String query)throws Exception
	{
		con = DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		Statement st = (Statement) con.createStatement();
        return st.executeUpdate(query);
	}
	static void getConnection() throws SQLException
	{
		Scanner s = new Scanner(System.in);
		int f=0;
		do
		{
			System.out.println("Enter Your Root Password");
			passwordRoot = s.nextLine();
			url = "jdbc:mysql://localhost:3306/VechicleRentalSystem";
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				f=0;
				con = (Connection) DriverManager.getConnection(url,"root",passwordRoot);
			}
			catch(Exception e)
			{
				f=1;
				System.out.println("ERROR!"+e);
			}
			if(f==0)
			{
				System.out.println("DataBase Connected\n");
			}
			else
			{
				System.out.println("\nYOUR PASSWORD IS WRONG");
			}
		}while(f==1);
		Statement stmt = (Statement) con.createStatement();
		
		
		
		/*String veTable = "create table config_vehicletype(vty_id int primary key,v_type varchar(10));";
		stmt.executeUpdate(veTable);
		
		String v1 = "insert into config_vehicletype values(1,'car')";
		String v2 = "insert into config_vehicletype values(2,'bike')";
		stmt.executeUpdate(v1);
		stmt.executeUpdate(v2);*/
		
		
		/*String vehicleTable ="create table vehicle(v_id int primary key auto_increment,v_name varchar(100) not null,no_plate varchar(20) unique not null,vtype_id int not null,FOREIGN KEY (vtype_id) REFERENCES  config_vehicletype(vty_id),secure_deposit float not null,rent_amt float not null,kms int not null,V_status_isAvailable varchar(10) not null);";
		stmt.executeUpdate(vehicleTable);
	
		String userTable ="create table user(u_id int primary key auto_increment,name varChar(100),email varchar(40),password varchar(100));";
		stmt.executeUpdate(userTable);
		
		String adminTable ="create table admin(_id int primary key auto_increment,name varChar(100),email varchar(40),password varchar(100));";
		stmt.executeUpdate(adminTable);
		
		String gen = "create table Config_gender(gen_id int primary key,gender varchar(10));";
		stmt.executeUpdate(gen);
		
		String g1 = "insert into Config_gender values(1,'male')";
		String g2 = "insert into Config_gender values(2,'female')";
		String g3 = "insert into Config_gender values(3,'other')";
		stmt.executeUpdate(g1);
		stmt.executeUpdate(g2);
		stmt.executeUpdate(g3);
		
		String contactTable ="create table personal_details(u_id int primary key,age int not null,gender_id int not null,Licence_no varchar(10) not null,phone_no varchar(13) not null,address varchar(70) not null,FOREIGN KEY (u_id) REFERENCES user(u_id),foreign key(gender_id) references Config_gender(gen_id));";
		stmt.executeUpdate(contactTable);*/
		
		/*String renterTable = "create table renter(renter_id int primary key auto_increment,vehicle_id int not null,foreign key (vehicle_id) references vehicle(v_id),user_id int,foreign key (user_id) references user(u_id),deposit float not null,rent_date date,return_date date,Kilometer int,damage float,tot_amt float)";
		stmt.executeUpdate(renterTable);
		
		
		String cartTable = "create table cart(user_id int primary key,foreign key (user_id) references user(u_id),vehicle_id int,foreign key (vehicle_id) references vehicle(v_id))";
		stmt.executeUpdate(cartTable);*/
		
	}
}
