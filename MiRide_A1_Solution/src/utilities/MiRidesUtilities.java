package utilities;

public class MiRidesUtilities 
{
	private final static int ID_LENGTH = 6;
	/*
	 * Checks to see if registration number is valid
	 */
	public static String isRegNoValid(String regNo) throws Exception
	{
		int regNoLength = regNo.length();
		if(regNoLength != ID_LENGTH)
		{
			throw new Exception("Error: registration number must be 6 characters");
		}
		boolean letters = regNo.substring(0,3).matches("[a-zA-Z]+");
		if (!letters) {
			throw new Exception("Error: The registration number should begin with three alphabetical characters.");
		}
		boolean numbers = regNo.substring(3).matches("[0-9]+");
		if (!numbers) {
			throw new Exception("Error: The registration number should end with three numeric characters.");
		}
		return regNo;
		// Regular expressions for validating the subcomponents of the registration number.
	}

}
