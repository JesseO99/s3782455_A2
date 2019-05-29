package cars;

import utilities.DateTime;
import utilities.DateUtilities;

public class SilverServiceCar extends Car
{
	private double minimumBookingFee = 3.00;
	private String refreshments[] = new String[10];
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
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		for(int i = 0; i < refreshments.length; i = i + 1)
		{
			if(refreshments[i] != null)
			{
				sb.append(":" + refreshments[i]);
			}
		}
		return sb.toString();
	}
}
