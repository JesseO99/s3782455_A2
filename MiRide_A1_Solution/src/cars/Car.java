package cars;

import java.util.ArrayList;

import utilities.DateTime;
import utilities.DateUtilities;
import utilities.MiRidesUtilities;

/*
 * Class:		Car
 * Description:	The class represents a car in a ride sharing system. 
 * Original Author:		Rodney Cocker 
 * Modified by: 		Jesse Osrecak
 */
public class Car
{
	// Car attributes
	private String regNo;
	private String make;
	private String model;
	private String driverName;
	private int passengerCapacity;

	// Constants
	private final double STANDARD_BOOKING_FEE = 1.5;
	private final int MAXIUM_PASSENGER_CAPACITY = 10;
	private final int MINIMUM_PASSENGER_CAPACITY = 1;
	
	// Tracking bookings
	private Booking[] currentBookings;
	private Booking[] pastBookings;
	private boolean available;
	private int bookingSpotAvailable = 0;
	private double tripFee = 0;
	private double bookingFee = STANDARD_BOOKING_FEE;
	private int bookAdvanced = 7;

	
	public Car(String regNo, String make, String model, String driverName, int passengerCapacity) throws Exception
	{
		setRegNo(regNo); // Validates and sets registration number
		setPassengerCapacity(passengerCapacity); // Validates and sets passenger capacity

		this.make = make;
		this.model = model;
		this.driverName = driverName;
		available = true;
		currentBookings = new Booking[5];
		pastBookings = new Booking[10];
	}

	/*
	 * Checks to see if the booking is permissible such as a valid date, number of
	 * passengers, and general availability. Creates the booking only if conditions
	 * are met and assigns the trip fee to be equal to the standard booking fee.
	 * 
	 * If booking has failed the method throws exception.
	 */


	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws Exception
	{
		boolean booked = false;
		// Does car have five bookings
		available = bookingAvailable();
		try
		{
		notCurrentlyBookedOnDate(required);
		// Date is within range, not in past and within the next week
		dateIsValid(required);
		// Number of passengers does not exceed the passenger capacity and is not zero.
		numberOfPassengersIsValid(numPassengers);
		bookCar(firstName, lastName, required, numPassengers);
		booked = true;
		return booked;
		}
		catch(Exception ex)
		{
			System.out.println("Booking Failed");
			throw ex;
		}
		// Booking is permissible


	}
	
	//Method actually books the car
	private void bookCar(String firstName, String lastName, DateTime required, int numPassengers)
	{
		tripFee = bookingFee;
		Booking booking = new Booking(firstName, lastName, required, numPassengers, this);
		currentBookings[bookingSpotAvailable] = booking;
		bookingSpotAvailable++;
	}

	/*
	 * Completes a booking based on the name of the passenger and the booking date.
	 * 
	 * Throws exception if the booking failed
	 */
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers) throws Exception
	{
		// Find booking in current bookings by passenger and date
		
		int bookingIndex = getBookingByDate(firstName, lastName, dateOfBooking);

		if (bookingIndex == -1)
		{
			throw new Exception("Error: Booking not found for " + firstName + " " + lastName);
		}

		return completeBooking(bookingIndex, kilometers);
	}

	/*
	 * Completes a booking based on the name of the passenger.
	 * 
	 * throws Exception if booking failed
	 */
	public String completeBooking(String firstName, String lastName, double kilometers) throws Exception
	{
		int bookingIndex = getBookingByName(firstName, lastName);

		if (bookingIndex == -1)
		{
			throw new Exception("Error: Booking not found for " + firstName + " " + lastName);
		} else
		{
			return completeBooking(bookingIndex, kilometers);
		}
	}

	/*
	 * Checks the current bookings to see if any of the bookings are for the current
	 * date. 
	 */
	public boolean isCarBookedOnDate(DateTime dateRequired)
	{
		boolean carIsBookedOnDate = false;
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				if (DateUtilities.datesAreTheSame(dateRequired, currentBookings[i].getBookingDate()))
				{
					carIsBookedOnDate = true;
				}
			}
		}
		return carIsBookedOnDate;
	}

	/*
	 * Retrieves a booking id based on the name and the date of the booking
	 */
	public String getBookingID(String firstName, String lastName, DateTime dateOfBooking)
	{
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				int days = DateTime.diffDays(dateOfBooking, booking.getBookingDate());
				if (firstNameMatch && lastNameMatch && days == 0)
				{
					return booking.getID();
				}
			}
		}
		return "Booking not found";
	}

	/*
	 * Human readable presentation of the state of the car.
	 */
	public String getDetails()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(getRecordMarker());
		sb.append(String.format("%-15s %s\n", "Reg No:", regNo));
		sb.append(String.format("%-15s %s\n", "Make & Model:", make + " " + model));

		sb.append(String.format("%-15s %s\n", "Driver Name:", driverName));
		sb.append(String.format("%-15s %s\n", "Capacity:", passengerCapacity));

		if (available)
		{
			sb.append(String.format("%-15s %s\n", "Available:", "YES"));
		} else
		{
			sb.append(String.format("%-15s %s\n", "Available:", "NO"));
		}

		return sb.toString();
	}

	/*
	 * Computer readable state of the car
	 * Variables separated by ':'
	 * Bookings separated by ';'
	 * Parts of bookings separated by ','
	 */

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(regNo + ":" + make + ":" + model);
		if (driverName != null)
		{
			sb.append(":" + driverName);
		}
		sb.append(":" + passengerCapacity);
		if (available)
		{
			sb.append(":" + "YES");
		} else
		{
			sb.append(":" + "NO");
		}
		sb.append(":");
		for(int i = 0; i < currentBookings.length; i = i + 1)
		{
			if(currentBookings[i] != null)
			{
				sb.append( currentBookings[i].toString()+ ";");
			}
		}
		sb.append(":");
		for(int j = 0; j < pastBookings.length; j = j + 1)
		{
			if(pastBookings[j] != null)
			{
				sb.append(pastBookings[j].toString() + ";");
			}
		}
		return sb.toString();
	}

	// Required getters
	public String getRegistrationNumber()
	{
		return regNo;
	}

	public String getDriverName()
	{
		return driverName;
	}

	public double getTripFee()
	{
		return tripFee;
	}

	/*
	 * Checks to see if any past bookings have been recorded
	 */
	private boolean hasBookings(Booking[] bookings)
	{
		boolean found = false;
		for (int i = 0; i < bookings.length; i++)
		{
			if (bookings[i] != null)
			{
				found = true;
			}
		}
		return found;
	}

	/*
	 * Processes the completion of the booking
	 */
	private String completeBooking(int bookingIndex, double kilometers)
	{
		Booking booking = currentBookings[bookingIndex];
		// Remove booking from current bookings array.
		currentBookings[bookingIndex] = null;

		// call complete booking on Booking object
		// double kilometersTravelled = Math.random()* 100;
		double fee = kilometers * (STANDARD_BOOKING_FEE * 0.3);
		tripFee += fee;
		booking.completeBooking(kilometers, fee, STANDARD_BOOKING_FEE);
		// add booking to past bookings
		for (int i = 0; i < pastBookings.length; i++)
		{
			if (pastBookings[i] == null)
			{
				pastBookings[i] = booking;
				break;
			}
		}
		String result = String.format("Thank you for riding with MiRide.\nWe hope you enjoyed your trip.\n$"
				+ "%.2f has been deducted from your account.", tripFee);
		return result;
	}

	/*
	 * Gets the position in the array of a booking based on a name and date. Returns
	 * the index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	private int getBookingByDate(String firstName, String lastName, DateTime dateOfBooking)
	{
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				boolean dateMatch = DateUtilities.datesAreTheSame(dateOfBooking, currentBookings[i].getBookingDate());
				if (firstNameMatch && lastNameMatch && dateMatch)
				{
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * Gets the position in the array of a booking based on a name. Returns the
	 * index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	public int getBookingByName(String firstName, String lastName)
	{
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				boolean firstNameMatch = currentBookings[i].getFirstName().toUpperCase()
						.equals(firstName.toUpperCase());
				boolean lastNameMatch = currentBookings[i].getLastName().toUpperCase().equals(lastName.toUpperCase());
				if (firstNameMatch && lastNameMatch)
				{
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * A record marker mark the beginning of a record.
	 */
	private String getRecordMarker()
	{
		final int RECORD_MARKER_WIDTH = 60;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < RECORD_MARKER_WIDTH; i++)
		{
			sb.append("_");
		}
		sb.append("\n");
		return sb.toString();
	}

	/*
	 * Checks to see if the number of passengers falls within the accepted range.
	 * 
	 * Throws Exception if does not meet requirements
	 */
	private boolean numberOfPassengersIsValid(int numPassengers) throws Exception
	{
		if (numPassengers < MINIMUM_PASSENGER_CAPACITY) 
		{
			throw new Exception("Error: The number of passengers is less than " + MINIMUM_PASSENGER_CAPACITY);
		}
		if (numPassengers > MAXIUM_PASSENGER_CAPACITY)
		{
			throw new Exception("Error: The number of passengers is more than " + MAXIUM_PASSENGER_CAPACITY);
		}
		return true;
	}

	/*
	 * Checks that the date is not in the past or more than 7 days in the future.
	 */
	private boolean dateIsValid(DateTime date)
	{
		return DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThanXDays(date, bookAdvanced);
	}

	/*
	 * Indicates if a booking spot is available. If it is then the index of the
	 * available spot is assigned to bookingSpotFree.
	 */
	public boolean bookingAvailable()
	{
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] == null)
			{
				bookingSpotAvailable = i;
				return true;
			}
		}
		return false;
	}

	/*
	 * Checks to see if if the car is currently booked on the date specified.
	 * Throws exception if car is currently booked on date
	 */
	private boolean notCurrentlyBookedOnDate(DateTime date) throws Exception
	{
		boolean foundDate = true;
		for (int i = 0; i < currentBookings.length; i++)
		{
			if (currentBookings[i] != null)
			{
				int days = DateTime.diffDays(date, currentBookings[i].getBookingDate());
				if (days == 0)
				{
					throw new Exception(this.toString() + "Is currently booked on date " + date.getFormattedDate());
				}
			}
			
		}
		return foundDate;
		
	}

	/*
	 * Validates and sets the registration number
	 * 
	 * throws Exception if RegNo is not valid
	 */
	private void setRegNo(String regNo) throws Exception
	{
		try
		{
			MiRidesUtilities.isRegNoValid(regNo);
			this.regNo = regNo;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	/*
	 * Validates and sets the passenger capacity
	 */
	private void setPassengerCapacity(int passengerCapacity)
	{
		boolean validPasengerCapcity = passengerCapacity >= MINIMUM_PASSENGER_CAPACITY
				&& passengerCapacity < MAXIUM_PASSENGER_CAPACITY;

		if (validPasengerCapcity)
		{
			this.passengerCapacity = passengerCapacity;
		} else
		{
			this.passengerCapacity = -1;
		}
	}
	
	/*
	 * More required getters and setters
	 */
	public void setBookingFee(double bookingFee)
	{
		bookingFee = this.bookingFee;
	}
	public double getBookingFee()
	{
		return bookingFee;
	}
	
	public void setBookAdvanced(int bookAdvanced)
	{
		this.bookAdvanced = bookAdvanced;
	}
	public String getCurrentBookings()
	{
		String currentBookingsDetails = "Current Bookings:\n";
		for(int i = 0 ; i < currentBookings.length; i = i + 1)
		{
			if(currentBookings[i] != null)
			{
				currentBookingsDetails = currentBookingsDetails + currentBookings[i].getDetails() + "\n";
			}
			else
			{
				break;
			}
		}
		
		return currentBookingsDetails;
	}
	
	public String getPastBookings()
	{
		String pastBookingsDetails = "PastBookings:\n";
		for(int i = 0 ; i < pastBookings.length; i = i + 1)
		{
			if(pastBookings[i] != null)
			{
				pastBookingsDetails = pastBookingsDetails +  pastBookings[i].getDetails() + "\n";
			}
			else
			{
				break;
			}
		}
		
		return pastBookingsDetails;
	}
	
	/*
	 * Checks to see if booking is on Date required
	 * 
	 * Throws Exception if currently booked on the given date
	 */
	public boolean bookingOnDate(DateTime required) throws Exception
	{
		return !notCurrentlyBookedOnDate(required);
	}
	/*
	 * Required getter for creating Car from loading saved text document
	 * Throws Exception if car creation failed.
	 * ALGORTIHM
	 * BEGIN
	 *	 SPLIT car details into individual components
	 *	 DECIFER if the car is a Standard car or a silver Service Car
	 *	 CREATE Car or Silver Service Car
	 *	 CHECK IF there exists past bookings
	 *			SPLIT bookings into individual bookings
	 *				SPLIT booking into individual components
	 *	 			CREATE booking for each past booking
	 *				COMPLETE bookings for each past booking
	 *	 CHECK IF there exists any current bookings
	 *			SPLIT booking into individual bookings
	 *			SPLIT booking into individual components
	 *	 RETURN Car
	 * END
	 */
	public static Car getCar(String text) throws Exception
	{
		String[] tokens = text.split(":");

		try
		{	
			 if(tokens.length > 8) //Checks to see if Car is a Silver Service Car
			{
				SilverServiceCar car = new SilverServiceCar(tokens[0], tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Double.parseDouble(tokens[8]), tokens[9].split(","));
				
				
				if(!tokens[7].equals(""))
				{
					String[] pastBookings = tokens[7].split(";"); // Splits up past bookings
					for(int i = 0; i < pastBookings.length; i ++ )
					{
						if(!pastBookings.equals(""))
						{
							String[] pastBooking = pastBookings[i].split(","); //Splits up past Booking into individual components
							car.book(pastBooking[3], pastBooking[4], DateUtilities.getDateFromEightDigit(pastBooking[2]), Integer.parseInt(pastBooking[5]));
							car.completeBooking(pastBooking[3], pastBooking[4], DateUtilities.getDateFromEightDigit(pastBooking[2]), Double.parseDouble(pastBooking[6]));
						}
						else
						{
							break;
						}
					}
				}
					
				
				if(!tokens[6].equals(""))
				{
					String[] currentBookings = tokens[6].split(";"); //Splits up current bookings
					for(int i = 0; i < currentBookings.length; i++)
					{	
						if(!currentBookings[i].equals(""))
						{
							String[] currentBooking = currentBookings[i].split(","); //Splits up current bookings into individual components
							car.book(currentBooking[3], currentBooking[4], DateUtilities.getDateFromEightDigit(currentBooking[2]), Integer.parseInt(currentBooking[5]));
						}
						else
						{
							break;
						}
						
					}
				}
				
				return car;
			}
			else //Bookings current car as a standard car
			{	
				Car car =  new Car(tokens[0], tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]));
				
			
				if(tokens.length == 8)
				 
				{
					String[] pastBookings = tokens[7].split(";"); //Splits up past bookings into individual bookings
					for(int i = 0; i < pastBookings.length; i ++ )
					{
						if(!pastBookings.equals(""))
						{
							String[] pastBooking = pastBookings[i].split(",");//SPlits up past bookings into individual components
							car.book(pastBooking[3], pastBooking[4], DateUtilities.getDateFromEightDigit(pastBooking[2]), Integer.parseInt(pastBooking[5]));
							car.completeBooking(pastBooking[3], pastBooking[4], DateUtilities.getDateFromEightDigit(pastBooking[2]), Double.parseDouble(pastBooking[6]));
						}
						else
						{
							break;
						}
					}
				}
				
				if(!tokens[6].equals(""))
				{
					String[] currentBookings = tokens[6].split(";");//Splits up current bookings into individual bookings
					
					for(int i = 0; i < currentBookings.length; i++)
					{
						if(!currentBookings[i].equals(""))
						{
							String[] currentBooking = currentBookings[i].split(","); //Splits up current booking into individual components
							car.book(currentBooking[3], currentBooking[4], DateUtilities.getDateFromEightDigit(currentBooking[2]), Integer.parseInt(currentBooking[5]));
						}
						else
						{
							break;
						}
						
					}
				}
				
				return car;
			}
	
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
	}
}
