import java.util.Scanner;
/**
 * @author Tanzim Ahmed Sagar
 * @version 1.0
 */
public class SubnetCalculator {
	private static Scanner keyboard = new Scanner(System.in);

	/**
	 * @param args command line arguments [not implemented yet]
	 */
	public static void main(String[] args) {
		Liner l = new Liner('+');
		DataObject dto = new DataObject();
		Methods method = new Methods();
		boolean restart = true;

		System.out.println(l.print(40));
		System.out.println("SUBNET CALCULATOR FOR CST8109");
		while (restart) {
			method.printTable(dto);
			int usrChoice = method.inputInt(keyboard);
			switch (usrChoice) {
				case 0:
					System.out.println(l.print(40));
					restart = false;
					break;
				case 1:
					System.out.println(l.print(40));
					dto.setGIP(method.inputIP(keyboard));
					break;
				case 2:
					System.out.println(l.print('-',40));
					System.out.println("[1] As actual IP address");
					System.out.println("[2] As /? format");
					System.out.println(l.print('-',40));
					System.out.print("Enter your choice: ");
					int usrChoiceForInputFormat = method.inputInt(keyboard);
					System.out.println(l.print(40));
					switch(usrChoiceForInputFormat) {
					case 1:
						dto.setGSM(method.inputIP(keyboard));
						break;
					case 2:
						System.out.print("Enter the gSM in /? format: ");
						dto.setGSMslashValue(method.inputInt(keyboard));
						break;
					default:
						System.err.println("Invalid Choice!");
						break;
					}
					break;
				case 3:
					System.out.println(l.print(40));
					System.out.print("Enter the amount of borrowed bits: ");
					dto.setAmountOfBorrowedBits(method.inputInt(keyboard));
					break;
				case 4:
					System.out.println(l.print(40));
					dto.setAmountOfSubnet(method.inputInt(keyboard));
					break;
				case 5:
					System.out.println(l.print(40));
					dto.setAmountOfHostBits(method.inputInt(keyboard));
					break;
				case 6:
					System.out.println(l.print(40));
					dto.setAmountOfHostIP(method.inputInt(keyboard));
					break;
				case 7:
					System.out.println(l.print(40));
					dto.setAmountOfUsableHIP(method.inputInt(keyboard));
				default:
					System.err.println("Unrecognized Choice!");
					break;
			} // end switch
		} // end while
	} // end main
} // end class

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
	void printTable(DataObject dto) {
		System.out.println(l.print('+', 40));
		System.out.print("[1] GIP/HIP");
		try {
			System.out.println(":	" + dto.ipToString(dto.getGIP()));
			System.out.println("Subnet Class:	" + dto.getSubnetClass());
			try {
				System.out.println("DSM /" + dto.getDSMslashValue() + "	= " + dto.ipToString(dto.getDSM()));
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
		try {
			System.out.println("[2] GSM /" + dto.getGSMslashValue() + "	= " + dto.ipToString(dto.getGSM()));
			
		} catch (NullPointerException nullEx) {
			System.out.println("[2] GSM");
		}
		if (dto.getAmountOfBorrowedBits() == 0) {
			System.out.println("[3] s");
		} else {
			System.out.println("[3] s = " + dto.getAmountOfBorrowedBits());
		}
		if (dto.getAmountOfSubnet() == 0) {
			System.out.println("[4] S");
		} else {
			System.out.println("[4] S = " + dto.getAmountOfSubnet());
		}
		if (dto.getAmountOfHostBits() == 0) {
			System.out.println("[5] h");
		} else {
			System.out.println("[5] h = " + dto.getAmountOfHostBits());
		}
		if (dto.getAmountOfHostIP() == 0) {
			System.out.println("[6] Total HIP");
		} else {
			System.out.println("[6] Total HIP = " + dto.getAmountOfHostIP());
		}
		if (dto.getAmountOfUsableHIP() == 0) {
			System.out.println("[7] Usable HIP");
		} else {
			System.out.println("[7] Usable HIP = " + dto.getAmountOfUsableHIP());
		}
		System.out.println(l.print(40));
		System.out.println("<<PRESS [0] TO CLOSE>>");
		System.out.print("Enter your choice: ");
	}
	int[] inputIP(Scanner keyboard) {
		int[] IP = new int[4];
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