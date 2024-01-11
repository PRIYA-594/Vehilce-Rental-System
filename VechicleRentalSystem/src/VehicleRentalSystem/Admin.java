package VehicleRentalSystem;

import java.util.Scanner;

public class Admin {
	public void adminRole()throws Exception
	{
		System.out.println("\n\n-------WELCOME ADMIN------------\n\n");
		int ch=0;
		do
		{
			Scanner s = new Scanner(System.in);
			Report report = new Report();
			System.out.println("\n-----------------------------------------");
			System.out.println("\nSelect from the given options");
			System.out.println("\n\n1.Add Vehicle\n2.Modify Vehicle security deposit\n3.Modify Vehicle Available status \n4.Delete Vehicle\n5.View All Vehicle\n6.Search By Vehicle Name\n7.Due Vehicle\n8.Rented Vehicle\n9.Not Rented Vehicles\n10.Sort vehicle by name\n11.Sort vehicle by available count\n12.view report\n13.Exit");
			System.out.println("\nEnter Your Choice :");
			ch = s.nextInt();
			Vehicle vehicle = new Vehicle();
			System.out.println("*******************");
			switch(ch)
			{
			case 1:
				vehicle.addVehicle();
				break;
			case 2:
				vehicle.modifydeposit();
				break;
			case 3:
				vehicle.modifyAvailableStatus();
				break;
			case 4:
				vehicle.deleteVehicle();
				break;
			case 5:
				vehicle.viewAllVehicle();
				break;
			case 6:
				vehicle.searchVehicle();
				break;
			case 7:
				vehicle.viewDueVehicle();
				break;
			case 8:
				vehicle.viewRentedVehicle();
				break;
			case 9:
				vehicle.viewNonRentedVehicle();
				break;
			case 10:
				vehicle.sortVehicleByName();
				break;
			case 11:
				vehicle.sortVehicleByStatus();
				break;
			case 12:
				report.viewReport();
				break;
			case 13:
				System.out.println("Thank You");
				break;
			default:
				System.out.println("Your choice is incorrect");
			}
		}while(ch!=13);
	}
}
