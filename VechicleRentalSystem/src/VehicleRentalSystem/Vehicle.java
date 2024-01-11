package VehicleRentalSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;



public class Vehicle {

	static Connection con = null;
	static Statement st = null;
	
    private int id;
    private String name;
    private String numberPlate;
    private int vehicleTypeId;
    private float securityDeposit;
    private float rent_amt;
    private int kilometers;
    private String v_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public float getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(float securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    
    public float getRentAmt() {
        return rent_amt;
    }

    public void setRentAmt(float rent_amt) {
        this.rent_amt = rent_amt;
    }
    
    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }
    
    public String getVehicleStatus() {
        return v_status;
    }

    public void setVehicleStatus(String v_status) {
        this.v_status = v_status;
    }
    
    public void addVehicle() throws Exception {
        Scanner s = new Scanner(System.in);
        System.out.println("\nEnter the Vehicle Details to add into the database");
        System.out.println("\nEnter the name(includes model&brand) :");
        setName(s.nextLine());
        System.out.println("\nEnter the Number Plate number :");
        setNumberPlate(s.nextLine());
        System.out.println("\nChoose the type of vehicle (1.car and 2.bike) :");
        setVehicleTypeId(s.nextInt());
        System.out.println("\nEnter the security Deposit");
        float sdep = 0.0f;
        if (getVehicleTypeId() == 1) {
            System.out.println("For car, minimum 10000");
            sdep = s.nextFloat();
        } else if (getVehicleTypeId() == 2) {
            System.out.println("For bike, minimum 3000");
            sdep = s.nextFloat();
        }
        System.out.println("\nEnter the rental price");
        setRentAmt(s.nextFloat());
        System.out.println("Enter the kilometers run :");
        setKilometers(s.nextInt());
        System.out.println("Enter the status of the vehicle");
        setVehicleStatus(s.next());
        String query = "insert into vehicle(v_name,no_plate,vtype_id,secure_deposit,rent_amt,kms,V_status_isAvailable) values('"+getName()+"','" +getNumberPlate()+ "','" + getVehicleTypeId() + "'," + sdep + ","+getRentAmt()+"," + getKilometers() + ",'"+getVehicleStatus()+"');";
        Db.UpdateQuery(query);
    }
	
	public void modifydeposit()throws Exception
	{
		Scanner s = new Scanner(System.in);
		
		System.out.println("\nEnter vehicle name or number plate whose security deposit needs to be modified");
		String name = s.nextLine();
		System.out.println("Enter the value to be replaced");
		float sdep = s.nextFloat();
		String query = "UPDATE vehicle set secure_deposit="+sdep+" where v_name='"+name+"' OR no_plate='"+name+"';";
		int r =Db.UpdateQuery(query);
		if(r==1)
		{
			System.out.println("Successfully updated");
		}
		else
		{
			System.out.println("Given details might be incorrect ");
		}
	}
	public void modifyAvailableStatus()throws Exception
	{
		Scanner s = new Scanner(System.in);
		System.out.println("\nTo modify the available count of vehicles");
		System.out.println("Enter the vehicle name or number plate number :");
		String name = s.nextLine();
		System.out.println("Enter the vehicle available status if availabe enter 1 otherwise enter 2:");
		int count = s.nextInt();
		String query="";
		if(count==1)
		{
			query = "update vehicle V_status_isAvailable='YES' where v_name='"+name+"' or no_plate='"+name+"'";
		}
		else if(count==2)
		{
			query = "update vehicle V_status_isAvailable='NO' where v_name='"+name+"' or no_plate='"+name+"'";
		}
		Db.UpdateQuery(query);
	}
	public void deleteVehicle()throws Exception
	{
		Scanner s = new Scanner(System.in);
		System.out.println("\n------------DELETE VEHICLE-----------------------------");
		System.out.println("\nSelect from the given options to delete");
		System.out.println("\nDelete By\n1.name or number plate\n2.vehicle id");
		int ch = s.nextInt();
		String query = "";
		int f =0;
		int r=0;
		do
		{
			if(ch==1)
			{
				System.out.println("\nEnter the name of the vehicle record to be deleted");
				String name = getStringVal();
				int v_id = getVehicleId(name);
				query="delete from renter where vehicle_id="+v_id+"";
				r=Db.UpdateQuery(query);
	
				query = "delete from vehicle where v_name='"+name+"' or  no_plate='"+name+"'";
				r=Db.UpdateQuery(query);
			}
			else if(ch==2)
			{
				System.out.println("\nEnter the vehicle id of the vehicle record to be deleted");
				int vid = s.nextInt();
				query="delete from renter where vehicle_id="+vid+"";
				r=Db.UpdateQuery(query);
				
				query = "delete from vehicle where v_id="+vid+"";
				r=Db.UpdateQuery(query);
			}
			else
			{
				f=1;
				System.out.println("Enter the valid choice");
			}
		}while(f==1);
		if(r==1)
		{
			System.out.println("Deleted Successfully");
		}
		else
		{
			System.out.println("The values given might be incorrect");
		}
	}
	
	public void viewAllVehicle()throws Exception
	{
		String query ="Select* from vehicle";
		display(query);
	}
	
	public void sortVehicleByStatus()throws Exception
	{
		String query ="Select* from vehicle order by V_status_isAvailable";
		display(query);
	}
	
	public void sortVehicleByName()throws Exception
	{
		String query ="Select* from vehicle order by v_name";
		display(query);
	}
	
	
	public void searchVehicle()throws Exception
	{
		System.out.println("Enter name or number plate of the vehicle to search for its details");
		String name = getStringVal();
		String query ="Select* from vehicle where v_name='"+name+"' or no_plate='"+name+"' order by v_name";
		display(query);
	}
	
	public void searchVehicleName()throws Exception
	{
		System.out.println("Enter name of the vehicle to search for its details");
		String name = getStringVal();
		String filter = getStringVal();
		filter=filter.toLowerCase();
		int id =0;
		if(filter.equals("car"))
		{
			id=1;
		}
		else if(filter.equals("bike"))
		{
			id=2;
		}
		String query ="Select* from vehicle where v_name='"+name+"' or vtype_id="+id+" order by v_name";
		display(query);
	}
	
	public String getVehicleType(int id)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query ="Select* from config_vehicletype where vty_id="+id+"";
		ResultSet rs = st.executeQuery(query);
		String st="";
		if(rs.next())
		{
			st = rs.getString(2);
		}
		return st;
	}
	
	public static String getStringVal()
	{
		Scanner s = new Scanner(System.in);
		String str = s.nextLine();
		return str;
	}
	
	public void display(String query)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next())
		{
			int id = rs.getInt(1);
			String vname = rs.getString(2);
			String numberplate = rs.getString("no_plate");
			int v_typeId = rs.getInt("vtype_id");
			String v_type = getVehicleType(v_typeId);
			float securityDeposit = rs.getFloat(5);
			int rent_amt = rs.getInt(6);
			int kms = rs.getInt(7);
			String status = rs.getString(8);
			System.out.println("Vechicle id : "+id+"\nVehicle name : "+vname+"\nNumber plate/Register Number : "+numberplate+"\nVehicle type : "+v_type+"\nSecurity Deposit : "+securityDeposit+"\nRental Price : "+rent_amt+"\nKilo meter : "+kms+"\nVehicle Status : "+status);
			System.out.println("--------------------------------------");
		}
	}
	
	
	public void viewVehicleDetails()throws Exception
	{
		System.out.println("List of car available for rent");
		String query ="Select v.* from vehicle v join config_vehicletype t on v.vtype_id=t.vty_id where v_type='car' and kms<3000";
		display(query);
		System.out.println("--------------------------------");
		System.out.println("List of bike available for rent");
		String query1 ="Select v.* from vehicle v join config_vehicletype t on v.vtype_id=t.vty_id where v_type='bike' and v.kms<1500";
		display(query1);
			
	}
	
	
	public int getVehicleId(String name)throws Exception
	{
		con = (Connection) DriverManager.getConnection(Db.url, "root", Db.passwordRoot);
		st = (Statement) con.createStatement();
		String query = "select* from vehicle where v_name ='"+name+"' or  no_plate='"+name+"'";
		ResultSet rs = st.executeQuery(query);
		int id =0;
		while(rs.next())
		{
			id=rs.getInt(1);
		}
		return id;
	}
	
	
	public void viewDueVehicle()throws Exception
	{
		System.out.println("List of service Due cars");
		String query ="Select v.* from vehicle v join config_vehicletype t on v.vtype_id=t.vty_id where v_type='car' and kms>3000";
		display(query);
		System.out.println("List of service Due bikes");
		String query1 ="Select v.* from vehicle v join config_vehicletype t on v.vtype_id=t.vty_id where v_type='bike' and v.kms>1500";
		display(query1);
	}
	
	
	public void viewRentedVehicle()throws Exception
	{
		System.out.println("List of rented vehicles\n**************************");
		String query = "select * from vehicle where V_status_isAvailable='NO';";
		display(query);

	}
	
	
	public void viewNonRentedVehicle()throws Exception
	{
		System.out.println("List of non rented vehicles\\n**************************");
		String query = "select * from vehicle where V_status_isAvailable='YES';";
		display(query);
	}
	
	
	public void sortByRentalPrice()throws Exception
	{
		System.out.println("List of vehicles sort by price");
		String query = "select * from vehicle order by rent_amt;";
		display(query);
	}
}
