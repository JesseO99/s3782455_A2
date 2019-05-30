package cars;

import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		Car
 * Description:	The class represents a silver service car in a ride sharing system. 
 * Original Author:		Jesse Osrecak
 */
public class SilverServiceCar extends Car
{
	//Extra variables for Silver Service car
	private String refreshments[] = new String[10];
	/*
	 * Constructor of Silver Service Car
	 * ALGORITHM
	 * BEGIN
	 * 		CALL parent Constructor
	 * 		SET	booking Fee
	 * 		SET booking Advanced to 3
	 * 		Set Refreshments
	 * END
	 * 
	 */
	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity, double bookingFee, String[] refreshments) throws Exception
	{
		super(regNo, make, model, driverName, passengerCapacity);
		super.setBookingFee(bookingFee);
		super.setBookAdvanced(3);
		int refLength = refreshments.length;
		for (int i = 0 ; i < refLength; i = i + 1)
		{
			if(refreshments[i] != null)
			{
				setRefreshments(refreshments[i]);
			}
			else
			{
				break;
			}
		}
	}
	/*
	 * Books Car
	 * Checks to see if car can be booked 
	 * Throws exception if car cannot be booked and booking fails
	 * 
	 */
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers ) throws Exception
	{
		boolean booked = false;
		if(DateUtilities.dateIsNotMoreThanXDays(required, 3))
		{
			booked = super.book(firstName, lastName, required, numPassengers);
		}
		return booked;
	}
	/*
	 * Sets refreshments list
	 * 
	 */
	private void setRefreshments(String refreshment)
	{
		for(int i = 0; i < refreshments.length; i = i + 1)
		{
			if(refreshments[i] == null)
			{
				refreshments[i] = refreshment;
				break;
			}
		}
			
	}
	/*
	 * Creates a string in a printable format for people to read and returns it to host method
	 */
	@Override 
	public String getDetails()
	{
		String details = super.getDetails() + "refreshments availible\n";
		for(int i = 0; i < refreshments.length ; i = i + 1)
		{
			if(refreshments[i] != null)
			{
				details = details + "Item" + i + "			" + refreshments[i] + "\n";
			}
			else
			{
				break;
			}
		}
		details = details + super.getCurrentBookings();
		details = details + super.getPastBookings();
		return details;
	}
	/*
	 * Creates a string that the computer can read. 
	 * Variables are separated by ':'
	 * Refreshments are separated by ','
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(":" + super.getBookingFee() + ":");
		for(int i = 0; i < refreshments.length; i = i + 1)
		{
			if(refreshments[i] != null)
			{
				sb.append(refreshments[i] + ",");
			}
		}
		return sb.toString();
	}
}
