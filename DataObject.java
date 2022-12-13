import java.util.Arrays;

/**
 * 
 * @author Tanzim Ahmed Sagar
 * @version 1.0
 */
public class DataObject {
	/** Total amount of binary bit in an octet of IPv4*/
	private final int TOTAL_BIT_IN_IPv4OCTET = 8;
	/** Total amount of Octet in IPv4*/
	private final int TOTAL_OCTET_IN_IPv4 = 4;
	/** given IP */
	private int[] gIP;
	/** given IP in boolean bits */
	private boolean[][] booleanGIP;
	/** subnet class [ABCDEF] */
	private char subnetClass;
	/** default subnet mask /? format */
	private int dSMslashValue;
	/** default subnet mask */
	private int[] dSM;
	/** given subnet mask */
	private int[] gSM;
	/** given subnet mask /? format */
	private int gSMslashValue;
	/** borrowed bits */
	private int s;
	/** amount of subnetID can be created */
	private int S;
	/** amount of host bits */
	private int h;
	/** amount of host IP can be created */
	private int hIP; 
	/** amount of usable host IP */
	private int usableHIp; 
	/** Subnetwork Address, wired ID, Network ID */
	private int[] sID;
	/** Subnetwork Address in Binary */
	private int[][] sIDinBinary;
	/** Broadcast ID */
	private int[] bID;
	//other variables
	private boolean calculateBorrowedBits = true;
	private char subnetClassMaskedOctetAmount;
	
	
	/**
	 * @since 1.0
	 * @return {@code int[]} gIP
	 */
	public int[] getGIP() {
		return gIP;
	}
	/**
	 * modifier for gIP (given IP)
	 * also provokes modifiers for subnetwork class
	 * @since 1.0
	 * @param gIP the gIP to set
	 */
	public void setGIP(int[] gIP) {
		this.gIP = new int[4];
		boolean ipIsValid = false;
		//validation
		for (int octet : gIP) {
			if (octet <= 255 && octet >= 0) {
				ipIsValid = true;
			} else {
				ipIsValid = false;
			}
		}
		if (ipIsValid) {
			this.gIP = gIP;
			this.setBooleanGIP();
			this.setSubnetClass();
			this.setSIDinBinary();
		} else {
			System.err.println("setting GIP failed!");
		}
	}
	/**
	 * @since 2.0
	 * @return the gIP in boolean[4][8]
	 */
	protected boolean[][] getBooleanGIP() {
		return booleanGIP;
	}
	/**
	 * Converts int[4] GivenIP to boolean[4][8]
	 * @since 2.0
	 */
	private void setBooleanGIP() {
		this.booleanGIP = new boolean[TOTAL_OCTET_IN_IPv4][TOTAL_BIT_IN_IPv4OCTET];
		for (int octetIndex = 0; octetIndex < booleanGIP.length; octetIndex++) {
			for (int octetBinaryIndex = 0; octetBinaryIndex < this.toBinary(this.gIP[octetIndex]).length; octetBinaryIndex++) {
				int[] temp = this.toBinary(this.gIP[octetIndex]);
				if (temp[octetBinaryIndex] != 0) {
					booleanGIP[octetIndex][octetBinaryIndex] = true;
				} else {
					booleanGIP[octetIndex][octetBinaryIndex] = false;
				}
			}
		}
	}
	/**
	 * @since 1.0
	 * @return Class of the gIP (given IP) in {@code char}
	 */
	public char getSubnetClass() {
		return subnetClass;
	}
	/**
	 * updates subnetClass based on gIP
	 * also triggers all the modifiers of DSM in different form.
	 * @since 1.0
	 */
	private void setSubnetClass() {
		if (gIP[0] == 0) {
			System.err.println("First octet of gIP is 0; network unavailable");
		} else if (gIP[0] > 0 && gIP[0] < 128) {
			subnetClass = 'A';
			subnetClassMaskedOctetAmount = 1;
		} else if (gIP[0] > 127 && gIP[0] <192) {
			subnetClass = 'B';
			subnetClassMaskedOctetAmount = 2;
		} else if (gIP[0] > 192 && gIP[0] < 223) {
			subnetClass = 'C';
			subnetClassMaskedOctetAmount = 3;
		} else if (gIP[0] > 224 && gIP[0] < 239) {
			subnetClass = 'D';
		} else if (gIP[0] > 240 && gIP[0] <= 254) {
			subnetClass = 'E';
		}
		//provoking other modifiers
		this.setDSMslashValue();
	}
	/**
	 * @since 1.0
	 * @return {@code int} the dSM in /? format
	 */
	public int getDSMslashValue() {
		return dSMslashValue;
	}
	/**
	 * modifier of {@code dSMslashValue}
	 * @since 1.0
	 */
	private void setDSMslashValue() {
		switch (subnetClass) {
		case 'A':
			this.dSMslashValue = TOTAL_BIT_IN_IPv4OCTET * 1;
			break;
		case 'B':
			this.dSMslashValue = TOTAL_BIT_IN_IPv4OCTET * 2;
			break;
		case 'C':
			this.dSMslashValue = TOTAL_BIT_IN_IPv4OCTET * 3;
			break;
		}
		//provoking other modifiers
		if (dSM == null) {
			this.setDSM();
		}
	}
	/**
	 * @since 1.0
	 * @return {@code int[]} the dSM
	 */
	public int[] getDSM() {
		return dSM;
	}
	/**
	 * modifier for dSM (default Subnet Mask)
	 * triggers the modifier of dSMslashValue
	 * @since 1.0
	 */
	private void setDSM() {
		dSM = new int[4];
		int oct = 0;
		switch (subnetClass) {
		case 'A':
			oct = 1;
			break;
		case 'B':
			oct = 2;
			break;
		case 'C':
			oct = 3;
			break;
		}
		for (int index = 0; index < oct; index++) {
			dSM[index] = 255;
		}
		//provoking other modifiers
		this.setDSMslashValue();
	}
	/**
	 * @since 1.0
	 * @return {@code int[]} the gSM
	 */
	public int[] getGSM() {
		return gSM;
	}
	/**
	 * @since 1.0
	 * @param gSM the gSM to set
	 */
	public void setGSM(int[] gSM) {
		this.gSM = new int[4];
		boolean ipIsValid;
		for (int octet : gSM) {
			if (octet < 256 && octet > -1) {
				ipIsValid = true;
			} else {
				ipIsValid = false;
			} // end if-else
			if (ipIsValid) {
				this.gSM = gSM;
			} else {
				System.err.println("setting GSM failed!");
			} // end if-else
		} //end of for loop
		//provoking other modifiers
		this.setGSMslashValue();
		this.setSIDinBinary();
	}
	/**
	 * @since 1.0
	 * @return the gSMslashValue {@code int}
	 */
	public int getGSMslashValue() {
		return gSMslashValue;
	}
	/**
	 * Modifier for gSM in /? form
	 * calculates value based on inputed GSM
	 * @since 1.0
	 */
	private void setGSMslashValue() {
		gSMslashValue = 0;
		for(int octet : this.gSM) {
			int binary[] = this.toBinary(octet);
			for (int digit : binary) {
				if (digit != 0) {
					gSMslashValue++;
				} //end if
			} //end binary[] iterator
		}//end gSM iterator
		//provoking other modifiers
		int borrowedbit = this.gSMslashValue - (((int)this.gSMslashValue/this.TOTAL_BIT_IN_IPv4OCTET) * this.TOTAL_BIT_IN_IPv4OCTET);
		this.setAmountOfBorrowedBits(borrowedbit);
		this.setAmountOfHostBits(32 - this.gSMslashValue);
		if (this.dSMslashValue != 0) {
			this.setAmountOfBorrowedBits(this.gSMslashValue - this.dSMslashValue);
		}
		this.setSIDinBinary();
	}
	/**
	 * Modifier for gSM in /? form
	 * also triggers the modifier of s (Amount Of Borrowed Bits)
	 * and the modifier of h (Amount Of host Bits)
	 * @since 1.0
	 * @param gSMslashValue the gSM in /? format
	 */
	public void setGSMslashValue(int gSMslashValue) {
		this.gSMslashValue = gSMslashValue;
		//calculating gSM in actual form
		this.gSM = new int[4];
		int completeMaskedOctet = (int)this.gSMslashValue/TOTAL_BIT_IN_IPv4OCTET;
		for (int index = 0; index < completeMaskedOctet; index++) {
			this.gSM[index] = 255;
		}
		int borrowedbit = this.gSMslashValue - (completeMaskedOctet * TOTAL_BIT_IN_IPv4OCTET);
		String binary = "";
		for (int index = 0; index < borrowedbit; index++) {
			binary = binary + "1";
		}
		if (binary.length() != TOTAL_BIT_IN_IPv4OCTET) {
			for (int index = binary.length(); index < TOTAL_BIT_IN_IPv4OCTET; index++) { 
				binary = binary + "0";
			}
		}
		this.gSM[completeMaskedOctet] = Integer.parseInt(binary,2);
		//provoking other modifiers
		if (this.calculateBorrowedBits) {
			this.setAmountOfBorrowedBits(borrowedbit);
		}
		this.setAmountOfHostBits(32 - this.gSMslashValue);
		if (this.dSMslashValue != 0 && this.calculateBorrowedBits) {
			this.setAmountOfBorrowedBits(this.gSMslashValue - this.dSMslashValue);
		}
		this.setSIDinBinary();
	}
	/**
	 * @since 1.0
	 * @return the s (Amount Of Borrowed Bits)
	 */
	public int getAmountOfBorrowedBits() {
		return s;
	}
	/**
	 * Modifier for s (Amount Of Borrowed Bits)
	 * also triggers the modifier of S (Amount of subnet)
	 * @since 1.0
	 * @param s {@code int} (Amount Of Borrowed Bits)
	 */
	public void setAmountOfBorrowedBits(int s) {
		int temp_s = (int)s/TOTAL_BIT_IN_IPv4OCTET;
		if (s > TOTAL_BIT_IN_IPv4OCTET ) {
			this.calculateBorrowedBits = false;
			this.setGSMslashValue(((this.subnetClassMaskedOctetAmount + temp_s) * TOTAL_BIT_IN_IPv4OCTET)+(s - (temp_s * TOTAL_BIT_IN_IPv4OCTET)));
			this.calculateBorrowedBits = true;
		}
		this.s = s;
		//provoking other modifiers
		this.setAmountOfSubnet();
	}
	/**
	 * Modifier for s (Amount Of Borrowed Bits) using S (Amount Of Subnet)
	 * @since 1.0
	 */
	private void setAmountOfBorrowedBits() {
		this.s = (int) Math.round(Math.log(S)/Math.log(2));
	}
	/**
	 * @since 1.0
	 * @return the S {@code int} (Amount Of Subnets)
	 */
	public int getAmountOfSubnet() {
		return S;
	}
	/**
	 * modifier for S (Amount of subnet)
	 * triggers a private modifier of s (Amount Of Borrowed Bits)
	 * @since 1.0
	 * @param S {@code int} Amount of subnet
	 */
	public void setAmountOfSubnet(int S) {
		this.S = S;
		//provoking other modifiers
		this.setAmountOfBorrowedBits();
	}
	/**
	 * modifier for S (Amount of subnetwork)
	 * @since 1.0
	 */
	private void setAmountOfSubnet() {
		S = (int) Math.round(Math.pow(2, s));
	}
	/**
	 * @since 1.0
	 * @return the h  {@code int}
	 */
	public int getAmountOfHostBits() {
		return h;
	}
	/**
	 * modifier for h (amount of host bits)
	 * automatically updates the value of {@code hIP} and {@code usableHIp}
	 * @since 1.0
	 */
	public void setAmountOfHostBits(int h) {
		this.h = h;
		this.hIP = (int) Math.round(Math.pow(2, this.h));
		this.usableHIp = this.hIP - 2;
	}
	/**
	 * @since 1.0
	 * @return the hIP as {@code int}
	 */
	public int getAmountOfHostIP() {
		return hIP;
	}
/**
 * modifier for hIP (Amount Of Host IP)
	 * @since 1.0
 * @param hIP amount of host IP can be created
 */
	public void setAmountOfHostIP(int hIP) {
		this.hIP = hIP;
		this.h = (int) Math.round(Math.pow(hIP,(0-h)));
		this.usableHIp = this.hIP - 2;
	}
	/**
	 * @since 1.0
	 * @return the usableHIp {@code int}
	 */
	public int getAmountOfUsableHIP() {
		return usableHIp;
	}
	/**
	 * modifier for usableHIP
	 * also generates the value of hIP (Amount Of Host IP)
	 * and  the value of h (amount of host bits)
	 * @since 1.0
	 * @param usableHIp the usableHIp to set
	 */
	public void setAmountOfUsableHIP(int usableHIp) {
		this.usableHIp = usableHIp;
		this.hIP = usableHIp + 2;
	}
	/**
	 * @since 1.0
	 * @return {@code int[]} sIDinBinary (Subnetwork Address)
	 */
	public int[] getSID() {
		return sID;
	}
	/**
	 * modifier for sID
	 * generates SID from boolean[4][8]
	 * @since 1.0
	 */
	private void setSID() {
		sID = new int[4];
		for (int octetIndex = 0; octetIndex < this.sIDinBinary.length; octetIndex++) {
			String octetInString = "";
			for (int octetBinaryIndex = 0; octetBinaryIndex < this.sIDinBinary[octetIndex].length; octetBinaryIndex++) {
				if (this.sIDinBinary[octetIndex][octetBinaryIndex] != 0) {
					octetInString = octetInString + 1;
				} else {
					octetInString = octetInString + 0;
				}
			}
			this.sID[octetIndex] = Integer.parseInt(octetInString,2);
		}
	}
	/**
	 * @since 1.0
	 * @return {@code int[][]} sIDinBinary (Subnetwork Address in binary form)
	 */
	public int[][] getSIDinBinary() {
		return sIDinBinary;
	}
	
	private void setSIDinBinary() {
		if (this.booleanGIP != null && this.gSM != null) {
			sIDinBinary = new int[TOTAL_OCTET_IN_IPv4][TOTAL_BIT_IN_IPv4OCTET];
			boolean[][] binaryGSM = new boolean[TOTAL_OCTET_IN_IPv4][TOTAL_BIT_IN_IPv4OCTET];
			for (int octetIndex = 0; octetIndex < binaryGSM.length; octetIndex++) {
				for (int octetBinaryIndex = 0; octetBinaryIndex < this.toBinary(this.gIP[octetIndex]).length; octetBinaryIndex++) {
					if (this.toBinary(this.gIP[octetIndex])[octetBinaryIndex] != 0) {
						binaryGSM[octetIndex][octetBinaryIndex] = true;
					} else {
						binaryGSM[octetIndex][octetBinaryIndex] = false;
					}
				}
			}
			String octetInString = "";
			for (int octetIndex = 0; octetIndex < this.sIDinBinary.length; octetIndex++) {
				for (int octetBinaryIndex = 0; octetBinaryIndex < this.sIDinBinary[octetIndex].length; octetBinaryIndex++) {
					if (booleanGIP[octetIndex][octetBinaryIndex] && binaryGSM[octetIndex][octetBinaryIndex]) {
						this.sIDinBinary[octetIndex][octetBinaryIndex] = 1;
						octetInString = octetInString + 1;
					} else {
						this.sIDinBinary[octetIndex][octetBinaryIndex] = 0;
						octetInString = octetInString + 0;
					}
				}
			}
			this.setSID();
			this.setBID();
		}
	}
	/**
	 * Generates the value of {@code bID}
	 * @since 2.0
	 * @param bID the bID to set
	 */
	private void setBID() {
		bID = new int[this.TOTAL_OCTET_IN_IPv4];
		int classNumber = 0;
		int[] temp;
		switch (subnetClass) {
		case 'A':
			classNumber = 1;
			for (int index = 0; index < classNumber; index++) { //copying sID based on the default subnet mask
				bID[index] = sID[index];
			}
			for (int index = classNumber + 1; index < TOTAL_OCTET_IN_IPv4; index++) { //filling the rest of the bID octets
				bID[index] = 255;
			}
			temp = Arrays.copyOf(sIDinBinary[classNumber], sIDinBinary[classNumber].length);
			for (int index = TOTAL_BIT_IN_IPv4OCTET; index > s; index--) {
				temp[index-1] = 1; 
			}
			bID[classNumber] = this.toDecimal(temp);
			break;
		case 'B':
			classNumber = 2;
			for (int index = 0; index < classNumber; index++) {
				bID[index] = sID[index];
			}
			for (int index = classNumber + 1; index < TOTAL_OCTET_IN_IPv4; index++) { //filling the rest of the bID octets
				bID[index] = 255;
			}
			temp = Arrays.copyOf(sIDinBinary[classNumber], sIDinBinary[classNumber].length);
			for (int index = TOTAL_BIT_IN_IPv4OCTET; index > s; index--) {
				temp[index-1] = 1; 
			}
			bID[classNumber] = this.toDecimal(temp);
			break;
		case 'C':
			classNumber = 3;
			for (int index = 0; index < classNumber; index++) {
				bID[index] = sID[index];
			}
			temp = Arrays.copyOf(sIDinBinary[classNumber], sIDinBinary[classNumber].length);
			for (int index = TOTAL_BIT_IN_IPv4OCTET; index > s; index--) {
				temp[index-1] = 1; 
			}
			bID[classNumber] = this.toDecimal(temp);
			break;
		}
	}
	/**
	 * @since 2.0
	 * @return the broadcast ID
	 */
	public int[] getBID() {
		return bID;
	}
	
	//Supplementary Methods
	
	/**
	 * method to turn an int[] representing an IP into string format
	 * @since 1.0
	 * @param ip IP address in {@code int[]} that needs to be in {@code string}
	 * @return String representation of the IP address in {@code int[]}
	 */
	public String ipToString(int[] ip) {
		String iP = "";
		int counter = 1;
		for (int octet : ip) {
			iP = iP + octet;
			if (counter < ip.length) {
				iP = iP + ".";
			}
			counter++;
		}
		return iP;
	}
	/**
	 * Method to turn an int to an int[] representing the binary format of the int.
	 * @since 1.0
	 * @param decimal Decimal version of the {@code int}
	 * @return {@code int[]} binary format
	 */
	public int[] toBinary(int decimalNumber){
		int binaryReversed[] = new int[TOTAL_BIT_IN_IPv4OCTET];
		int[] binary = new int[TOTAL_BIT_IN_IPv4OCTET];
		int index = 0;
		while(decimalNumber > 0){
			binaryReversed[index++] = decimalNumber%2;
			decimalNumber = decimalNumber/2;
		}
		int secoundIndex = 0;
		for (int firstIndex = binaryReversed.length-1; firstIndex >= 0; firstIndex--) {
			binary[secoundIndex] = binaryReversed[firstIndex];
			secoundIndex++;
		}
		return binary;
	}
	/**
	 * Converts a binary number to its decimal representation.
	 * @param binary an array of integers representing the binary number
	 * @return the decimal representation of the binary number
	 */
	public int toDecimal(int[] binary) {
	    int decimal = 0;
	    for (int i = 0; i < binary.length; i++) {
	        int bit = binary[binary.length - 1 - i];
	        if (bit == 1) {
	            decimal += Math.pow(2, i);
	        }
	    }
	    return decimal;
	}

}// End of Class