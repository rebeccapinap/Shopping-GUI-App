/* Name: Rebecca Pina Partidas
Course: CNT 4714 – Fall 2023
Assignment title: Project 1 – Event-driven Enterprise Simulation
Date: Sunday September 17, 2023
*/

// Necessary libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.util.*;

public class Project1NileDotCom extends JFrame {
	
	// GUI variables
	private JFrame frame;
	private JLabel itemIdLabel, itemQtyLabel, itemDetailsLabel, orderTotalLabel;
	private JTextField itemIdTextField, itemQtyTextField, itemDetailsTextField, orderTotalTextField;
	private JButton findItemButton, addItemButton, viewCartButton, checkOutButton, emptyCartButton, exitAppButton;
	
	// Reference variables for event handlers
	private findItemButtonHandler findBHandler;
	private addItemButtonHandler addBHandler;
	private viewCartButtonHandler viewBHandler;
	private checkOutButtonHandler checkOutBHandler;
	private emptyCartButtonHandler emptyBHandler;
	private exitAppButtonHandler exitBHandler;
	
	// Static variables
	static String userInputID = "";
	static String userInputQtyStr = "";
	static String itemTitle = "";
	static int userInputQty = 0;
	static int itemQty = 0;
	static int itemDiscount = 0;
	static int itemCount = 1;
	static double itemPrice = 0;
	static double itemSubtotal = 0;
	static double cartSubtotal = 0;
	static double orderTaxAmount = 0;
	static double orderTotal = 0;
	
	static String [] itemIDarr;
	static String [] itemTitleArr;
	static int [] itemQtyArr;
	static int [] itemDiscountArr;
	static double [] itemPriceArr;
	static double [] itemSubtotalArr;
	
	// Constructor for GUI
	public Project1NileDotCom() {
		
		Color light_blue = new Color(124, 212, 253);
		
		frame = new JFrame();
		
		// Sets title of frame
		setTitle("Nile.com - Fall 2023");
		
		// Instantiates and set JLabel objects
		itemIdLabel = new JLabel("\t\tEnter Item ID For Item #" + itemCount + ":");
		itemQtyLabel = new JLabel("\t\tEnter Quantity For Item #" + itemCount + ":");
		itemDetailsLabel = new JLabel("\t\tDetails for Item #" + itemCount + ":");
		orderTotalLabel = new JLabel("\t\tOrder Subtotal for " + (itemCount - 1) + " item(s):");
		
		// Instantiates and set JTextFields objects
		itemIdTextField = new JTextField();
		itemQtyTextField = new JTextField();
		itemDetailsTextField = new JTextField();
		orderTotalTextField = new JTextField();
		
		// Instantiates and set JButton objects
		// Registers handlers
		findItemButton = new JButton("Find Item #" + itemCount);
		findBHandler = new findItemButtonHandler();
		findItemButton.addActionListener(findBHandler);
		
		addItemButton = new JButton("Add Item #" + itemCount + " To Cart");
		addBHandler = new addItemButtonHandler();
		addItemButton.addActionListener(addBHandler);
		
		viewCartButton = new JButton("View Cart");
		viewBHandler = new viewCartButtonHandler();
		viewCartButton.addActionListener(viewBHandler);
		
		checkOutButton = new JButton("Check Out");
		checkOutBHandler = new checkOutButtonHandler();
		checkOutButton.addActionListener(checkOutBHandler);
		
		emptyCartButton = new JButton("Empty Cart - Start a New Order");
		emptyBHandler = new emptyCartButtonHandler();
		emptyCartButton.addActionListener(emptyBHandler);
		
		exitAppButton = new JButton("Exit (Close App)");
		exitBHandler = new exitAppButtonHandler();
		exitAppButton.addActionListener(exitBHandler);
		
		// Makes text fields and buttons uneditable and disabled
		itemDetailsTextField.setEditable(false);
		orderTotalTextField.setEditable(false);
		addItemButton.setEnabled(false);
		viewCartButton.setEnabled(false);
		checkOutButton.setEnabled(false);
		
		// Gets a content pane for frame
		// Sets grid layout
		Container pane = getContentPane();
		GridLayout grid6by2 = new GridLayout(6,2,8,4);
		GridLayout grid4by2 = new GridLayout(4,2,8,4);
		GridLayout grid1by1 = new GridLayout(1,1,2,2);
		
		// Creates panels
		JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		
		// Sets layout for panels
		northPanel.setLayout(grid6by2);
		southPanel.setLayout(grid4by2);
		centerPanel.setLayout(grid1by1);
		
		// Adds labels and text fields to north panel
		northPanel.add(itemIdLabel);
		northPanel.add(itemIdTextField);
		northPanel.add(itemQtyLabel);
		northPanel.add(itemQtyTextField);
		northPanel.add(itemDetailsLabel);
		northPanel.add(itemDetailsTextField);
		northPanel.add(orderTotalLabel);
		northPanel.add(orderTotalTextField);
		
		// Set background color
		centerPanel.setBackground(light_blue);
		
		// Adds buttons to south panel
		southPanel.add(findItemButton);
		southPanel.add(addItemButton);
		southPanel.add(viewCartButton);
		southPanel.add(checkOutButton);
		southPanel.add(emptyCartButton);
		southPanel.add(exitAppButton);
		
		// Set background color
		southPanel.setBackground(light_blue);
		
		// Adds panels to content pane using BorderLayout
		pane.add(northPanel, BorderLayout.NORTH);
		pane.add(southPanel, BorderLayout.SOUTH);
		pane.add(centerPanel, BorderLayout.CENTER);
		
		// Centers frame on user's screen with defined height and width
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		
		int x = (screen.width - 700)/2;
		int y = (screen.height - 350)/2;
		
		setBounds(x, y, 700, 350);
	}
	
	private class findItemButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Check if button works
			//System.out.println("Find item button was clicked.");
			
			// Variable initializations
			File inputFile = new File("inventory.csv");
			FileReader inputFileReader = null;
			BufferedReader inputBufReader = null;
			Scanner scanner = null;
			String inventoryLine = "";
			String itemIDFromFile;
			boolean found = false;
			boolean itemInStock;
			boolean linesLeft = true;
			
			// Gets user input for item ID and item qty desired
			userInputID = itemIdTextField.getText();
			userInputQtyStr = itemQtyTextField.getText();
			userInputQty = Integer.parseInt(userInputQtyStr);
			
			try {
				// Reads from file
				inputFileReader = new FileReader(inputFile);
				inputBufReader = new BufferedReader(inputFileReader);
				
				// Declares arrays at beginning of app usage
				if (itemCount == 1) {
					itemIDarr = new String [20];
					itemTitleArr = new String [20];
					itemQtyArr = new int [20];
					itemDiscountArr = new int [20];
					itemPriceArr = new double [20];
					itemSubtotalArr = new double [20];
				}
				
				// Reads first line of file
				inventoryLine = inputBufReader.readLine();
				
				while (inventoryLine != null && linesLeft == true) {
					// Scanner with delimiter to take in parts of the input file
					scanner = new Scanner(inventoryLine).useDelimiter("\\s*, \\s*");
					
					// Gets item ID from line of file
					itemIDFromFile = scanner.next();
					
					if (userInputID.equals(itemIDFromFile)) {
						found = true;
						
						// Gets item title from line of file
						itemTitle = scanner.next();
						
						// Checks if item from line of file is in stock
						itemInStock = scanner.nextBoolean();
						
						// If item is not in stock shows error message
						if (itemInStock == false) {
							JOptionPane.showMessageDialog(null, "Sorry... that item is out of stock, please try another item.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
							itemIdTextField.setText("");
							itemQtyTextField.setText("");
							break;
						}
						
						// Gets item quantity from line of file
						itemQty = scanner.nextInt();
						
						// Gets item price from line of file
						itemPrice = scanner.nextDouble();
						
						// Checks if user input of quantity is valid
						// If not it will show error message
						if (userInputQty <= 0) {
							JOptionPane.showMessageDialog(null, "Please enter a non-zero, positive quantity.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
							itemQtyTextField.setText("");
						}
						// If it is valid, but the quantity that the user wants is more than what is in stock shows this error
						else if (userInputQty > itemQty) {
							JOptionPane.showMessageDialog(null, "Insufficient stock. Only " + itemQty + " on hand. Please reduce the quantity.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
							itemQtyTextField.setText("");
						}
						// If it is a valid quantity and there is enough inventory
						else {
							// Applies discount depending on how much user wants
							if (userInputQty >= 15) 
								itemDiscount = 20;
							else if (userInputQty >= 10)
								itemDiscount = 15;
							else if (userInputQty >= 5)
								itemDiscount = 10;
							else
								itemDiscount = 0;
							
							// Calculates subtotal of item and quantity with discount
							itemSubtotal = itemPrice * userInputQty * (1 - (itemDiscount/100.0));
							
							// Updates item details label for new item # it will show details for
							itemDetailsLabel.setText("\t\tDetails for Item #" + itemCount + ":");
							
							// Shows item details
							itemDetailsTextField.setText(userInputID + " " + itemTitle + " $" + String.format("%.2f", itemPrice) + " " + userInputQty + " " + itemDiscount + "% $" + String.format("%.2f", itemSubtotal));
							
							// Now allows user to add item to cart
							addItemButton.setEnabled(true);
							findItemButton.setEnabled(false);
						}
						
						break;
					}
					
					// Reads next line of file
					inventoryLine = inputBufReader.readLine();	
					
					// If next line is empty makes linesLeft false indicating end of file
					if (inventoryLine == null) {
						linesLeft = false;
					}
					
				}
				
				// Shows error message if item ID from user input doesn't match any in the file
				if (found == false) {
					JOptionPane.showMessageDialog(null, "Sorry... item ID " + userInputID + " is not in file, please try another item.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
					itemIdTextField.setText("");
					itemQtyTextField.setText("");
				}
			
			}
			
			catch(FileNotFoundException fileNotFoundException) {
				JOptionPane.showMessageDialog(null, "Error: File not found", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
			catch(IOException ioException) {
				JOptionPane.showMessageDialog(null, "Error: Problem reading from file", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
			catch(NumberFormatException numberFormatException) {
				JOptionPane.showMessageDialog(null, "Error: Invalid input for number of line items or quantity of items.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	private class addItemButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Check if button works
			//System.out.println("Add item button was clicked.");
			
			// Adds item details into arrays if item is added to cart
			itemIDarr [itemCount - 1] = userInputID;
			itemTitleArr [itemCount - 1] = itemTitle;
			itemQtyArr [itemCount - 1] = userInputQty;
			itemDiscountArr [itemCount - 1] = itemDiscount;
			itemPriceArr [itemCount - 1] = itemPrice;
			itemSubtotalArr [itemCount - 1] = itemSubtotal;
			
			// Calculates current subtotal of all items in cart and shows it to user
			cartSubtotal += itemSubtotal;
			orderTotalTextField.setText("$" + String.format("%.2f", cartSubtotal));
			
			// Shows message that says item has been added to cart
			JOptionPane.showMessageDialog(null, "Item #" + itemCount + " accepted. Added to your cart.", "Nile Dot Com - Item Confirmed", JOptionPane.INFORMATION_MESSAGE);
			
			// Increments what item user is currently on
			itemCount++;
			
			// Updates labels, buttons, and resets text fields and button availability
			itemIdLabel.setText("\t\tEnter Item ID For Item #" + itemCount + ":");
			itemQtyLabel.setText("\t\tEnter Quantity For Item #" + itemCount + ":");
			orderTotalLabel.setText("\t\tOrder Subtotal for " + (itemCount - 1) + " item(s):");
			
			itemIdTextField.setText("");
			itemQtyTextField.setText("");
			
			findItemButton.setText("Find Item #" + itemCount);
			addItemButton.setText("Add Item #" + itemCount + " To Cart");
			
			addItemButton.setEnabled(false);
			findItemButton.setEnabled(true);
			
			// If at least one item is in cart, user can view cart or check out
			if (itemCount == 2) {
				viewCartButton.setEnabled(true);
				checkOutButton.setEnabled(true);
			}
			
		}
	}
	
	private class viewCartButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Check if button works
			//System.out.println("View cart button was clicked.");
			
			// Variable initializations
			int i;
			String viewOutput = "";
			
			// Goes through item details arrays and creates message for output
			for (i = 0; i < itemCount - 1; i++) {
				viewOutput += (i + 1) + ". " + itemIDarr[i] + " " + itemTitleArr[i] + " $" + String.format("%.2f", itemPriceArr[i]) + " " + itemQtyArr[i] + " " + itemDiscountArr[i] + "% $" + String.format("%.2f", itemSubtotalArr[i]) + "\n";
			}
			
			// Shows items in cart to user
			JOptionPane.showMessageDialog(null, viewOutput, "Nile Dot Com - Current Shopping Cart Status", JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
	
	private class checkOutButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Check if button works
			//System.out.println("Check out button was clicked.");
			
			// Variable initializations
			int i;
			String todayStr = "";
			String stringForID = "";
			String orderID = "";
			String totalMessage = "";
			
			// Get date information
			Date today = new Date();
			DateFormat usaDateTime = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
			DateFormat frenchDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, Locale.FRANCE);
			
			FileWriter transactionFile;
			PrintWriter printWriter = null;
				
			
			try {
				// Writes to file
				transactionFile = new FileWriter("transactions.csv", true);
				printWriter = new PrintWriter (transactionFile);
				
				// Gets current date and time
				todayStr = usaDateTime.format(today);
				stringForID = frenchDateTime.format(today);
				
				StringBuilder orderIDSB = new StringBuilder(stringForID);
				StringBuilder outputSB = new StringBuilder();
				
				// Calculates tax for all items in cart and adds tax to subtotal
				orderTaxAmount = cartSubtotal * 0.06;
				orderTotal = cartSubtotal + orderTaxAmount;
				
				// Makes correct format for transaction id
				orderIDSB.deleteCharAt(orderIDSB.indexOf("/"));
				orderIDSB.deleteCharAt(orderIDSB.indexOf("/"));
				orderIDSB.deleteCharAt(orderIDSB.indexOf(" "));
				orderIDSB.deleteCharAt(orderIDSB.indexOf(":"));
				orderIDSB.deleteCharAt(orderIDSB.indexOf(":"));
				orderIDSB.delete(14, orderIDSB.length());
				
				orderID = orderIDSB.toString();
				
				// Makes titles for output message
				totalMessage = "Date: " + todayStr + "\n\nNumber of line items: " + (itemCount - 1) + "\n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal:\n\n";
				
				// Adds each item's details to output message and output file
				for (i = 0; i < itemCount - 1; i++) {
					totalMessage += (i + 1) + ". " + itemIDarr[i] + " " + itemTitleArr[i] + " $" + String.format("%.2f", itemPriceArr[i]) + " " + itemQtyArr[i] + " " + itemDiscountArr[i] + "% $" + String.format("%.2f", itemSubtotalArr[i]) + "\n";
					outputSB.append(orderID + ", " + itemIDarr[i] + ", " + itemTitleArr[i] + ", " + itemPriceArr[i] + ", " + itemQtyArr[i] + ", " + (itemDiscountArr[i] / 100.0) + ", $" + String.format("%.2f", itemSubtotalArr[i]) + ", " + todayStr + "\r\n");
				}
				
				outputSB.append("\n");
				printWriter.print(outputSB.toString());
			}
			
			catch(IOException ioexception) {
				JOptionPane.showMessageDialog(null, "Error: Problem writing to file.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
			}
			finally {
				printWriter.close();
			}
			
			// Adds final details of transaction to output message
			totalMessage += "\n\nOrder subtotal: $" + String.format("%.2f", cartSubtotal) + "\n\nTax Rate: 6%\n\nTax amount: $" + String.format("%.2f", orderTaxAmount) + "\n\nORDER TOTAL: $" + String.format("%.2f", orderTotal) + "\n\nThanks for shopping at Nile Dot Com!";
			
			// Output message with order details
			JOptionPane.showMessageDialog(null, totalMessage, "Nile Dot Com - FINAL INVOICE", JOptionPane.INFORMATION_MESSAGE);
			
			addItemButton.setEnabled(false);
			findItemButton.setEnabled(false);
			itemIdTextField.setEditable(false);
			itemQtyTextField.setEditable(false);
			
		}
	}
	
	private class emptyCartButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Check if button works
			//System.out.println("Empty cart button was clicked.");
			
			// Resets arrays
			itemIDarr = new String [0];
			itemTitleArr = new String [0];
			itemQtyArr = new int [0];
			itemDiscountArr = new int [0];
			itemPriceArr = new double [0];
			itemSubtotalArr = new double [0];
			
			// Resets variables
			userInputID = "";
			userInputQtyStr = "";
			itemTitle = "";
			userInputQty = 0;
			itemQty = 0;
			itemDiscount = 0;
			itemCount = 1;
			itemPrice = 0;
			itemSubtotal = 0;
			cartSubtotal = 0;
			
			// Resets labels, buttons, and textfields
			itemIdLabel.setText("\t\tEnter Item ID For Item #" + itemCount + ":");
			itemQtyLabel.setText("\t\tEnter Quantity For Item #" + itemCount + ":");
			itemDetailsLabel.setText("\t\tDetails for Item #" + itemCount + ":");
			orderTotalLabel.setText("\t\tOrder Subtotal for " + (itemCount - 1) + " item(s):");
			
			findItemButton.setText("Find Item #" + itemCount);
			addItemButton.setText("Add Item #" + itemCount + " To Cart");
			
			itemIdTextField.setText("");
			itemQtyTextField.setText("");
			itemDetailsTextField.setText("");
			orderTotalTextField.setText("");
			
			addItemButton.setEnabled(false);
			findItemButton.setEnabled(true);
			viewCartButton.setEnabled(false);
			checkOutButton.setEnabled(false);
			itemIdTextField.setEditable(true);
			itemQtyTextField.setEditable(true);
		}
	}
	
	private class exitAppButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Check if button works
			//System.out.println("Exit app button was clicked.");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		// Creates frame object and shows GUI
		JFrame onlineStore = new Project1NileDotCom();
		onlineStore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		onlineStore.setVisible(true);
	}

}
