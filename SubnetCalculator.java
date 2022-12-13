import java.util.Scanner;
/**
 * @author Tanzim Ahmed Sagar
 * @version 2.0
 */
public class SubnetCalculator {
	private static Scanner keyboard = new Scanner(System.in);

	/**
	 * @param args command line arguments [<u>not implemented yet</u>]
	 */
	public static void main(String[] args) {
		Liner l = new Liner('+');
		DataObject dto = new DataObject();
		Methods method = new Methods();
		boolean restart = true;

		System.out.println(l.print(40));
		System.out.println("SUBNET CALCULATOR FOR CST8109");
		while (restart) {
			method.printTable(dto); //printing table
			int usrChoice = method.inputInt(keyboard);
			switch (usrChoice) {
			//CLOSE
				case 0:
					System.out.println(l.print(40));
					restart = false;
					break;
			//GIP
				case 1:
					System.out.println(l.print(40));
					dto.setGIP(method.inputIP(keyboard));
					break;
			//GSM
				case 2:
					System.out.println(l.print('-',40));
					System.out.println("[1] As actual IP address");
					System.out.println("[2] As /? format");
					System.out.println(l.print('-',40));
					System.out.print("Enter your choice: ");
					int usrChoiceForInputFormat = method.inputInt(keyboard);
					System.out.println(l.print(40));
					switch(usrChoiceForInputFormat) {
				//ACTUAL IP
					case 1:
						dto.setGSM(method.inputIP(keyboard));
						break;
				//SHORT FORM
					case 2:
						System.out.print("Enter the gSM in /? format: ");
						dto.setGSMslashValue(method.inputInt(keyboard));
						break;
				//ERROR
					default:
						System.err.println("Invalid Choice!");
						break;
					}
					break;
			//BORROWED BITS (s)
				case 3:
					System.out.println(l.print(40));
					System.out.print("Enter the amount of borrowed bits: ");
					dto.setAmountOfBorrowedBits(method.inputInt(keyboard));
					break;
			//AMOUNT OF SUBNET (S)
				case 4:
					System.out.println(l.print(40));
					System.out.print("Enter the amount of subnets can be created: ");
					dto.setAmountOfSubnet(method.inputInt(keyboard));
					break;
			//AMOUNT OF HOST BIT (h)
				case 5:
					System.out.println(l.print(40));
					System.out.print("Enter the amount of host bits: ");
					dto.setAmountOfHostBits(method.inputInt(keyboard));
					break;
			//TOTAL HIP
				case 6:
					System.out.println(l.print(40));
					System.out.print("Enter the amount of host bits: ");
					dto.setAmountOfHostIP(method.inputInt(keyboard));
					break;
			//USABLE HIP
				case 7:
					System.out.println(l.print(40));
					System.out.print("Enter the amount of host IP can be created: ");
					dto.setAmountOfUsableHIP(method.inputInt(keyboard));
					break;
			//RESET TABLE
				case 9:
					System.out.println(l.print('-',40));
					dto = new DataObject();
					System.out.println("Reset Done!");
					break;
			//ERROR
				default:
					System.err.println("Unrecognized Choice!");
					break;
			} // end switch
		} // end while
	} // end main
} // end of outer class

/**
 * Supplementary class of the main to avoid code redundancy
 * @author Tanzim Ahmed Sagar
 * @version 2.0
 */
class Methods {
	private Liner l = new Liner('-');
	/**
	 * Does not prompt the user for input, reads in an {@code int} value when there is one in the provided {@code Scanner}
	 * If the user does not provide input that can be converted into an {@code int} an
	 * error message is printed requesting a correct input and a loop is used
	 * to trap the user until they get this right.
	 * @param scanner Scanner Object
	 * @return inputed {@code int} from {@code Scanner}
	 */
	public int inputInt(Scanner scanner) {
		boolean isInputBad = true;
		boolean hasNextInt;
		int value = 0;
		while(isInputBad) {
			hasNextInt = scanner.hasNextInt();
			if(hasNextInt) {
				value = scanner.nextInt();
				isInputBad = false;
			}
			else {
				System.err.print("Invalid input. Enter a real number: ");
			}
			scanner.nextLine();
		}
		return value;
	}
	/**
	 * method to print the data table
	 * @param dto DataObject
	 * @since 1.0
	 */
	void printTable(DataObject dto) {
		System.out.println(l.print('+', 40));
//GIP
		System.out.print("[1] GIP/HIP");
		try {
			System.out.println(":	" + dto.ipToString(dto.getGIP()));
			System.out.println("    Subnet Class: " + dto.getSubnetClass());
			try {
				System.out.println("    DSM " + "	= " + dto.ipToString(dto.getDSM()) + "/"+ dto.getDSMslashValue());
				System.out.print("    ");
				for (int index = 0; index < dto.getDSM().length; index++) {
					for (int binaryDigit : dto.toBinary(dto.getDSM()[index]) ) {
						System.out.print(binaryDigit);
					}
					if(index+1<dto.getDSM().length) {
						System.out.print(".");
					}
				}
				System.out.println("\n");
			} catch (NullPointerException nullEx) {
				System.err.print("DSM couldn't be calculated!");
				switch(dto.getSubnetClass()) {
					case 'D':
						System.err.println("Class D is reserved for Multicasting.");
						break;
					case 'E':
						System.err.println("Class E is experimental.");
						break;
				}
			}
		} catch (NullPointerException nullEx) {
			System.out.println();
		}
//GSM
		try {
			System.out.println("[2] GSM " + "	= " + dto.ipToString(dto.getGSM()) +"/" + dto.getGSMslashValue());
			System.out.print("    ");
			for (int index = 0; index < dto.getGSM().length; index++) {
				for (int binaryDigit : dto.toBinary(dto.getGSM()[index]) ) {
					System.out.print(binaryDigit);
				}
				if(index+1<dto.getGSM().length) {
					System.out.print(".");
				}
			}
			System.out.println();
		} catch (NullPointerException nullEx) {
			System.out.println("[2] GSM");
		}
//s
		if (dto.getAmountOfBorrowedBits() == 0) {
			System.out.println("[3] borrowed bits (s)");
		} else {
			System.out.println("[3] borrowed bits (s) = " + dto.getAmountOfBorrowedBits());
		}
//S
		if (dto.getAmountOfSubnet() == 0) {
			System.out.println("[4] Amount of subnetID can be created (S)");
		} else {
			System.out.println("[4] Amount of subnetID can be created (S) = " + dto.getAmountOfSubnet());
			System.out.println();
		}
//h
		if (dto.getAmountOfHostBits() == 0) {
			System.out.println("[5] h");
		} else {
			System.out.println("[5] Amount of host bits (h) = " + dto.getAmountOfHostBits());
		}
//HIP
		if (dto.getAmountOfHostIP() == 0) {
			System.out.println("[6] Total HostIP");
		} else {
			System.out.println("[6] Total HostIP = " + dto.getAmountOfHostIP());
		}
//UsableHIP
		if (dto.getAmountOfUsableHIP() == 0) {
			System.out.println("[7] Usable HostIP");
		} else {
			System.out.println("[7] Usable HostIP = " + dto.getAmountOfUsableHIP());
		}
//SubnetID
		if (dto.getSID() != null) {
			System.out.println();
			System.out.println("    SID = " + dto.ipToString(dto.getSID()));
			System.out.print("    ");
			for(int[] octetInBinary : dto.getSIDinBinary()) {
				for(int binaryDigit : octetInBinary) {
					System.out.print(binaryDigit);
				}
				System.out.print(".");
			}
			System.out.println();
		}
//BroadcastID
		if (dto.getBID() != null) {
			System.out.println();
			System.out.println("    BroadcastID = " + dto.ipToString(dto.getBID()));
			System.out.println();
		}
//Closing & Reset
		System.out.println(l.print(40));
		System.out.println("<<PRESS [0] TO CLOSE OR [9] TO RESET>>");
		System.out.print("Enter your choice: ");
	}
	/**
	 * method to input an IP address
	 * @since 1.0
	 * @param keyboard {@code Scanner} object
	 * @return {@code int[]} IP address
	 */
	int[] inputIP(Scanner keyboard) {
		int[] IP = new int[4];//instance variable
		for(int index = 1; index <= 4; index++) {
			boolean inputIsBad = true;
			while (inputIsBad) {
				System.out.print("Enter the " + index + " octet and press enter: ");
				int inputedValue = inputInt(keyboard);
				if (inputedValue > 255) {
					System.err.println("an octet can't be more then 255 in decimal value!");
					inputIsBad = true;
				} else {
					IP[index-1] = inputedValue;
					inputIsBad = false;
				}
			}
		}
		return IP;
	}
}