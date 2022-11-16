/**
 * 
 * @author Tanzim Ahmed Sagar
 * @version 1.0
 */
public class DataObject {
	/** given IP */
	private int[] gIP;
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
	/** amount of subnets can be created */
	private int S;
	/** amount of host bits */
	private int h;
	/** amount of host IP can be created */
	private int hIP; 
	/** amount of usable host IP */
	private int usableHIp; 
	/** subnet IP, wired ID, Network IP */
	private int[] sIP = new int[4];
	//other variables
	private boolean calculateBorrowedBits = true;
	private char subnetClassMaskedOctetAmount;
	
	
	/**
	 * @return {@code int[]} the gIP
	 */
	public int[] getGIP() {
		return gIP;
	}
	/**
	 * modifier for gIP (given IP)
	 * also provokes modifiers for subnet class
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
				this.setSubnetClass();
			} else {
				System.err.println("setting GIP failed!");
			}
	}
	/**
	 * @return Class of the gIP (given IP) in {@code char}
	 */
	public char getSubnetClass() {
		return subnetClass;
	}
	/**
	 * updates subnetClass based on gIP
	 * also triggers all the modifiers of DSM in different form. 
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
		this.setDSMslashValue();
	}
	/**
	 * @return {@code int} the dSM in /? format
	 */
	public int getDSMslashValue() {
		return dSMslashValue;
	}
	/**
	 * modifier of {@code dSMslashValue}
	 */
	private void setDSMslashValue() {
		switch (subnetClass) {
		case 'A':
			this.dSMslashValue = 8;
			break;
		case 'B':
			this.dSMslashValue = 16;
			break;
		case 'C':
			this.dSMslashValue = 24;
			break;
		}
		if (dSM == null) {
			this.setDSM();
		}
	}
	/**
	 * @return {@code int[]} the dSM
	 */
	public int[] getDSM() {
		return dSM;
	}
	/**
	 * modifier for dSM (default Subnet Mask)
	 * triggers the modifier of dSMslashValue
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
		this.setDSMslashValue();
	}
	/**
	 * @return {@code int[]} the gSM
	 */
	public int[] getGSM() {
		return gSM;
	}
	/**
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
		this.setGSMslashValue();
	}
	/**
	 * @return the gSMslashValue {@code int}
	 */
	public int getGSMslashValue() {
		return gSMslashValue;
	}
	/**
	 * [PRIVATE] Modifier for gSM in /? form
	 * calculates value based on inputed GSM
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
		int borrowedbit = this.gSMslashValue - (((int)this.gSMslashValue/8) * 8);
		this.setAmountOfBorrowedBits(borrowedbit);
		this.setAmountOfHostBits(32 - this.gSMslashValue);
		if (this.dSMslashValue != 0) {
			this.setAmountOfBorrowedBits(this.gSMslashValue - this.dSMslashValue);
		}
	}
	/**
	 * Modifier for gSM in /? form
	 * also triggers the modifier of s (Amount Of Borrowed Bits)
	 * and the modifier of h (Amount Of host Bits)
	 * @param gSMslashValue the gSM in /? format
	 */
	public void setGSMslashValue(int gSMslashValue) {
		this.gSMslashValue = gSMslashValue;
		//calculating gSM in actual form
		this.gSM = new int[4];
		int completeMaskedOctet = (int)this.gSMslashValue/8;
		for (int index = 0; index < completeMaskedOctet; index++) {
			this.gSM[index] = 255;
		}
		int borrowedbit = this.gSMslashValue - (completeMaskedOctet * 8);
		String binary = "";
		for (int index = 0; index < borrowedbit; index++) {
			binary = binary + "1";
		}
		if (binary.length() != 8) {
			for (int index = binary.length(); index < 8; index++) { //TODO
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
	}
	/**
	 * @return the s (Amount Of Borrowed Bits)
	 */
	public int getAmountOfBorrowedBits() {
		return s;
	}
	/**
	 * Modifier for s (Amount Of Borrowed Bits)
	 * also triggers the modifier of S (Amount of subnet)
	 * @param s {@code int} (Amount Of Borrowed Bits)
	 */
	public void setAmountOfBorrowedBits(int s) {
		int temp_s = (int)s/8;
		if (s > 8 ) {
			this.calculateBorrowedBits = false;
			this.setGSMslashValue(((this.subnetClassMaskedOctetAmount + temp_s) * 8)+(s - (temp_s * 8)));
			this.calculateBorrowedBits = true;
		}
		this.s = s;
		this.setAmountOfSubnet();
	}
	/**
	 * Modifier for s (Amount Of Borrowed Bits) using S (Amount Of Subnets)
	 */
	private void setAmountOfBorrowedBits() {
		this.s = (int) Math.round(Math.log(S)/Math.log(2));
	}
	/**
	 * @return the S {@code int} (Amount Of Subnets)
	 */
	public int getAmountOfSubnet() {
		return S;
	}
	/**
	 * modifier for S (Amount of subnet)
	 * triggers a private modifier of s (Amount Of Borrowed Bits)
	 * @param S {@code int} Amount of subnet
	 */
	public void setAmountOfSubnet(int S) {
		this.S = S;
		this.setAmountOfBorrowedBits();
	}
	/**
	 * modifier for S (Amount of subnet)
	 */
	private void setAmountOfSubnet() {
		S = (int) Math.round(Math.pow(2, s));
	}
	/**
	 * @return the h  {@code int}
	 */
	public int getAmountOfHostBits() {
		return h;
	}
	/**
	 * modifier for h (amount of host bits)
	 * automatically triggers the modifiers of {@code hIP} and {@code usableHIp}
	 */
	public void setAmountOfHostBits(int h) {
		this.h = h;
		this.hIP = (int) Math.round(Math.pow(2, this.h));
		this.usableHIp = this.hIP - 2;
	}
	/**
	 * @return the hIP as {@code int}
	 */
	public int getAmountOfHostIP() {
		return hIP;
	}
/**
 * modifier for hIP (Amount Of Host IP)
 * @param hIP amount of host IP can be created
 */
	public void setAmountOfHostIP(int hIP) {
		this.hIP = hIP;
		this.h = (int) Math.round(Math.pow(hIP,(0-h)));
		this.usableHIp = this.hIP - 2;
	}
	/**
	 * @return the usableHIp {@code int}
	 */
	public int getAmountOfUsableHIP() {
		return usableHIp;
	}
	/**
	 * modifier for usableHIP
	 * also generates the value of hIP (Amount Of Host IP)
	 * and  the value of h (amount of host bits)
	 * @param usableHIp the usableHIp to set
	 */
	public void setAmountOfUsableHIP(int usableHIp) {
		this.usableHIp = usableHIp;
		this.hIP = usableHIp + 2;
	}
	/**
	 * @return the sID {@code int[]}
	 */
	public int[] getsIP() {
		return sIP;
	}
	/**
	 * method to turn an int[] representing an IP into string format
	 * @param ip IP address in {@code int[]} that needs to be in {@code string}
	 * @return String representation of the IP address in {@code int[]}
	 */
	public String ipToString(int[] ip) {
		String iP = "";
		int counter = 1;
		for (int octet : ip) {
			iP = iP + octet;
			if (counter < 4) {
				iP = iP + ".";
			}
			counter++;
		}
		return iP;
	}
	/**
	 * Method to turn an int to an int[] representing the binary format of the int.
	 * @param decimal Decimal version of the {@code int}
	 * @return {@code int[]} binary format
	 */
	public int[] toBinary(int decimalNumber){
		int binary[] = new int[8];
		int index = 0;
		while(decimalNumber > 0){
			binary[index++] = decimalNumber%2;
			decimalNumber = decimalNumber/2;
		}
		return binary;
	}
}// End of Class