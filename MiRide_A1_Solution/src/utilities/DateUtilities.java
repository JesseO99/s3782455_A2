package utilities;

public class DateUtilities {
	/*
	 * Checks to see if the date is in the past
	 */
	public static boolean dateIsNotInPast(DateTime date)
	{
		final int OFFSET_FOR_DAYS_IN_MILLISECONDS = 1;
		boolean notInPast = false;
		
		DateTime today = new DateTime();
		
		int daysInPast = DateTime.diffDays(date, today) + OFFSET_FOR_DAYS_IN_MILLISECONDS;
		if(daysInPast >=0)
		{
			notInPast = true;
		}
		
		return notInPast;
	}
	/*
	 * Checks to see if the dates are the same
	 */
	public static boolean datesAreTheSame(DateTime date1, DateTime date2)
	{
		if(date1.getEightDigitDate().equals(date2.getEightDigitDate()))
		{
			return true;
		}
		return false;
	}
	/*
	 * Checks to see if date is more than 7 days 
	 */
	public static boolean dateIsNotMoreThan7Days(DateTime date)
	{
		
		boolean within7Days = false;
		DateTime today = new DateTime();
		DateTime nextWeek = new DateTime(7);
		
		int daysInFuture = DateTime.diffDays(nextWeek, date);
		if(daysInFuture >0 && daysInFuture <8)
		{
			within7Days = true;
		}
		return within7Days;
	}
	/*
	 * Checks to see if date is more than 3 days than current date
	 */
	public static boolean dateIsNotMoreThan3Days(DateTime date)
	{
		boolean within3Days = false;
		DateTime today = new DateTime();
		DateTime nextWeek = new DateTime(3);
		
		int daysInFuture = DateTime.diffDays(nextWeek, date);
		if(daysInFuture > 0 && daysInFuture < 4)
		{
			within3Days = true;
		}
		return within3Days;
	}
	/*
	 * Checks to see if date is more than x days than the current date
	 */
	public static boolean dateIsNotMoreThanXDays(DateTime date, int x)
	{
		boolean within3Days = false;
		DateTime today = new DateTime();
		DateTime limit = new DateTime(x);
		
		int daysInFuture = DateTime.diffDays(limit, date);
		if(daysInFuture > 0 && daysInFuture < x + 1)
		{
			within3Days = true;
		}
		return within3Days;
	}
	/*
	 * creates date object from eight digit date string
	 */
	public static DateTime getDateFromEightDigit(String eightDigitDate)
	{
		int day = Integer.parseInt(eightDigitDate.substring(0,2));
		int month = Integer.parseInt(eightDigitDate.substring(2,4));
		int year = Integer.parseInt(eightDigitDate.substring(4));
		DateTime date = new DateTime(day, month, year);
		return date;
	}
}
