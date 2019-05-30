package app;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		Menu
 * Description:	The class a menu and is used to interact with the user. 
 * Original Author:		Rodney Cocker  
 * Modified by:			Jesse Osrecak
 */
public class Menu
{
	private Scanner console = new Scanner(System.in);
	private MiRideApplication application = new MiRideApplication();
	// Allows me to turn validation on/off for testing business logic in the
	// classes.
	private boolean testingWithValidation = true;

	/*
	 * Runs the menu in a loop until the user decides to exit the system.
	 */
	public void run()
	{
		try
		{
			application.loadCars();
			//console.nextLine();
		}
		catch(Exception ex)
		{
			System.out.println("Error:" + ex.getMessage());
		}

		
		final int MENU_ITEM_LENGTH = 2;
		String input;
		String choice = "";
		do
		{
			printMenu();

			input = console.nextLine().toUpperCase();

			if (input.length() != MENU_ITEM_LENGTH)
			{
				System.out.println("Error - selection must be two characters!");
			} else
			{
				System.out.println();

				switch (input)
				{
				case "CC":
					createCar();
					break;
				case "BC":
					book();
					break;
				case "CB":
					completeBooking();
					break;
				case "DA":
					System.out.println(application.displayAllBookings(getCarType(), sortType()));
					break;
				case "SS":
					System.out.print("Enter Registration Number: ");
					System.out.println(application.displaySpecificCar(console.nextLine()));
					break;
				case "SA":
					System.out.println(searchAvailible());
					break;
				case "SD":
					application.seedData();
					break;
				case "SP":
					System.out.println("Saving Data");
					try
					{
						application.saveData();
					}
					catch(IOException ex)
					{
						System.out.println("Error" + ex.getMessage());
					}
					break;
				case "EX":
					System.out.println("Exiting Program ... Goodbye!");
					choice = "EX";
					break;
				default:
					System.out.println("Error, invalid option selected!");
					System.out.println("Please try Again...");
				}
			}

		} while (choice != "EX");
	}

	/*
	 * Creates cars for use in the system available or booking.
	 */
	private void createCar()
	{
		String id = "", make, model, driverName;
		int numPassengers = 0;
		String type;
		
		System.out.print("Enter registration number: ");
		id = promptUserForRegNo();
		if (id.length() != 0)
		{
			// Get details required for creating a car.
			make = askMake();
			model = askModel();
			driverName = askDriversName();
			numPassengers = askPassengerCapacity();

			boolean result = application.checkIfCarExists(id);
			
			System.out.println("Enter Service Type (SD/SS)");
			type = promptUserForCarType();
			
			if (!result && type.equals("SD"))
			{
				String carRegistrationNumber = application.createCar(id, make, model, driverName, numPassengers);
				System.out.println(carRegistrationNumber);
			} 
			else if(!result && type.equals("SS"))
			{
				System.out.println("Enter a booking Fee (minimum $3.00)");
				double fee = getBookingFee();
				System.out.println("Enter a list of refreshments seperated by a comma ','");
				String refreshmentsList = console.nextLine();

				String carRegistrationNumber =  application.createSilverCar(id, make, model, driverName, numPassengers, fee, refreshmentsList);
				System.out.println(carRegistrationNumber);
			}
			else
			{
				System.out.println("Error - Already exists in the system");
			}
			
			
		}
	}
	/*
	 * Asking Mehods
	 */
	private String askMake()
	{
		String make;
		System.out.print("Enter Make: ");
		make = console.nextLine();
		if(make.equals(""))
		{
			System.out.println("No Make Entered");
			make = askMake();
		}
		return make;
	}
	
	private String askModel()
	{
		String model;
		System.out.print("Enter Model: ");
		model = console.nextLine();
		if(model.equals(""))
		{
			System.out.println("No Model Entered");
			model = askMake();
		}
		return model;
	}
	
	private String askDriversName()
	{
		String name;
		System.out.print("Enter Driver Name: ");
		name = console.nextLine();
		if(name.equals(""))
		{
			System.out.println("No Driver Name Entered");
			name = askMake();
		}
		return name;
	}
	
	private int askNumPassengers()
	{
		int passengerCapacity;
		System.out.print("Enter number of passengers: ");
		try
		{
			passengerCapacity = console.nextInt();
			console.nextLine();
			if(1 > passengerCapacity || passengerCapacity > 9)
			{
				throw new NumberFormatException("The number of passengers is invalid please enter a valid number of Passengers.\n"
						+ "The passenger capacity must be less than 10 and greater than 0");
			}
		}
		catch(NumberFormatException ex)
		{
			System.out.println(ex.getMessage());
			
			passengerCapacity = askNumPassengers();
		}
		catch(InputMismatchException ex) 
		{
			console.nextLine();
			System.out.println("The expected passenger capacicty is a nummber\n"
					+ "Please enter a number");
			passengerCapacity = askNumPassengers();
		}
		return passengerCapacity;
	}

	private int askPassengerCapacity()
	{
		int passengerCapacity;
		try
		{
			System.out.println("Please enter the nummber of passengers");
			passengerCapacity = console.nextInt();
			console.hasNextLine();
		}
		catch(InputMismatchException ex)
		{
			System.out.println("Error: please input passenger capacity in number form");
			passengerCapacity = askPassengerCapacity();
		}
		return passengerCapacity;
	}
	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private boolean book()
	{
		System.out.println("Enter date car required: ");
		String dateEntered = askDate();
		int day = Integer.parseInt(dateEntered.substring(0, 2));
		int month = Integer.parseInt(dateEntered.substring(3, 5));
		int year = Integer.parseInt(dateEntered.substring(6));
		DateTime dateRequired = new DateTime(day, month, year);
		
		if(!DateUtilities.dateIsNotInPast(dateRequired) || !DateUtilities.dateIsNotMoreThan7Days(dateRequired))
		{
			System.out.println("Date is invalid, must be within the coming week.");
			return false;
		}
		
		
		String[] availableCars = application.book(dateRequired);
		for (int i = 0; i < availableCars.length; i++)
		{
			System.out.println(availableCars[i]);
		}
		if (availableCars.length != 0)
		{
			System.out.println("Please enter a number from the list:");
			int itemSelected = Integer.parseInt(console.nextLine());
			
			String regNo = availableCars[itemSelected - 1];
			regNo = regNo.substring(regNo.length() - 6);
			System.out.println("Please enter your first name:");
			String firstName = console.nextLine();
			System.out.println("Please enter your last name:");
			String lastName = console.nextLine();
			System.out.println("Please enter the number of passengers:");
			int numPassengers = askNumPassengers();
			String result = application.book(firstName, lastName, dateRequired, numPassengers, regNo);

			System.out.println(result);
		} else
		{
			System.out.println("There are no available cars on this date.");
		}
		return true;
	}
	
	/*
	 * Complete bookings found by either registration number or booking date.
	 */
	private void completeBooking()
	{
		System.out.print("Enter Registration or Booking Date:");
		String response = console.nextLine();
		
		String result;
		// User entered a booking date
		if (response.contains("/"))
		{
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			System.out.print("Enter kilometers:");
			double kilometers = askKilometers();
			try
			{
				int day = Integer.parseInt(response.substring(0, 2));
				int month = Integer.parseInt(response.substring(3, 5));
				int year = Integer.parseInt(response.substring(6));
				DateTime dateOfBooking = new DateTime(day, month, year);
				result = application.completeBooking(firstName, lastName, dateOfBooking, kilometers);
			}
			catch(NumberFormatException ex)
			{
				System.out.println("Error: Date in unreadable \nplease"
						+ "use date foramat DD/MM/YYYY");	
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		} 
		else
		{
			
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			try
			{
				application.getBookingByName(firstName, lastName, response);
				System.out.print("Enter kilometers:");
				double kilometers = Double.parseDouble(console.nextLine());
				result = application.completeBooking(firstName, lastName, response, kilometers);
				System.out.println(result);
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		
	}

	/*
	 * Prompt user for registration number and validate it is in the correct form.
	 * Boolean value for indicating test mode allows by passing validation to test
	 * program without user input validation.
	 */
	private String promptUserForRegNo()
	{
		String regNo = "";
		boolean validRegistrationNumber = false;
		// By pass user input validation.
		if (!testingWithValidation)
		{
			return console.nextLine();
		} 
		else
		{
			while (!validRegistrationNumber)
			{
				regNo = console.nextLine();
				boolean exists = application.checkIfCarExists(regNo);
				if(exists)
				{
					// Empty string means the menu will not try to process
					// the registration number
					System.out.println("Error: Reg Number already exists");
					return "";
				}
				if (regNo.length() == 0)
				{
					break;
				}
				try
				{
					application.isValidId(regNo);
					validRegistrationNumber = true;
				}
				catch(Exception ex)
				{
					System.out.println(ex.getMessage());
					System.out.println("Enter registration number: ");
					System.out.println("(or hit ENTER to exit)");
				}
			}
			return regNo;
		}
	}

	/*
	 * Prints the menu.
	 */
	private void printMenu()
	{
		System.out.printf("\n********** MiRide System Menu **********\n\n");

		System.out.printf("%-30s %s\n", "Create Car", "CC");
		System.out.printf("%-30s %s\n", "Book Car", "BC");
		System.out.printf("%-30s %s\n", "Complete Booking", "CB");
		System.out.printf("%-30s %s\n", "Display ALL Cars", "DA");
		System.out.printf("%-30s %s\n", "Search Specific Car", "SS");
		System.out.printf("%-30s %s\n", "Search Available Cars", "SA");
		System.out.printf("%-30s %s\n", "Seed Data", "SD");
		System.out.printf("%-30s %s\n", "Save Data","SP");
		System.out.printf("%-30s %s\n", "Exit Program", "EX");
		System.out.println("\nEnter your selection: ");
		System.out.println("(Hit enter to cancel any operation)");
	}
	/*
	 * Ask user of car type
	 */
	private String promptUserForCarType()
	{
		boolean ctSuccess = false;
		String choice = "";
		while(!ctSuccess)
		{
			choice = console.nextLine().toUpperCase();
			if( choice.equals("SD") || choice.equals("SS"))
			{
				ctSuccess = true;
				break; 
			}
			else if(choice.equals(""))
			{
				break;
			}
			else
			{
				System.out.println("Please print a car type (SD/SS) or press enter with no text to exit");
			}
		}
		return choice;
	}
	/*
	 * Ask user for booking fee
	 */
	private double getBookingFee()
	{
		double bookingFee = 0;
		bookingFee = 0;
		try 
		{
			bookingFee = 0;
			System.out.print("$");
			bookingFee = console.nextDouble();
			console.nextLine();
			if(bookingFee == 0)				
			{
				return bookingFee;
			}
			else if(bookingFee < 3.00)
			{
				throw new NumberFormatException("This is an ivalid booking fee \n"
					+ "A booking fee for a silver service must be greater than $3.00.\n"
					+ "(Or press enter with no text to exit");
			}
		}
		catch(InputMismatchException ex)
		{
			System.out.println("Please enter a booking Fee in the formmat \n"
					+ "$X.XX\n"
					+ "Enter Booking Fee");
			bookingFee = getBookingFee();
		}
		catch(NumberFormatException ex)
		{
			System.out.println(ex.getMessage());
			bookingFee = getBookingFee();
		}
		return bookingFee;
	}
	/*
	 * Searches for availible cars for given date
	 */
	private String searchAvailible()
	{
		String type = getCarType();
		System.out.println("Enter date car required: ");
		
		String dateEntered = askDate();
		int day = Integer.parseInt(dateEntered.substring(0, 2));
		int month = Integer.parseInt(dateEntered.substring(3, 5));
		int year = Integer.parseInt(dateEntered.substring(6));
		DateTime dateRequired = new DateTime(day, month, year);	
		
		return application.searchAvailible(type, dateRequired);
	}
	/*
	 * Asks for car type
	 */
	private String getCarType()
	{
		System.out.println("Please enter the car type (SD/SS)");
		String type = console.nextLine().toUpperCase();
		if(type.equals("SD") || type.equals("SS"))
		{
			return type;
		}
		else
		{
			System.out.println("Error: invalid car type");
			type = getCarType();
		}
		return type;
	}
	/*
	 * Asks for sory type
	 */
	private String sortType()
	{
		System.out.println("Enter sort order (A/D)");
		String order = console.nextLine().toUpperCase();
		return order;
	}
	/*
	 * Ask for required date
	 */
	private String askDate()
	{
		System.out.println("format DD/MM/YYYY)");
		String dateEntered = console.nextLine();
		
		try
		{
			int day = Integer.parseInt(dateEntered.substring(0, 2));
			int month = Integer.parseInt(dateEntered.substring(3, 5));
			int year = Integer.parseInt(dateEntered.substring(6));
		}
		catch(InputMismatchException ex)
		{
			System.out.println("Please input the correct date fornat");
			dateEntered = askDate();
		}
		return dateEntered;
	}
/*
 * Ask for KM traveled
 */
	private double askKilometers()
	{
		double kilometers;
		try
		{
			kilometers = console.nextDouble();
			console.nextLine();
		}
		catch(InputMismatchException ex)
		{
			console.nextLine();
			System.out.println("Error: Killometetrs unidentifable\n"
					+ "Please enter the distance traveled in kilometers");
			kilometers = askKilometers();
		}
		return kilometers;
	}
}
