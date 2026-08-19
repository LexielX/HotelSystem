import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

	
	public class NewBookingPanel extends JPanel {
	
		private static final long serialVersionUID = 1L;
		private JTextField FNameFIeld;
		private JTextField LNameField;
		private JTextField PNumField;
		private JTextField emailField;	
		private JComboBox roomTypeCBX;
		private JComboBox<String> availableRoomCBX;
		private JSpinner checkInSpinner;
		private JSpinner checkOutSPinner;
		private JTextField txtTotalAmount;
		private JComboBox PMethodCBX;
		private JLabel roomImageLabel;
		private JLabel roomNameLabel;
		// ADDED: para sa number of guests
		private JSpinner guestCountSpinner;
		JCheckBox chckbxSeniorPwd;
		private JLabel lblExtraCharge;
		private JTextField txtChargeAmount;
		private int currentCustomerId = -1;
		
	
		public NewBookingPanel() {
			setForeground(new Color(31, 26, 85));
			setSize(1924, 1083);
			setLayout(null);
			
			JLabel lblNewBooking = new JLabel("CREATE NEW BOOKING\n");
			lblNewBooking.setForeground(new Color(0, 0, 68));
			lblNewBooking.setBounds(44, 20, 364, 28);
			lblNewBooking.setFont(new Font("SansSerif", Font.BOLD, 20));
			add(lblNewBooking);
			
			//ADDED: search existing customer
			JButton btnSearchCustomer = new JButton("Search Customer");
			btnSearchCustomer.setBounds(331, 58, 241, 25);
			btnSearchCustomer.setFont(new Font("SansSerif", Font.BOLD, 12));
			btnSearchCustomer.setBackground(new Color(0, 128, 255));
			btnSearchCustomer.setForeground(Color.WHITE);
			btnSearchCustomer.addActionListener(e -> showCustomerSearchDialog());
			add(btnSearchCustomer);
			
			JLabel lblcostumerInformation = new JLabel("--Customer Information");
			lblcostumerInformation.setForeground(new Color(0, 0, 68));
			lblcostumerInformation.setFont(new Font("SansSerif", Font.BOLD, 18));
			lblcostumerInformation.setBounds(101, 58, 241, 17);
			add(lblcostumerInformation);
			
			JLabel lblFirst = new JLabel("First Name:");
			lblFirst.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblFirst.setBounds(193, 98, 104, 17);
			add(lblFirst);
			
			FNameFIeld = new JTextField();
			FNameFIeld.setBounds(331, 93, 241, 21);
			add(FNameFIeld);
			FNameFIeld.setColumns(10);
			// ADDED: auto uppercase at block numbers sa First Name
			FNameFIeld.addKeyListener(new java.awt.event.KeyAdapter() {
			    public void keyTyped(java.awt.event.KeyEvent e) {
			        char c = e.getKeyChar();
			        // block numbers - hindi makakapag type ng number
			        if (c >= '0' && c <= '9') {
			            e.consume();
			        }
			        // auto uppercase - kahit hindi pinindot caps lock
			        if (Character.isLetter(c)) {
			            e.setKeyChar(Character.toUpperCase(c));
			        }
			    }
			});
			
			JLabel lblLastName = new JLabel("Last Name:");
			lblLastName.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblLastName.setBounds(193, 140, 104, 17);
			add(lblLastName);
			
			JLabel lblPhoneNumber = new JLabel("Phone Number:");
			lblPhoneNumber.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblPhoneNumber.setBounds(193, 186, 128, 17);
			add(lblPhoneNumber);
			
			JLabel lblEmail = new JLabel("Email:");
			lblEmail.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblEmail.setBounds(200, 236, 121, 17);
			add(lblEmail);
			
			JLabel lblroomSelection = new JLabel("--Room Selection");
			lblroomSelection.setForeground(new Color(0, 0, 68));
			lblroomSelection.setFont(new Font("SansSerif", Font.BOLD, 18));
			lblroomSelection.setBounds(101, 274, 214, 17);
			add(lblroomSelection);
			
			JLabel lblRoomType = new JLabel("Room type:");
			lblRoomType.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblRoomType.setBounds(193, 313, 121, 17);
			add(lblRoomType);
			
			JLabel lblAvailableRoom = new JLabel("Available Room");
			lblAvailableRoom.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblAvailableRoom.setBounds(193, 359, 121, 17);
			add(lblAvailableRoom);
			
			// ADDED: No. of Guests label - katabi ng room selection
			JLabel lblGuestCount = new JLabel("No. of Guests:");
			lblGuestCount.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblGuestCount.setBounds(193, 401, 121, 17);
			add(lblGuestCount);
			
			JLabel lbldates = new JLabel("--Dates");
			lbldates.setForeground(new Color(0, 0, 68));
			lbldates.setFont(new Font("SansSerif", Font.BOLD, 18));
			lbldates.setBounds(101, 428, 214, 17);
			add(lbldates);
			
			JLabel lblCheck = new JLabel("Check-in Date");
			lblCheck.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblCheck.setBounds(193, 455, 121, 17);
			add(lblCheck);
			
			JLabel lblCheckoutDate = new JLabel("Check-out Date");
			lblCheckoutDate.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblCheckoutDate.setBounds(193, 501, 128, 17);
			add(lblCheckoutDate);
			
			JLabel lblNewLabel = new JLabel("Payment");
			lblNewLabel.setForeground(new Color(0, 0, 68));
			lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
			lblNewLabel.setBounds(101, 540, 214, 17);
			add(lblNewLabel);
			
			JLabel lblPaymentMethod = new JLabel("Payment Method");
			lblPaymentMethod.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblPaymentMethod.setBounds(193, 610, 147, 17);
			add(lblPaymentMethod);
			
			JLabel lblCheckOut = new JLabel("Total Amount");
			lblCheckOut.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblCheckOut.setBounds(193, 648, 121, 17);
			add(lblCheckOut);
			
			LNameField = new JTextField();
			LNameField.setBounds(331, 140, 241, 21);
			add(LNameField);
			LNameField.setColumns(10);
			// ADDED: auto uppercase at block numbers sa Last Name
			LNameField.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
			        char c = e.getKeyChar();
			        // block numbers - hindi makakapag type ng number
			        if (c >= '0' && c <= '9') {
			            e.consume();
			        }
			        // auto uppercase - kahit hindi pinindot caps lock
			        if (Character.isLetter(c)) {
			            e.setKeyChar(Character.toUpperCase(c));
			        }
			    }
			});
			
			PNumField = new JTextField();
			PNumField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					
					//will ignore if not number
					if (!Character.isDigit(c)) {
						e.consume();
					}
					//para hindi pwedeng lumagpas ng 11 digit
					if (PNumField.getText().length() >= 11) {
						e.consume();
					}
				}
			});
			PNumField.setBounds(331, 186, 241, 21);
			add(PNumField);
			PNumField.setColumns(10);
			//same sa nameField but reverse para naman numbers lang ang pwedeng i type
			
			
			emailField = new JTextField();
			emailField.setBounds(331, 236, 241, 21);
			add(emailField);
			emailField.setColumns(10);
			
			//room type combo box drop down
			String[] roomTypes = {
					"Standard Room - ₱2,500/day",
					"Junior suite - ₱3,500/day",
					"Executive Suite - ₱5,000/day",
					"Presidential Suite - ₱8,000/day"
			};
			
			roomTypeCBX = new JComboBox(roomTypes);
			roomTypeCBX.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadAvailableRooms(); //everytime you change the room types the available room number change also
					calculateTotalAmount(); // this method will calculate naman yung total amount everytime
					
					// dto lalabas ung mga pic ng room pag sinelect mo ano gusto mo
					String selectedRoom = (String) roomTypeCBX.getSelectedItem();
					String imageFile = "";
					
					if (selectedRoom.contains("Standard Room")) {
					    imageFile = "Standard room.jpg";
					    roomNameLabel.setText("Standard Room");
					} else if (selectedRoom.contains("Junior")) {
					    roomNameLabel.setText("Junior Suite");
					    imageFile = "Junior room.jpg";
					} else if (selectedRoom.contains("Executive")) {
					    imageFile = "Executive room.jpg";
					    roomNameLabel.setText("Executive Suite");
					} else if (selectedRoom.contains("Presidential")) {
					    imageFile = "Presindetial room.jpg";
					    roomNameLabel.setText("Presidential Suite");
					}
					
					roomImageLabel.setIcon(createScaledIcon(imageFile, 815, 649));
				}
			});
			roomTypeCBX.setBounds(331, 310, 241, 26);
			add(roomTypeCBX);
			
			// ADDED: room name label sa taas ng image
			roomNameLabel = new JLabel("");
			roomNameLabel.setBounds(654, 5, 815, 20);
			roomNameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
			roomNameLabel.setForeground(new Color(0, 0, 68));
			roomNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
			add(roomNameLabel);
			
			// ADDED: setup ng image label na lalabas sa tabi ng combobox
			roomImageLabel = new JLabel();
			roomImageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
			roomImageLabel.setBounds(654, 35, 848, 698);
			roomImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			roomImageLabel.setHorizontalAlignment(JLabel.RIGHT);
			add(roomImageLabel);
						
			availableRoomCBX = new JComboBox<>();
			availableRoomCBX.setBounds(331, 356, 241, 26);
			add(availableRoomCBX);
			
			// CHANGED: Replaced hard limits with a standard 1-20 range
			guestCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
			guestCountSpinner.setBounds(334, 401, 74, 22);
			//will trigger the calculateTotalAmount method when clicking arrow
			guestCountSpinner.addChangeListener(e -> calculateTotalAmount());
			add(guestCountSpinner);
			
			//display the total charged amount
			lblExtraCharge = new JLabel("");
			lblExtraCharge.setFont(new Font("SansSerif", Font.ITALIC, 12));
			lblExtraCharge.setForeground(new Color(224, 27, 36)); // Red text
			lblExtraCharge.setBounds(416, 401, 258, 22);
			add(lblExtraCharge);
			
			// 1. Get today's date and reset the time to midnight
			java.util.Calendar today = java.util.Calendar.getInstance();
			today.set(java.util.Calendar.HOUR_OF_DAY, 0);
			today.set(java.util.Calendar.MINUTE, 0);
			today.set(java.util.Calendar.SECOND, 0);
			today.set(java.util.Calendar.MILLISECOND, 0);
			Date minDate = today.getTime();

			// 2. Pass minDate as the second parameter (the Minimum limit)
			SpinnerDateModel checkInModel = new SpinnerDateModel(minDate, minDate, null, java.util.Calendar.DAY_OF_MONTH);
			SpinnerDateModel checkOutModel = new SpinnerDateModel(minDate, minDate, null, java.util.Calendar.DAY_OF_MONTH);

			checkInSpinner = new JSpinner(checkInModel);
			checkInSpinner.setBounds(331, 455, 241, 22);
			checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd"));
			add(checkInSpinner);

			checkOutSPinner = new JSpinner(checkOutModel);
			checkOutSPinner.setBounds(331, 501, 241, 22);
			checkOutSPinner.setEditor(new JSpinner.DateEditor(checkOutSPinner, "yyyy-MM-dd"));
			add(checkOutSPinner);
			
			// ADDED: loadAvailableRooms() so changing the date refreshes the dropdown
			checkInSpinner.addChangeListener(e -> {
			    calculateTotalAmount();
			    loadAvailableRooms(); 
			});

			checkOutSPinner.addChangeListener(e -> {
			    calculateTotalAmount();
			    loadAvailableRooms();
			});

			txtTotalAmount = new JTextField();
			txtTotalAmount.setBounds(331, 648, 241, 21);
			txtTotalAmount.setEditable(false); // make it read only so they cant edit the price
			txtTotalAmount.setFont(new Font("SansSerif", Font.BOLD, 14));
			add(txtTotalAmount);
			
			// Create an array of our payment option
			String[] paymentMethods = {
					"Cash", 
					"GCash/QR Scan"
			};
			
			PMethodCBX = new JComboBox(paymentMethods);
			PMethodCBX.setBounds(331, 607, 241, 26);
			add(PMethodCBX);
			
			
			// 1. Update Clear Form Button
			JButton btnClearForm = new JButton("Clear Form");
			btnClearForm.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        clearForm();
			    }
			});
			btnClearForm.setForeground(new Color(255, 255, 255));
			btnClearForm.setBackground(new Color(224, 27, 36));
			btnClearForm.setFont(new Font("SansSerif", Font.BOLD, 14));
			// CHANGED: Moved down and right
			btnClearForm.setBounds(1198, 743, 140, 27); 
			add(btnClearForm);

			// 2. Update Save Booking Button
			JButton btnSaveBooking = new JButton("Save Booking");
			btnSaveBooking.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        saveBooking();
			    }
			});
			btnSaveBooking.setForeground(new Color(255, 255, 255));
			btnSaveBooking.setBackground(new Color(46, 194, 126));
			btnSaveBooking.setFont(new Font("SansSerif", Font.BOLD, 14));
			// CHANGED: Moved down and right, next to Clear Form
			btnSaveBooking.setBounds(1358, 743, 140, 27); 
			add(btnSaveBooking);
			
			loadAvailableRooms();
			// ADDED: para lumabas agad ang default picture ng Standard Room pag bumukas ang panel
			roomImageLabel.setIcon(createScaledIcon("Standard room.jpg", 815, 649));
			roomNameLabel.setText("Standard Room");
			
			
			//discount checkBox to
			chckbxSeniorPwd = new JCheckBox("Senior / PWD");
			chckbxSeniorPwd.addActionListener(new ActionListener() {
				//para ma trigger you calculate amount method every pindot
				public void actionPerformed(ActionEvent e) {
					calculateTotalAmount();
				}
			});
			chckbxSeniorPwd.setBounds(331, 565, 129, 23);
			add(chckbxSeniorPwd);
			
			JLabel lblDiscount = new JLabel("Discount");
			lblDiscount.setFont(new Font("SansSerif", Font.BOLD, 14));
			lblDiscount.setBounds(193, 567, 115, 15);
			add(lblDiscount);
			
			
		}
		
		//method to load avail rooms depends on selected room type
		private void loadAvailableRooms() {
				availableRoomCBX.removeAllItems(); //clear the old list every start
				
				//this figure out which room_type is selected
				int typeIndex = roomTypeCBX.getSelectedIndex() + 1;//Cuz DB id start at 1
				
				try (Connection conn = DBConnection.getConnection()){
						String sql = "SELECT room_id, room_number FROM rooms WHERE room_type_id =? AND is_available = 1";
						PreparedStatement ps = conn.prepareStatement(sql);
						ps.setInt(1, typeIndex);
						ResultSet rs = ps.executeQuery();
						
						boolean found = false;
								
						while (rs.next()) {
							availableRoomCBX.addItem(rs.getInt("room_id") + " - Room " + rs.getString("room_number"));
							found = true;
						}
					
						if (!found) availableRoomCBX.addItem("No rooms available");
				}catch(SQLException e){
					JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
				}
		}
		
		private void calculateTotalAmount() {
		    try {
		        // get dates from spinner
		        Date checkInDate = (Date) checkInSpinner.getValue();
		        Date checkOutDate = (Date) checkOutSPinner.getValue();

		        // Strip the TIME part — keep only the date
		        // This resets hours, minutes, seconds to 00:00:00
		        java.util.Calendar calIn = java.util.Calendar.getInstance();
		        calIn.setTime(checkInDate);
		        calIn.set(java.util.Calendar.HOUR_OF_DAY, 0);
		        calIn.set(java.util.Calendar.MINUTE, 0);
		        calIn.set(java.util.Calendar.SECOND, 0);
		        calIn.set(java.util.Calendar.MILLISECOND, 0);

		        java.util.Calendar calOut = java.util.Calendar.getInstance();
		        calOut.setTime(checkOutDate);
		        calOut.set(java.util.Calendar.HOUR_OF_DAY, 0);
		        calOut.set(java.util.Calendar.MINUTE, 0);
		        calOut.set(java.util.Calendar.SECOND, 0);
		        calOut.set(java.util.Calendar.MILLISECOND, 0);

		        // now calculate days cleanly
		        long diffMillis = calOut.getTimeInMillis() - calIn.getTimeInMillis();
		        long days = diffMillis / (1000 * 60 * 60 * 24);

		        if (days <= 0) {
		            txtTotalAmount.setText("Invalid Dates");
		            lblExtraCharge.setText("");
		            return;
		        }
		        
		        double[] basePrices    = {2500.0, 3500.0, 5000.0, 8000.0};
		        int[] includedGuests   = {2,       4,      6,      10};       // Base capacity
		        double[] extraGuestFee = {500.0,  700.0,  1000.0, 1500.0};    // Fee per extra head
		        
		        int typeIndex = roomTypeCBX.getSelectedIndex();
		        int actualGuests = (int) guestCountSpinner.getValue();
		        int allowedBaseGuests = includedGuests[typeIndex];
		        
		        // Calculate Base Room Cost
		        double total = days * basePrices[typeIndex];
		        
		        
		        
		        // Calculate Extra Guest Surcharge
		        int extraGuests = Math.max(0, actualGuests - allowedBaseGuests);
		        double extraChargeTotal = extraGuests * extraGuestFee[typeIndex] * days;
		        
		        // then we will now display how much is the total charged
		        if (extraGuests > 0) {
		            lblExtraCharge.setText(String.format("(+ ₱%,.2f for %d extra guests)", extraChargeTotal, extraGuests));
		        } else {
		            lblExtraCharge.setText(""); // clear if no extra guests
		        }
		        
		        // Finalize Total
		        total += extraChargeTotal;
		        total = applyDiscount(total); // Apply Senior/PWD if checked
		        
		        txtTotalAmount.setText("₱" + String.format("%,.2f", total));


		    } catch (Exception e) {
		        txtTotalAmount.setText("Error");
		    }
		}
		
		private void clearForm() {
			FNameFIeld.setText("");
	        LNameField.setText("");
	        PNumField.setText("");
	        emailField.setText("");
	        roomTypeCBX.setSelectedIndex(0);
	        PMethodCBX.setSelectedIndex(0);
	        checkInSpinner.setValue(new Date());
	        checkOutSPinner.setValue(new Date());
	        txtTotalAmount.setText("₱ 0.00");
	        // ADDED: i-clear ang image at label pag na-clear ang form
	        roomImageLabel.setIcon(null);
	        roomNameLabel.setText("");
	        // ADDED: i-reset ang guest count spinner sa 1
	        guestCountSpinner.setValue(1);
	        chckbxSeniorPwd.setSelected(false); // reverse the check box to unchecked
	        lblExtraCharge.setText("");
	        roomTypeCBX.setSelectedIndex(0);
		}
		
	
		
		
		
		
		private void saveBooking() {
		    // STEP 1 — Validate empty fields
		    if (FNameFIeld.getText().trim().isEmpty() || LNameField.getText().trim().isEmpty()
		            || PNumField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
		        JOptionPane.showMessageDialog(this, "Please fill in all customer information.", "Validation Error", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // STEP 2 — Validate phone number (must be 10-11 digits)
		    String phone = PNumField.getText().trim();
		    if (!phone.matches("\\d{10,11}")) {
		        JOptionPane.showMessageDialog(this, "Please enter a valid phone number (10-11 digits).", "Validation Error", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // STEP 3 — Validate email
		    String email = emailField.getText().trim();
		    if (!email.contains("@") || !email.contains(".")) {
		        JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		    
		    // BLOCK booking if customer already has an active booking (Reserved OR Checked In)
		    if (currentCustomerId != -1) {
		        try (Connection conn = DBConnection.getConnection()) {
		            String checkSQL = "SELECT b.booking_id, r.room_number, b.status FROM bookings b " +
		                              "JOIN rooms r ON b.room_id = r.room_id " +
		                              "WHERE b.customer_id = ? AND b.status IN ('Reserved', 'Checked In')";
		            PreparedStatement checkPS = conn.prepareStatement(checkSQL);
		            checkPS.setInt(1, currentCustomerId);
		            ResultSet checkRS = checkPS.executeQuery();

		            if (checkRS.next()) {
		                String roomNum = checkRS.getString("room_number");
		                String status = checkRS.getString("status");
		                JOptionPane.showMessageDialog(this,
		                    "This customer already has an active booking at Room " + roomNum + " (Status: " + status + ").\n" +
		                    "Cannot create a new booking while a reservation or check-in is active.",
		                    "Booking Blocked",
		                    JOptionPane.WARNING_MESSAGE);
		                return; // STOP — do not save
		            }
		        } catch (SQLException ex) {
		            JOptionPane.showMessageDialog(this, "Error checking customer status: " + ex.getMessage());
		            return;
		        }
		    }
		    
		    


		    

		    // STEP 4 — Validate room selection
		    String roomEntry = (String) availableRoomCBX.getSelectedItem();
		    if (roomEntry == null || roomEntry.startsWith("No rooms")) {
		        JOptionPane.showMessageDialog(this, "No available room selected.", "Validation Error", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		    
		 // STEP 4b — Safety: double-check room is still actually available
		    int roomId = Integer.parseInt(roomEntry.split(" - ")[0].trim());
		    try (Connection conn = DBConnection.getConnection()) {
		        PreparedStatement ps = conn.prepareStatement("SELECT is_available FROM rooms WHERE room_id = ?");
		        ps.setInt(1, roomId);
		        ResultSet rs = ps.executeQuery();
		        if (rs.next() && rs.getInt("is_available") == 0) {
		            JOptionPane.showMessageDialog(this, 
		                "Room " + roomEntry + " was just taken by another booking.\nPlease select a different room.", 
		                "Room No Longer Available", JOptionPane.WARNING_MESSAGE);
		            loadAvailableRooms();
		            return;
		        }
		    } catch (SQLException ex) {
		        JOptionPane.showMessageDialog(this, "Error checking room availability: " + ex.getMessage());
		        return;
		    }

		    // STEP 5 — Get dates and strip the time part
		    Date checkIn = (Date) checkInSpinner.getValue();
		    Date checkOut = (Date) checkOutSPinner.getValue();

		    java.util.Calendar calIn = java.util.Calendar.getInstance();
		    calIn.setTime(checkIn);
		    calIn.set(java.util.Calendar.HOUR_OF_DAY, 0);
		    calIn.set(java.util.Calendar.MINUTE, 0);
		    calIn.set(java.util.Calendar.SECOND, 0);
		    calIn.set(java.util.Calendar.MILLISECOND, 0);

		    java.util.Calendar calOut = java.util.Calendar.getInstance();
		    calOut.setTime(checkOut);
		    calOut.set(java.util.Calendar.HOUR_OF_DAY, 0);
		    calOut.set(java.util.Calendar.MINUTE, 0);
		    calOut.set(java.util.Calendar.SECOND, 0);
		    calOut.set(java.util.Calendar.MILLISECOND, 0);

		    // STEP 6 — Validate dates
		    long days = (calOut.getTimeInMillis() - calIn.getTimeInMillis()) / (1000 * 60 * 60 * 24);
		    if (days <= 0) {
		        JOptionPane.showMessageDialog(this, "Check-out must be after check-in.", "Validation Error", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // use stripped dates for DB
		    checkIn = calIn.getTime();
		    checkOut = calOut.getTime();

		    
		    
		 // STEP 7 — Calculate total for Database
		  
		    double[] basePrices    = {2500.0, 3500.0, 5000.0, 8000.0};
		    int[] includedGuests   = {2, 4, 6, 10}; 
		    double[] extraGuestFee = {500.0, 700.0, 1000.0, 1500.0}; 
		    
		    

		    int typeIndex = roomTypeCBX.getSelectedIndex();
		    int actualGuests = (int) guestCountSpinner.getValue();
		    int allowedBaseGuests = includedGuests[typeIndex];
		    int extraGuests = Math.max(0, actualGuests - allowedBaseGuests);

		    double baseTotal = basePrices[typeIndex] * days;
		    double extraChargeTotal = extraGuests * extraGuestFee[typeIndex] * days;

		    double total = baseTotal + extraChargeTotal;
		    
		    //discount if its applicable
		    total = applyDiscount(total);
		    
		    
		    // STEP 8 — Convert to SQL dates
		    java.sql.Date sqlCheckIn = new java.sql.Date(checkIn.getTime());
		    java.sql.Date sqlCheckOut = new java.sql.Date(checkOut.getTime());
		    String payment = (String) PMethodCBX.getSelectedItem();

		 // STEP 9 — Save to database
		    try (Connection conn = DBConnection.getConnection()) {
		        conn.setAutoCommit(false); // start transaction

		        try {
		            int finalCustomerId = currentCustomerId;

		            // 1. ONLY insert a new customer if they are brand new (ID is -1)
		            if (finalCustomerId == -1) {
		                // Brand new customer -> INSERT
		                String custSQL = "INSERT INTO customers (first_name, last_name, phone_number, email, created_at) VALUES (?,?,?,?, NOW())";
		                PreparedStatement custPS = conn.prepareStatement(custSQL, Statement.RETURN_GENERATED_KEYS);
		                custPS.setString(1, FNameFIeld.getText().trim());
		                custPS.setString(2, LNameField.getText().trim());
		                custPS.setString(3, phone);
		                custPS.setString(4, email);
		                custPS.executeUpdate();

		                ResultSet keys = custPS.getGeneratedKeys();
		                if (keys.next()) finalCustomerId = keys.getInt(1);
		                
		            } else {
		                // EXISTING customer -> UPDATE their contact info just in case they changed it on the form!
		                String updateSQL = "UPDATE customers SET phone_number = ?, email = ? WHERE customer_id = ?";
		                PreparedStatement updatePS = conn.prepareStatement(updateSQL);
		                updatePS.setString(1, phone);
		                updatePS.setString(2, email);
		                updatePS.setInt(3, finalCustomerId);
		                updatePS.executeUpdate();
		            }

		            // 2. Insert booking (CHANGED: 'Active' is now perfectly set to 'Reserved')
		            String bookSQL = "INSERT INTO bookings (customer_id, room_id, check_in_date, check_out_date, total_amount, payment_method, status) VALUES (?,?,?,?,?,?, 'Reserved')";
		            PreparedStatement bookPS = conn.prepareStatement(bookSQL);
		            bookPS.setInt(1, finalCustomerId); 
		            bookPS.setInt(2, roomId);
		            bookPS.setDate(3, sqlCheckIn);
		            bookPS.setDate(4, sqlCheckOut);
		            bookPS.setDouble(5, total);
		            bookPS.setString(6, payment);
		            bookPS.executeUpdate();

		            // 3. Mark room as occupied
		            PreparedStatement roomPS = conn.prepareStatement("UPDATE rooms SET is_available = 0 WHERE room_id = ?");
		            roomPS.setInt(1, roomId);
		            roomPS.executeUpdate();

		            conn.commit(); // save everything!

		            // Success message
		            JOptionPane.showMessageDialog(this,
		                String.format("✅ Booking saved!\nCustomer: %s %s\nTotal: ₱%,.2f",
		                FNameFIeld.getText(), LNameField.getText(), total),
		                "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

		            clearForm();
		            loadAvailableRooms();
		          
		        } catch (SQLException ex) {
		            conn.rollback(); // undo everything if error!
		            throw ex;
		        } finally {
		            conn.setAutoCommit(true);
		        }

		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
		}

		
		
		
		
		
	// ADDED: helper method para i-resize ang image
	private ImageIcon createScaledIcon(String path, int width, int height) {
	    try {
	        ImageIcon icon = new ImageIcon(path);
	        if (icon.getIconWidth() == -1) {
	            System.out.println("Warning: Could not find image at " + path);
	            return null;
	        }
	        Image img = icon.getImage();
	        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	        return new ImageIcon(newImg);
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	
	
	//discount method 
	private double applyDiscount(double subTotal) {
		if (chckbxSeniorPwd.isSelected()) {
			return subTotal - (subTotal * 0.20);	
		}
		return subTotal;
	}
	
	//search customer method
	private void showCustomerSearchDialog() {
	    JDialog searchDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Search Customer", true);
	    searchDialog.setSize(500, 350);
	    searchDialog.setLocationRelativeTo(this);
	    searchDialog.getContentPane().setLayout(new BorderLayout());

	    // Top Panel: Search Bar
	    JPanel topPanel = new JPanel(new FlowLayout());
	    JTextField txtSearch = new JTextField(20);
	    JButton btnSearch = new JButton("Search");
	    topPanel.add(new JLabel("Name:"));
	    topPanel.add(txtSearch);
	    topPanel.add(btnSearch);
	    searchDialog.getContentPane().add(topPanel, BorderLayout.NORTH);
	    
	    txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyTyped(java.awt.event.KeyEvent e) {
		        char c = e.getKeyChar();
		        // block numbers - hindi makakapag type ng number
		        if (c >= '0' && c <= '9') {
		            e.consume();
		        }
		        // auto uppercase - kahit hindi pinindot caps lock
		        if (Character.isLetter(c)) {
		            e.setKeyChar(Character.toUpperCase(c));
		        }
		    }
		});


	    // Center Panel: Results Table
	    String[] columns = {"ID", "First Name", "Last Name", "Phone"};
	    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0) {
	        public boolean isCellEditable(int row, int column) { return false; }
	    };
	    JTable table = new JTable(model);
	    searchDialog.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

	    // Bottom Panel: Action Buttons
	    JPanel bottomPanel = new JPanel();
	    JButton btnSelect = new JButton("Select Customer");
	    bottomPanel.add(btnSelect);
	    searchDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

	    // The Search Action (Database Query)
	    btnSearch.addActionListener(e -> {
	        model.setRowCount(0); // clear previous results
	        String keyword = "%" + txtSearch.getText().trim() + "%";
	        
	        try (Connection conn = DBConnection.getConnection()) {
	            String sql = "SELECT customer_id, first_name, last_name, phone_number, email FROM customers WHERE first_name LIKE ? OR last_name LIKE ?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, keyword);
	            ps.setString(2, keyword);
	            ResultSet rs = ps.executeQuery();
	            
	            while (rs.next()) {
	                model.addRow(new Object[]{
	                    rs.getInt("customer_id"),
	                    rs.getString("first_name"),
	                    rs.getString("last_name"),
	                    rs.getString("phone_number")
	                });
	            }
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(searchDialog, "Search Error: " + ex.getMessage());
	        }
	    });

	    // The Select Action (Auto-fill the main form)
	    btnSelect.addActionListener(e -> {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow != -1) {
	            // 1. Save the ID
	            currentCustomerId = (int) model.getValueAt(selectedRow, 0);
	            
	            // 2. Fetch full details to populate form
	            try (Connection conn = DBConnection.getConnection()) {
	                PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers WHERE customer_id = ?");
	                ps.setInt(1, currentCustomerId);
	                ResultSet rs = ps.executeQuery();
	                if (rs.next()) {
	                    FNameFIeld.setText(rs.getString("first_name"));
	                    LNameField.setText(rs.getString("last_name"));
	                    PNumField.setText(rs.getString("phone_number"));
	                    emailField.setText(rs.getString("email"));
	                    
	                    // 3. Lock fields to prevent editing an existing DB record
	                    FNameFIeld.setEditable(false);
	                    LNameField.setEditable(false);
	                    PNumField.setEditable(true);
	                    emailField.setEditable(true);
	                }
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	            searchDialog.dispose(); // Close popup
	        }
	    });

	    searchDialog.setVisible(true);
	}
}