package app;

import cars.Car;
import cars.SilverServiceCar;
import utilities.DateTime;
import utilities.MiRidesUtilities;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Rodney Cocker & Jesse Osrecak
 */
public class MiRideApplication
{
	private Car[] cars = new Car[15];
	
	private int itemCount = 0;
	private String[] availableCars;

	public MiRideApplication()
	{
		//seedData();
	}
	
	public String createCar(String id, String make, String model, String driverName, int numPassengers) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) {
			cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount-1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}

	public String[] book(DateTime dateRequired)
	{
		int numberOfAvailableCars = 0;
		// finds number of available cars to determine the size of the array required.
		for(int i=0; i<cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					numberOfAvailableCars++;
				}
			}
		}
		if(numberOfAvailableCars == 0)
		{
			String[] result = new String[0];
			return result;
		}
		availableCars = new String[numberOfAvailableCars];
		int availableCarsIndex = 0;
		// Populate available cars with registration numbers
		for(int i=0; i<cars.length;i++)
		{
			
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". " + cars[i].getRegistrationNumber();
					availableCarsIndex++;
				}
			}
		}
		return availableCars;
	}
	
	public String book(String firstName, String lastName, DateTime required, int numPassengers, String registrationNumber)
	{
		Car car = getCarById(registrationNumber);
		if(car != null)
        {
            car.book(firstName, lastName, required, numPassengers);
		    String message = "Thank you for your booking. \n" + car.getDriverName() 
		        + " will pick you up on " + required.getFormattedDate() + ". \n"
				+ "Your booking reference is: " + car.getBookingID(firstName, lastName, required);
		    return message;
        }
        else{
            return "Car with registration number: " + registrationNumber + " was not found.";
        }
	}
	
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers)
	{
		String result = "";
		
		// Search all cars for bookings on a particular date.
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				result = cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
				if(!result.equals("Booking not found"))
				{
					return result;
				}
			}
		}
		return "Booking not found.";
	}
	
	public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers)
	{
		String carNotFound = "Car not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}

		if (car == null)
		{
			return carNotFound;
		}
		if (car.getBookingByName(firstName, lastName) != -1)
		{
			return car.completeBooking(firstName, lastName, kilometers);
		}
		return "Error: Booking not found.";
	}
	
	public boolean getBookingByName(String firstName, String lastName, String registrationNumber)
	{
		String bookingNotFound = "Error: Booking not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}
		
		if(car == null)
		{
			return false;
		}
		if(car.getBookingByName(firstName, lastName) == -1)
		{
			return false;
		}
		return false;
	}
	public String displaySpecificCar(String regNo)
	{
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(cars[i].getRegistrationNumber().equals(regNo))
				{
					return cars[i].getDetails();
				}
			}
		}
		return "Error: The car could not be located.";
	}
	
	public boolean seedData()
	{
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				return false;
			}
		}
		// 2 cars not booked
		Car honda = new Car("SIM194", "Honda", "Accord Euro", "Henry Cavill", 5);
		cars[itemCount] = honda;
		honda.book("Craig", "Cocker", new DateTime(1), 3);
		itemCount++;
		
		Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
		cars[itemCount] = lexus;
		lexus.book("Craig", "Cocker", new DateTime(1), 3);
		itemCount++;
		
		// 2 cars booked
		Car bmw = new Car("BMW256", "Mini", "Minor", "Barbara Streisand", 4);
		cars[itemCount] = bmw;
		itemCount++;
		bmw.book("Craig", "Cocker", new DateTime(1), 3);
		
		Car audi = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
		cars[itemCount] = audi;
		itemCount++;
		audi.book("Rodney", "Cocker", new DateTime(1), 4);
		
		// 1 car booked five times (not available)
		Car toyota = new Car("TOY765", "Toyota", "Corola", "Tina Turner", 7);
		cars[itemCount] = toyota;
		itemCount++;
		toyota.book("Rodney", "Cocker", new DateTime(1), 3);
		toyota.book("Craig", "Cocker", new DateTime(2), 7);
		toyota.book("Alan", "Smith", new DateTime(3), 3);
		toyota.book("Carmel", "Brownbill", new DateTime(4), 7);
		toyota.book("Paul", "Scarlett", new DateTime(5), 7);
		toyota.book("Paul", "Scarlett", new DateTime(6), 7);
		toyota.book("Paul", "Scarlett", new DateTime(7), 7);
		
		// 1 car booked five times (not available)
		Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
		cars[itemCount] = rover;
		itemCount++;
		rover.book("Rodney", "Cocker", new DateTime(1), 3);
		//rover.completeBooking("Rodney", "Cocker", 75);
		DateTime inTwoDays = new DateTime(2);
		rover.book("Rodney", "Cocker", inTwoDays, 3);
		rover.completeBooking("Rodney", "Cocker", inTwoDays,75);
		return true;
	}

	public String displayAllBookings(String type, String order)
	{
		if(itemCount == 0)
		{
			return "No cars have been added to the system.";
		}
		Car[] carsTemp = new Car[15];
		Car[] ssTemp = new Car[15];
		StringBuilder sb = new StringBuilder();
		sb.append("Summary of all cars: ");
		sb.append("\n");
		for(int i = 0; i < cars.length; i = i + 1)
		{
			if(cars[i] instanceof SilverServiceCar)
			{
				for(int j = 0; j < ssTemp.length; j = j + 1) 
				{
					if(ssTemp[j] != null)
					{
						ssTemp[j] = cars[i];
						break;
					}
				}
			}
			else if(cars[i] instanceof Car)
			{
				for(int j = 0; j < carsTemp.length; j = j + 1) 
				{
					if(carsTemp[j] == null)
					{
						carsTemp[j] = cars[i];
						break;
					}
				}
			}
		}

		boolean sorted = false;
		while(!sorted)
		{
			System.out.println(1);
			sorted = true;
			for(int i = 0; i < ssTemp.length ; i = i + 1)
			{

				if(ssTemp[i + 1] == null)
				{
					break;
				}				
				String currentCar = ssTemp[i].getRegistrationNumber();
				String nextCar = ssTemp[i + 1].getRegistrationNumber();
				if(order.contentEquals("A"))
				{
					boolean sortedAscending = currentCar.compareTo(nextCar) < 0;
					if(!sortedAscending)
					{
						Car temp = ssTemp[i];
						ssTemp[i] = ssTemp[i+1];
						ssTemp[i+1] = temp;
						sorted = false;
					}
				} 
				else
				{
					boolean sortedDescending = currentCar.compareTo(nextCar) > 0;
					if(!sortedDescending)
					{
						Car temp = ssTemp[i];
						ssTemp[i] = ssTemp[i+1];
						ssTemp[i+1] = temp;
						sorted = false;
					}
				}	
			}
			for(int i = 0; i < carsTemp.length ; i = i + 1) 
			{
				if(carsTemp[i + 1] == null)
				{
					break;
				}				
				String currentCar = carsTemp[i].getRegistrationNumber();
				String nextCar = carsTemp[i + 1].getRegistrationNumber();
				if(order.contentEquals("A"))
				{
					boolean sortedAscending = currentCar.compareTo(nextCar) < 0;
					if(!sortedAscending)
					{
						Car temp = carsTemp[i];
						carsTemp[i] = carsTemp[i+1];
						carsTemp[i+1] = temp;
						sorted = false;
					}
				} 
				else
				{
					boolean sortedDescending = currentCar.compareTo(nextCar) > 0;
					if(!sortedDescending)
					{
						Car temp = carsTemp[i];
						carsTemp[i] = carsTemp[i+1];
						carsTemp[i+1] = temp;
						sorted = false;
					}
				}
			}
		}

		if(type.matches("SS"))
		{
			for(int j = 0; j < ssTemp.length; j = j + 1) 
			{
				if(ssTemp[j] != null)					
				{
					sb.append(ssTemp[j].getDetails());
				}
			}
			
		}
		else if(type.matches("SD"))
		{
			for(int j = 0; j < carsTemp.length; j = j + 1) 
			{
				if(carsTemp[j] != null)					
				{
					sb.append(carsTemp[j].getDetails());
				}
			}
		}
		



		return sb.toString();
	}

	public String displayBooking(String id, String seatId)
	{
		Car booking = getCarById(id);
		if(booking == null)
		{
			return "Booking not found";
		}
		return booking.getDetails();
	}
	
	public String isValidId(String id)
	{
		return MiRidesUtilities.isRegNoValid(id);
	}

	public boolean checkIfCarExists(String regNo)
	{
		Car car = null;
		if (regNo.length() != 6)
		{
			return false;
		}
		car = getCarById(regNo);
		if (car == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private Car getCarById(String regNo)
	{
		Car car = null;

		for (int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(regNo))
				{
					car = cars[i];
					return car;
				}
			}
		}
		return car;
	}
	
	public  String createSilverCar(String id, String make, String model, String driverName, int numPassengers, double fee, String refreshmentsList)
	{
		
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) {
			cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, fee, refreshmentsList.split(","));
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount-1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}
	
	public String searchAvailible(String type, DateTime required)
	{
		Car[] carsTemp = new Car[15];
		Car[] ssTemp = new Car[15];
		for(int i = 0; i < cars.length; i = i + 1)
		{
			if(cars[i] instanceof SilverServiceCar)
			{
				for(int j = 0; j < ssTemp.length; j = j + 1) 
				{
					if(ssTemp[j] == null)
					{
						ssTemp[j] = cars[i];
						break;
					}
				}
			}
			else if(cars[i] instanceof Car)
			{
				for(int j = 0; j < ssTemp.length; j = j + 1) 
				{
					if(carsTemp[j] == null)
					{
						carsTemp[j] = cars[i];
						break;
					}
				}
			}
		}
		
		String avaCars = "";
		if(type.matches("SS"))
		{
			for(int i = 0; i < ssTemp.length; i = i + 1)
			{
				if(ssTemp[i] != null && ssTemp[i].bookingOnDate(required) && ssTemp[i].bookingAvailable())
				{
					avaCars = avaCars + ssTemp[i].getDetails();
				}
			}
		}
		else if(type.matches("SD"))
		{
			for(int i = 0; i < carsTemp.length; i = i + 1)
			{
				if(carsTemp[i] != null && carsTemp[i].bookingOnDate(required) && carsTemp[i].bookingAvailable())
				{
					avaCars = avaCars + carsTemp[i].getDetails();
				}
			}
		}
		if(avaCars.contentEquals(""))
		{
			avaCars = "Error - no cars were found on this date";
		}
		return avaCars;
	}
	
	
}
