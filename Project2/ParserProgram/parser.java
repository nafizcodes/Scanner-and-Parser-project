//Contributers: Nafiz Imtiaz, Chris Dihenia, and Axel Alvarez

import java.io.File; // Imports the File class
import java.io.FileNotFoundException; // Imports error handling
import java.util.ArrayList; // Imports ArrayList
import java.util.List; // Imports List
import java.util.Scanner; // Imports Scanner
import java.util.Stack;

// Func Name: DFA_Scanner()
// Func Input: fileContent, valueList
// Func Output: tokenList <ArrayList>- (STRING),  valieList<ArrayList> - (STRING)
// Func Objective: (To scan the entire file, locate the individual tokens, and print as a list)
public class parser {

	// Scanner Function for DFA
	static List<String> DFA_Scanner(String fileContent, List<String> valueList) {

		// Initializing ArrayList tokenList
		List<String> tokenList = new ArrayList<String>();

		// Initializing ArrayList error and adding the error token into the list
		List<String> err = new ArrayList<String>();
		err.add("error.");

//		 Loops through String fileContent from start to finish, iterating index of
//		 fileContent String by 1 space for each loop of the For loop, and verifies the character in index i of
//		 the String for valid tokens

		for (int i = 0; i < fileContent.length(); i++) {
			// Checks for ' ', '\n', '\t' and iterates loop once if found
			// These are characters that are not added to the tokenList
			if ((fileContent.charAt(i) == ' ') || (fileContent.charAt(i) == '\n') || (fileContent.charAt(i) == '\t')) {
				continue;
			}

			// Instance for adding Division token to tokenList
			// and Instance for not adding comments to the tokenList
			else if (fileContent.charAt(i) == '/') {
				if (i + 1 >= fileContent.length()) {
					tokenList.add("div");
					valueList.add("/");
					break;
				}
				i++; // Iterate through string once token is identified

				if (fileContent.charAt(i) == '/') {
					while (fileContent.charAt(i) != '\n') {
						i++;
						if (i + 1 >= fileContent.length()) {
							break;
						}
					}
				} else if (fileContent.charAt(i) == '*') {
					i++;
					while (!((fileContent.charAt(i - 1) == '*') && (fileContent.charAt(i) == '/'))) {
						if (i + 1 >= fileContent.length()) {
							return err;
						}
						i++;
					}
				} else {
					tokenList.add("div");
					valueList.add("/");
				}
			}

//			 Instance utilized to differentiate tokens into their respective types using the provided automata
//			 for '(', ')', '+', '-' '*' and adds their corresponding token to
//			 tokenList if identified
			else if (fileContent.charAt(i) == '(') {
				tokenList.add("lparen");
				valueList.add("(");
			} else if (fileContent.charAt(i) == ')') {
				tokenList.add("rparen");
				valueList.add(")");
			} else if (fileContent.charAt(i) == '+') {
				tokenList.add("plus");
				valueList.add("+");
			} else if (fileContent.charAt(i) == '-') {
				tokenList.add("minus");
				valueList.add("-");
			} else if (fileContent.charAt(i) == '*') {
				tokenList.add("times");
				valueList.add("*");
			}

//			 Instance utilized to identify assign token
//			 If ':' is found without '=' proceeding it, then force error
			else if (fileContent.charAt(i) == ':') {
				if (i + 1 >= fileContent.length()) {
					return err;
				}

				i++;

				if (fileContent.charAt(i) == '=') {
					tokenList.add("assign");
					valueList.add(":=");
					continue;
				} else {
					return err;
				}
			}

//			 Instance to handle decimal numbers 
//			 If '.' is found without numbers proceeding it, then error is returned
			else if (fileContent.charAt(i) == '.') {
				String num_val = "";
				if (i + 1 >= fileContent.length()) {
					return err;
				}
				if (!Character.isDigit(fileContent.charAt(i + 1))) {
					return err;
				}
				num_val += fileContent.charAt(i);
				i++;
				while (Character.isDigit(fileContent.charAt(i))) {
					if (i + 1 >= fileContent.length()) {
						tokenList.add("number");
						valueList.add(num_val);
						break;
					}
					num_val += fileContent.charAt(i);
					if (!(Character.isDigit(fileContent.charAt(i + 1)))) {
						break;
					}

					i++;
				}
				if (i + 1 >= fileContent.length()) {
					break;
				}
				tokenList.add("number");
				valueList.add(num_val);
				continue;
			}

//			 Instance for the validation of the "number" token
//			 Checks for real numbers and/or decimal numbers that begin with a number
			else if (Character.isDigit(fileContent.charAt(i))) {
				String num_val = "";
				if (i + 1 >= fileContent.length()) {
					tokenList.add("number");
					valueList.add(num_val);
					break;
				}
				while (Character.isDigit(fileContent.charAt(i))) {
					if (i + 1 >= fileContent.length()) {
						tokenList.add("number");
						valueList.add(num_val);
						break;
					}
					num_val += fileContent.charAt(i);
					if (!(Character.isDigit(fileContent.charAt(i + 1)))) {
						break;
					}
					i++;
				}
				if (i + 1 >= fileContent.length()) {
					continue;
				}
				if (fileContent.charAt(i + 1) == '.') {
					i++;
					num_val += fileContent.charAt(i);
					if (i + 1 >= fileContent.length()) {
						return err;
					}
					i++;
					if (!Character.isDigit(fileContent.charAt(i))) {
						return err;
					}
					while (Character.isDigit(fileContent.charAt(i))) {
						if (i + 1 >= fileContent.length()) {
							tokenList.add("number");
							valueList.add(num_val);
							break;
						}
						num_val += fileContent.charAt(i);
						if (!(Character.isDigit(fileContent.charAt(i + 1)))) {
							break;
						}
						i++;
					}
					if (i + 1 >= fileContent.length()) {
						continue;
					}
				}
				tokenList.add("number");
				valueList.add(num_val);
				continue;
			}

			// Checks for id names and adds ID token to tokenList if found
			// Instance to validate the "read" and "write" tokens
			else if (Character.isLetter(fileContent.charAt(i))) {
				String idCheck = "";
				if (i + 1 >= fileContent.length()) {
					tokenList.add("id");
					valueList.add(idCheck);
					continue;
				}

				// Forcing DFA requirements for Next State of id's to be followed
				while (Character.isLetter(fileContent.charAt(i)) || Character.isDigit(fileContent.charAt(i))) {
					if (i + 1 >= fileContent.length()) {
						break;
					}
					idCheck += fileContent.charAt(i);
					if (!(Character.isLetter(fileContent.charAt(i + 1))
							|| Character.isDigit(fileContent.charAt(i + 1)))) {
						break;
					}
					i++;
				}

				// Verification that the sequence of tokens is for sure "read" or "write"
				if (idCheck.equals("read")) {
					tokenList.add("read");
					valueList.add(idCheck);
					continue;
				}
				if (idCheck.equals("write")) {
					tokenList.add("write");
					valueList.add(idCheck);
					continue;
				} else
					tokenList.add("id");
				valueList.add(idCheck);
			}

			// If non-valid token is identified, then error is returned
			else {
				return err;
			}
		}

		// If the For Loop is completed, then tokenList list is returned
		return tokenList;
	}

	// Function Used to Print parse_stack to Command Line in Reverse Order
	static void reversePrint(Stack<String> parse_stack) {

		if (parse_stack.isEmpty())
			return;

		// Instantiate Temporary Stack
		Stack<String> temporaryStack = new Stack<>();
		String tab = "";

		// Function to copy elements from parse_stack to temporaryStack
		while (parse_stack.size() > 0) {
			temporaryStack.push(parse_stack.pop());
		}

		// Function to Print Elements from Temporary Stack (with proper custom tabbing
		// to match Task Form)
		while (temporaryStack.size() > 0) {
			String x = temporaryStack.peek();

			if (temporaryStack.peek().contains("</")) {
				tab = tab.substring(0, tab.length() - 3);
				System.out.println(tab + x);
				temporaryStack.pop();
			}

			else if (temporaryStack.peek().contains("<")) {
				System.out.println(tab + x);
				temporaryStack.pop();
				tab += "   ";
			}

			else {
				System.out.println(tab + x);
				temporaryStack.pop();
			}
		}
	}

	// Function to match identified tokens when its specific parse block is reached
	static void match(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		String val = (valueList.get(0));
		parse_stack.add(val);
		valueList.remove(0);
		tokenList.remove(0);
	}

	// Function call for Program Block
	static void program(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		tokenList.add("$$");
		parse_stack.add("<Program>");
		if (tokenList.get(0).equals("id") || tokenList.get(0).equals("read") || tokenList.get(0).equals("write")
				|| tokenList.get(0) == "$$") {
			stmt_list(tokenList, valueList, parse_stack);

			if (!(tokenList.get(0).equals("$$")))
				parse_stack.add("Error!");
		} else
			parse_stack.add("Error.");

		parse_stack.add("</Program>");
	}

	// Function call for Statement_List Block
	static void stmt_list(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<stmt_list>");
		if (tokenList.get(0).equals("id") || tokenList.get(0).equals("read") || tokenList.get(0).equals("write")) {
			stmt(tokenList, valueList, parse_stack);
			stmt_list(tokenList, valueList, parse_stack);
		} else if (tokenList.get(0).equals("$$")) {
			// epsilon production
		} else
			parse_stack.add("Error.");

		parse_stack.add("</stmt_list>");
	}

	// Function call for Statement Block
	static void stmt(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<stmt>");
		if (tokenList.get(0).equals("id")) {

			parse_stack.add("<id>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</id>");

			if ((tokenList.get(0).equals("assign"))) {
				parse_stack.add("<assign>");
				match(tokenList, valueList, parse_stack);

				parse_stack.add("</assign>");
				expr(tokenList, valueList, parse_stack);
			} else {
				parse_stack.add("Error.");
			}
		} else if (tokenList.get(0).equals("read")) {
			if (tokenList.get(1).equals("id")) {
				parse_stack.add("<read>");
				match(tokenList, valueList, parse_stack);

				parse_stack.add("</read>");
				parse_stack.add("<id>");
				match(tokenList, valueList, parse_stack);

				parse_stack.add("</id>");
			} else {
				parse_stack.add("Error.");
			}
		} else if (tokenList.get(0).equals("write")) {

			parse_stack.add("<write>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</write>");
			expr(tokenList, valueList, parse_stack);
		} else {
			parse_stack.add("Error");
		}

		parse_stack.add("</stmt>");
	}

	// Function call for Expression Block
	static void expr(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<expr>");

		if (tokenList.get(0).equals("id") || tokenList.get(0).equals("number") || tokenList.get(0).equals("lparen")) {
			term(tokenList, valueList, parse_stack);
			term_tail(tokenList, valueList, parse_stack);

		} else {
			parse_stack.add("Error");
		}

		parse_stack.add("</expr>");
	}

	// Function call for Term_tail Block
	static void term_tail(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<term_tail>");
		if (tokenList.get(0).equals("plus") || tokenList.get(0).equals("minus")) {
			add_op(tokenList, valueList, parse_stack);
			term(tokenList, valueList, parse_stack);
			term_tail(tokenList, valueList, parse_stack);

		} else if (tokenList.get(0).equals("id") || tokenList.get(0).equals("read") || tokenList.get(0).equals("write")
				|| tokenList.get(0) == "$$") {
			// epsilon production (SKIP)
		} else {
			parse_stack.add("Error");
		}
		parse_stack.add("</term_tail>");
	}

	// Function call for Term Block
	static void term(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<term>");
		if (tokenList.get(0).equals("id") || tokenList.get(0).equals("number") || tokenList.get(0).equals("lparen")) {
			factor(tokenList, valueList, parse_stack);
			factor_tail(tokenList, valueList, parse_stack);

		} else {
			parse_stack.add("Error");
		}
		parse_stack.add("</term>");
	}

	// Function call for Factor_tail Block
	static void factor_tail(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<factor_tail>");

		if (tokenList.get(0).equals("times") || tokenList.get(0).equals("div")) {
			mult_op(tokenList, valueList, parse_stack);
			factor(tokenList, valueList, parse_stack);
			factor_tail(tokenList, valueList, parse_stack);

		} else if (tokenList.get(0).equals("plus") || tokenList.get(0).equals("minus")
				|| tokenList.get(0).equals("rparen") || tokenList.get(0).equals("id") || tokenList.get(0).equals("read")
				|| tokenList.get(0).equals("write") || tokenList.get(0) == "$$") {
			// epsilon production (SKIP)
		} else {
			parse_stack.add("Error.");
		}

		parse_stack.add("</factor_tail>");
	}

	// Function call for Factor Block
	static void factor(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<factor>");

		if (tokenList.get(0).equals("id")) {
			parse_stack.add("<id>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</id>");
		}

		else if (tokenList.get(0).equals("number")) {
			parse_stack.add("<number>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</number>");

		} else if (tokenList.get(0).equals("lparen")) {
			parse_stack.add("<lparen>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</lparen>");
			expr(tokenList, valueList, parse_stack); // CHECK THIS LATER
			parse_stack.add("<rparen>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</rparen>");
		}

		else
			parse_stack.add("Error.");

		parse_stack.add("</factor>");
	}

	// Function call for Add Operator Block (Plus and Minus)
	static void add_op(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<add_op>");

		if (tokenList.get(0).equals("plus")) {
			parse_stack.add("<plus>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</plus>");
		}

		else if (tokenList.get(0).equals("minus")) {
			parse_stack.add("<minus>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</minus>");
		} else
			parse_stack.add("Error");

		parse_stack.add("</add_op>");
	}

	// Function call for Multiply Operator Block (Times and Division)
	static void mult_op(List<String> tokenList, List<String> valueList, Stack<String> parse_stack) {
		parse_stack.add("<mult_op>");

		if (tokenList.get(0).equals("times")) {
			parse_stack.add("<times>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</times>");
		}

		else if (tokenList.get(0).equals("div")) {
			parse_stack.add("<div>");
			match(tokenList, valueList, parse_stack);
			parse_stack.add("</div>");
		} else
			parse_stack.add("Error.");

		parse_stack.add("</mult_op>");
	}

	public static void main(String[] args) throws Exception {

		// Instantiating a string variable to hold the content within the textfile
		String fileContent = "";

		// File Opener
		try {
			// Take the file argument from command line
			File fileOpen = new File(args[0]);
			Scanner fileReader = new Scanner(fileOpen);
			while (fileReader.hasNextLine()) {
				fileContent += fileReader.nextLine() + "\n";
			}
			// Closing the File
			fileReader.close();
		}

		// Print Statement If Entered File Does Not Exist
		catch (FileNotFoundException e) {
			System.out.println("Error. File does not exist");
			System.exit(0);
		}

		// Initializing an ArrayList named valueList to hold the physical token in a
		// separate list
		List<String> valueList = new ArrayList<String>();

		// Initializing an ArrayList named tokenList and assigning it to the tokenList
		// of
		// the DFA_Scanner(String
		// fileContent, valueList) function
		List<String> tokenList = DFA_Scanner(fileContent, valueList);

		// Initializing a Stack to store the tokenList of our Parser
		Stack<String> parse_stack = new Stack<String>();

		// Starting the Parser
		program(tokenList, valueList, parse_stack);

		// Error Checking and Calling the Print Function for the Parser
		if (parse_stack.contains("Error.")) {
			System.out.println("Error.");
		} else
			reversePrint(parse_stack);
	}
}