package cars;

import utilities.DateTime;
import utilities.DateUtilities;

public class SilverServiceCar extends Car
{
	private double minimumBookingFee = 3.00;
	private String refreshments[] = new String[10];
	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity, double bookingFee, String[] refreshments)
	{
		super(regNo, make, model, driverName, passengerCapacity);
		super.setBookingFee(bookingFee);
		super.setBookAdvanced(3);
		int refLength = refreshments.length;
		for (int i = 0 ; i <= refLength; i = i++)
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
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers )
	{
		return super.book(firstName, lastName, required, numPassengers);
	}
	private void setRefreshments(String refreshment)
	{
		for(int i = 0; i <= refreshments.length; i = i + 1)
		{
			if(refreshments[i] == null)
			{
				refreshments[i] = refreshment;
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
				details = details + "Items " + i + " " + refreshments[i] + "\n";
			}
			else
			{
				break;
			}
			
			details = details + super.getCurrentBookings() + super.getPastBookings();
		}
		return details;
	}
	@Override
	public String toString()
	{
		String string = super.toString();
		
		return string;
	}
}
