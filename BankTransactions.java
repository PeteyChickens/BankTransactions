import java.util.Scanner;
import java.math.BigDecimal;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

class BankTransactions {
	
	private static boolean writeAmount = true;
	
	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		//keeps prompting the user until "exit" is typed
		while(true) {
			System.out.print("Please enter in a command (Deposit, Withdraw, Balance, Exit): ");
			String command = userInput.nextLine();
			command = command.toLowerCase();	
			boolean acceptedCommand = checkCommand(command);
			if(acceptedCommand == true) {
				performCommand(command);
			}
		}
	}
	
	//param command: the command the user typed in to the command line
	//return: true if command is accepted, false if command is not accepted
	//Checks to see if the command the user typed in is an accepted command
	public static boolean checkCommand(String command) {
		if(command.equals("deposit") || command.equals("withdraw") || command.equals("balance") || command.equals("exit")) {
			return true;
		}
		return false;
	}
	
	//param command: the command the user tyed in to the command line
	//checks which command to perform
	public static void performCommand(String command) {
		if(command.equals("deposit") || command.equals("withdraw")) {
			depowithTransactions(command);
		}
		if(command.equals("balance")) {
			getBalance();
		}
		if(command.equals("exit")) {
			System.exit(0);
		}
	}
	
	//param command: the command the user typed in, either deposit or withdraw
	//either deposits or withdraws, changing the file by adding a number
	public static void depowithTransactions(String command) {
		Scanner numInput = new Scanner(System.in);
		boolean correctNum = false;
		BigDecimal tran = new BigDecimal(0);
		while(correctNum == false) {
			System.out.print("Please enter an amount to " + command + ": ");
			if(numInput.hasNextBigDecimal()) {
				tran = numInput.nextBigDecimal();
				correctNum = checkNumber(tran);
			}else{
				String s = numInput.next();
			}
		}
		String addToFile = tran.toString();
		String[] checkDec = addToFile.split("\\.");
		if(checkDec.length < 2) {
			addToFile = addToFile + ".00";
		}else {
			if(checkDec[1].length() == 1) {
				addToFile = addToFile + "0";
			}
		}
		BufferedWriter writeFile = null;
		try {
			writeFile = new BufferedWriter(new FileWriter("log.html", true));
			if(writeAmount == true) {
				writeFile.write("<b>Amount</b><br>\n");
				writeFile.newLine();
				writeAmount = false;
			}
			if(command.equals("deposit")) {
				writeFile.write(addToFile + "<br>\n");
			}else {
				writeFile.write("-" + addToFile + "<br>\n");
			}
			writeFile.close();
		}catch (IOException e) {
			System.err.println(e);
		}
	}
	
	//displays what your ending balance is by reading the amounts from the file and adding them all together
	public static void getBalance() {
		BigDecimal total = new BigDecimal(0);
		String nextLine = null;
		try {
			BufferedReader tranFile = new BufferedReader(new FileReader("log.html"));
			while((nextLine = tranFile.readLine()) != null) {
				if(nextLine.contains(".")) {
					if(nextLine.contains("br")) {
						if(nextLine.contains("-")) {
							BigDecimal temp = new BigDecimal(nextLine.substring(1, nextLine.length()-4));
							temp = temp.negate();
							total = total.add(temp);
						}else {
							BigDecimal temp = new BigDecimal(nextLine.substring(0, nextLine.length()-4));
							total = total.add(temp);
						}
					}else if(nextLine.contains("-")) {
						BigDecimal temp = new BigDecimal(nextLine.substring(25, nextLine.length()-11));
						temp = temp.negate();
						total = total.add(temp);
					}else {
						BigDecimal temp = new BigDecimal(nextLine.substring(24, nextLine.length()-10));
						total = total.add(temp);
					}
				}
			}
			tranFile.close();
			System.out.println("The current balance is: $" + total.toString());
		}catch(IOException e) {
			System.err.println(e);
		}
	}
	
	//param tran: number to check to see if it was input correctly
	//return: return true if the number was input correctly, false if number input wrong
	//this method checks to see if the number the user input is correctly input
	public static boolean checkNumber(BigDecimal tran) {
		if(tran.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}else {
			return checkDecimals(tran);
		}
	}
	
	//param tran: the number to check how many decimal places it is
	//return: returns true if 2 or less decimal places, false if more than 2 decimal places
	//this method checks to see if there are 2 or fewer decimal places in the number
	public static boolean checkDecimals(BigDecimal tran) {
		String s = tran.toString();
		String[] decSplit = s.split("\\.");
		if(decSplit.length > 1) {
			if(decSplit[1].length() <= 2) {
				return true;
			}else{
				return false;
			}
		}else {
			return true;
		}
	}
}