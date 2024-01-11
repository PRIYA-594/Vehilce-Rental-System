package VehicleRentalSystem;

import java.util.Scanner;

public class Report {
	public void viewReport()throws Exception
	{
		Scanner s = new Scanner(System.in);
		System.out.println("Report------");
		Vehicle vh = new Vehicle();
		//System.out.println("\n1.view due of service vehicles\n2.sort vehicle by rental price\n3.Search by vehicle name filter car or bike \n4.Rented vehicle and Non-rented vehicle\n5.Exit");
		int ch = 0;
		do
		{
			System.out.println("\n1.view due of service vehicles\n2.sort vehicle by rental price\n3.Search by vehicle name filter car or bike \n4.Rented vehicle and Non-rented vehicle\n5.Exit");
			ch = s.nextInt();
			switch(ch)
			{
			case 1:
				vh.viewDueVehicle();
				break;
			case 2:
				vh.sortByRentalPrice();
				break;
			case 3:
				vh.searchVehicleName();
				break;
			case 4:
				vh.viewRentedVehicle();
				vh.viewNonRentedVehicle();
				break;
			case 5:
				System.out.println("Thank You");
				break;
			default:
				System.out.println("Enter the correct choice");
			}
		}while(ch!=5);
	}
}
