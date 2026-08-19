
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManageBookingPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;

    // Simple variables lang para sa filter
    private String filterStatus = "All";
    private String filterPayment = "All"; 
    private String filterRoomType = "All";
    
    public ManageBookingPanel() {
    	setLayout(new BorderLayout());

        // --- Header ---
        JLabel titleLbl = new JLabel("Manage Booking");
        titleLbl.setForeground(new Color(0, 0, 47));
        titleLbl.setBackground(new Color(240, 240, 240));
        titleLbl.setOpaque(true);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLbl.setBorder(new EmptyBorder(15, 15, 15, 0));
        add(titleLbl, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columns = {"Booking ID", "Customer Name", "Room No.", "Room Type", "Check-In", "Check-Out", "Total (₱)", "Payment", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(31, 71, 145));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(table);

        // =========================================================
        // Custom Cell Renderer for the "Status" Column (Index 8)
        // =========================================================
        table.getColumnModel().getColumn(8).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Make text bold so the colors pop
                c.setFont(new Font("SansSerif", Font.BOLD, 13));

                if (value != null) {
                    String status = value.toString();
                    
                    // Color code based on the exact status word
                    switch (status) {
                        case "Reserved":
                            c.setForeground(new Color(0, 128, 255)); // Blue
                            break;
                        case "Checked In":
                            c.setForeground(new Color(46, 194, 126)); // Green
                            break;
                        case "Checked Out":
                            c.setForeground(new Color(255, 165, 0)); // Orange
                            break;
                        case "Cancelled":
                            c.setForeground(new Color(224, 27, 36)); // Red
                            break;
                        case "Rescheduled":
                            c.setForeground(new Color(148, 0, 211)); // Purple
                            break;
                        default:
                            c.setForeground(Color.BLACK); // Fallback
                            break;
                    }
                }

                // Keep selection background color intact when clicking a row
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                } else {
                    c.setBackground(table.getBackground());
                }

                return c;
            }
        });

        // --- Action Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton btnRefresh = new JButton("Refresh");
        JButton btnCheckIn = new JButton("Check In");
        JButton btnCheckOut = new JButton("Check Out");
        JButton btnExtend = new JButton("Extend Stay");
        JButton btnUpgrade = new JButton("Upgrade Room");
        JButton btnReschedule = new JButton("Reschedule");
        JButton btnCancel = new JButton("Cancel Booking");
        JButton btnFilter = new JButton("Filter");
        JButton btnClearFilter = new JButton("Clear Filter");

        // Styling Buttons
        btnFilter.setBackground(new Color(70, 130, 180)); btnFilter.setForeground(Color.WHITE);
        btnClearFilter.setBackground(new Color(128, 128, 128)); btnClearFilter.setForeground(Color.WHITE);
        btnCheckIn.setBackground(new Color(46, 194, 126)); btnCheckIn.setForeground(Color.WHITE);
        btnCheckOut.setBackground(new Color(255, 165, 0)); btnCheckOut.setForeground(Color.WHITE);
        btnExtend.setBackground(new Color(148, 0, 211)); btnExtend.setForeground(Color.WHITE);
        btnUpgrade.setBackground(new Color(32, 178, 170)); btnUpgrade.setForeground(Color.WHITE);
        btnReschedule.setBackground(new Color(0, 128, 255)); btnReschedule.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(224, 27, 36)); btnCancel.setForeground(Color.WHITE);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnCheckIn);
        buttonPanel.add(btnCheckOut);
        buttonPanel.add(btnExtend);
        buttonPanel.add(btnUpgrade);
        buttonPanel.add(btnReschedule);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnFilter);
        
        // Listeners
        btnRefresh.addActionListener(e -> loadBookings());
        btnCheckIn.addActionListener(e -> checkInBooking());
        btnCheckOut.addActionListener(e -> checkOutBooking());
        btnExtend.addActionListener(e -> extendStayBooking());
        btnUpgrade.addActionListener(e -> upgradeRoomBooking());
        btnReschedule.addActionListener(e -> rescheduleBooking());
        btnCancel.addActionListener(e -> cancelBooking());
        btnFilter.addActionListener(e -> showFilterDialog());

        loadBookings();
    }

   
    private void loadBookings() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            
            // Auto Check-Out guests if their date has passed
            Statement autoStmt = conn.createStatement();
            autoStmt.executeUpdate(
                "UPDATE bookings SET status = 'Checked Out' " +
                "WHERE check_out_date < CURDATE() AND status = 'Checked In' " +
                "ORDER BY status DESC"
            );
            
            // Free rooms ONLY if they have NO active bookings
            Statement autoFreeStmt = conn.createStatement();
            autoFreeStmt.executeUpdate(
                "UPDATE rooms r SET r.is_available = 1 " +
                "WHERE r.is_available = 0 " +
                "AND NOT EXISTS (" +
                "    SELECT 1 FROM bookings b " +
                "    WHERE b.room_id = r.room_id " +
                "    AND b.status IN ('Reserved', 'Checked In')" +
                ")"
            );
            
            // Start ng SQL query
            String sql = "SELECT b.booking_id, CONCAT(c.first_name, ' ', c.last_name) AS customer, " +
                         "r.room_number, rt.type_name, b.check_in_date, b.check_out_date, " +
                         "b.total_amount, b.payment_method, b.status " +
                         "FROM bookings b " +
                         "JOIN customers c ON b.customer_id = c.customer_id " +
                         "JOIN rooms r ON b.room_id = r.room_id " +
                         "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                         "WHERE 1=1 ";
            
            // Dagdag ng filter kung hindi "All" ang napili
            if (!filterStatus.equals("All")) {
                sql = sql + "AND b.status = ? ";
            }
            if (!filterPayment.equals("All")) {
                sql = sql + "AND b.payment_method = ? ";
            }
            if (!filterRoomType.equals("All")) {
                sql = sql + "AND rt.type_name = ? ";
            }
            
            sql = sql + "ORDER BY b.check_in_date DESC";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            
            // Set values sa ? (question mark)
            int index = 1;
            
            if (!filterStatus.equals("All")) {
                ps.setString(index, filterStatus);
                index = index + 1;
            }
            if (!filterPayment.equals("All")) {
                ps.setString(index, filterPayment);
                index = index + 1;
            }
            if (!filterRoomType.equals("All")) {
                ps.setString(index, filterRoomType);
                index = index + 1;
            }
            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                    rs.getInt("booking_id"), rs.getString("customer"), rs.getString("room_number"),
                    rs.getString("type_name"), rs.getString("check_in_date"), rs.getString("check_out_date"),
                    rs.getDouble("total_amount"), rs.getString("payment_method"), rs.getString("status")
                });
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }

    // ============================================================
    // 2. Check In
    // ============================================================
    private void checkInBooking() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (!tableModel.getValueAt(row, 8).equals("Reserved")) {
            JOptionPane.showMessageDialog(this, "Only 'Reserved' bookings can be Checked In."); return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE bookings SET status = 'Checked In' WHERE booking_id = ?");
            ps.setInt(1, (int) tableModel.getValueAt(row, 0));
            ps.executeUpdate();
            loadBookings();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ============================================================
    // 3. Check Out
    // ============================================================
    private void checkOutBooking() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (!tableModel.getValueAt(row, 8).equals("Checked In")) {
            JOptionPane.showMessageDialog(this, "Only 'Checked In' guests can Check Out."); return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            int bookingId = (int) tableModel.getValueAt(row, 0);
            conn.prepareStatement("UPDATE bookings SET status = 'Checked Out' WHERE booking_id = " + bookingId).executeUpdate();
            conn.prepareStatement("UPDATE rooms r JOIN bookings b ON b.room_id = r.room_id SET r.is_available = 1 WHERE b.booking_id = " + bookingId).executeUpdate();
            loadBookings();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ============================================================
    // 4. Extend Stay (NEW)
    // ============================================================
    private void extendStayBooking() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        
        String status = (String) tableModel.getValueAt(row, 8);
        if (!status.equals("Checked In")) {
            JOptionPane.showMessageDialog(this, 
                "Only 'Checked In' guests can extend their stay.", 
                "Cannot Extend", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int bookingId = (int) tableModel.getValueAt(row, 0);
            double currentTotal = (double) tableModel.getValueAt(row, 6);
            String roomType = (String) tableModel.getValueAt(row, 3);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date oldCheckOut = sdf.parse((String) tableModel.getValueAt(row, 5));

            SpinnerDateModel extendModel = new SpinnerDateModel(oldCheckOut, oldCheckOut, null, Calendar.DAY_OF_MONTH);
            JSpinner extendSpinner = new JSpinner(extendModel);
            extendSpinner.setEditor(new JSpinner.DateEditor(extendSpinner, "yyyy-MM-dd"));

            if (JOptionPane.showConfirmDialog(this, new Object[]{"Select New Check-Out Date:", extendSpinner}, "Extend Stay", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Date newCheckOut = (Date) extendSpinner.getValue();
                long extraDays = (newCheckOut.getTime() - oldCheckOut.getTime()) / (1000 * 60 * 60 * 24);

                if (extraDays > 0) {
                    double extraCost = extraDays * getRateForType(roomType);
                    double newTotal = currentTotal + extraCost;

                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("UPDATE bookings SET check_out_date = ?, total_amount = ? WHERE booking_id = ?");
                        ps.setString(1, sdf.format(newCheckOut));
                        ps.setDouble(2, newTotal);
                        ps.setInt(3, bookingId);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Stay extended! Extra cost: ₱" + String.format("%.2f", extraCost));
                        loadBookings();
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ============================================================
    // 5. Upgrade Room (NEW)
    // ============================================================

    private void upgradeRoomBooking() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please click on a booking first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String status = (String) tableModel.getValueAt(row, 8);
        if (!status.equals("Reserved") && !status.equals("Checked In")) {
            JOptionPane.showMessageDialog(this, "Cannot upgrade this booking.", "Warning", JOptionPane.WARNING_MESSAGE); 
            return;
        }

        try {
            // Get booking details
            int bookingId = (int) tableModel.getValueAt(row, 0);
            String oldRoomNum = (String) tableModel.getValueAt(row, 2);
            String oldRoomType = (String) tableModel.getValueAt(row, 3);
            double currentTotal = (double) tableModel.getValueAt(row, 6);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date in = sdf.parse((String) tableModel.getValueAt(row, 4));
            Date out = sdf.parse((String) tableModel.getValueAt(row, 5));
            long days = (out.getTime() - in.getTime()) / (1000 * 60 * 60 * 24);

            // --- 1. Create the UI Panel for the Popup ---
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            String[] roomTypes = { "Standard Room", "Junior Suite", "Executive Suite", "Presidential Suite" };
            JComboBox<String> roomTypeCombo = new JComboBox<>(roomTypes);
            JComboBox<String> availableRoomCombo = new JComboBox<>();

            panel.add(new JLabel("Room Type:"));
            panel.add(roomTypeCombo);
            panel.add(new JLabel("Available Room:"));
            panel.add(availableRoomCombo);

            // Lists to secretly store the IDs and Rates of the rooms in the dropdown
            java.util.List<Integer> roomIds = new java.util.ArrayList<>();
            java.util.List<Double> roomRates = new java.util.ArrayList<>();

            // --- 2. Action Listener to filter rooms dynamically ---
            ActionListener updateRoomsAction = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    availableRoomCombo.removeAllItems();
                    roomIds.clear();
                    roomRates.clear();

                    int typeId = roomTypeCombo.getSelectedIndex() + 1; // Database IDs start at 1

                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement(
                            "SELECT r.room_id, r.room_number, rt.rate_per_day " +
                            "FROM rooms r JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                            "WHERE r.room_type_id = ? AND r.is_available = 1"
                        );
                        ps.setInt(1, typeId);
                        ResultSet rs = ps.executeQuery();
                        
                        boolean found = false;
                        while (rs.next()) {
                            availableRoomCombo.addItem("Room " + rs.getString("room_number"));
                            roomIds.add(rs.getInt("room_id"));
                            roomRates.add(rs.getDouble("rate_per_day"));
                            found = true;
                        }
                        if (!found) availableRoomCombo.addItem("No rooms available");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            };

            // Hook up the listener and trigger it once to load the initial list
            roomTypeCombo.addActionListener(updateRoomsAction);
            updateRoomsAction.actionPerformed(null);

            // --- 3. Show the Popup ---
            if (JOptionPane.showConfirmDialog(this, panel, "Upgrade Room", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                
                String selectedRoom = (String) availableRoomCombo.getSelectedItem();
                if (selectedRoom == null || selectedRoom.equals("No rooms available")) {
                    JOptionPane.showMessageDialog(this, "Invalid room selection. Upgrade cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get data for the selected room
                int selectedIndex = availableRoomCombo.getSelectedIndex();
                int newRoomId = roomIds.get(selectedIndex);
                double newRate = roomRates.get(selectedIndex);
                
                // Calculate new totals
                double oldRate = getRateForType(oldRoomType);
                double diff = (newRate - oldRate) * days;
                double newTotal = currentTotal + diff;

                // --- 4. Final Confirmation ---
                if (JOptionPane.showConfirmDialog(this, "Price Difference: ₱" + String.format("%,.2f", diff) + "\nNew Total: ₱" + String.format("%,.2f", newTotal) + "\nProceed?", "Confirm Upgrade", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try (Connection conn = DBConnection.getConnection()) {
                        conn.setAutoCommit(false);
                        
                        // Update booking
                        PreparedStatement ps = conn.prepareStatement("UPDATE bookings SET room_id = ?, total_amount = ? WHERE booking_id = ?");
                        ps.setInt(1, newRoomId); 
                        ps.setDouble(2, newTotal); 
                        ps.setInt(3, bookingId); 
                        ps.executeUpdate();
                        
                        // Free the old room and occupy the new room
                        conn.prepareStatement("UPDATE rooms SET is_available = 1 WHERE room_number = '" + oldRoomNum + "'").executeUpdate();
                        conn.prepareStatement("UPDATE rooms SET is_available = 0 WHERE room_id = " + newRoomId).executeUpdate();
                        
                        conn.commit();
                        JOptionPane.showMessageDialog(this, "Room upgraded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadBookings(); // Refresh the table
                    }
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    // ============================================================
    // 6. Reschedule 
    // ============================================================
    //
    private void rescheduleBooking() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (!tableModel.getValueAt(row, 8).equals("Reserved")) {
            JOptionPane.showMessageDialog(this, "Only 'Reserved' bookings can be rescheduled."); return;
        }
        
        int bookingId = (int) tableModel.getValueAt(row, 0);
        double dailyRate = getRateForType((String) tableModel.getValueAt(row, 3));

        try {
            // Kunin ang original dates mula sa table (columns 4 at 5)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date originalCheckIn = sdf.parse((String) tableModel.getValueAt(row, 4));
            Date originalCheckOut = sdf.parse((String) tableModel.getValueAt(row, 5));

            // I-set ang minimum ng check-in sa original check-in date
            // Hindi na pwedeng i-atras ang check-in date
            SpinnerDateModel checkInModel = new SpinnerDateModel(originalCheckIn, originalCheckIn, null, Calendar.DAY_OF_MONTH);
            // I-set ang initial value ng check-out sa original check-out date
            SpinnerDateModel checkOutModel = new SpinnerDateModel(originalCheckOut, originalCheckIn, null, Calendar.DAY_OF_MONTH);
            
            JSpinner checkInSpinner = new JSpinner(checkInModel);
            JSpinner checkOutSpinner = new JSpinner(checkOutModel);
            checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd"));
            checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd")); 

            JPanel datePanel = new JPanel(new GridLayout(2, 2, 5, 5));
            datePanel.add(new JLabel("New Check-In Date:")); datePanel.add(checkInSpinner);
            datePanel.add(new JLabel("New Check-Out Date:")); datePanel.add(checkOutSpinner);

            if (JOptionPane.showConfirmDialog(this, datePanel, "Pick New Dates", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Date in = sdf.parse(sdf.format(checkInSpinner.getValue()));
                Date out = sdf.parse(sdf.format(checkOutSpinner.getValue()));

                long days = (out.getTime() - in.getTime()) / (1000 * 60 * 60 * 24);
                if (days <= 0) { JOptionPane.showMessageDialog(this, "Check-out must be after check-in."); return; }

                double fee = dailyRate * 0.15;
                double newTotal = (dailyRate * days) + fee;

                if (JOptionPane.showConfirmDialog(this, String.format("Reschedule Fee: ₱%,.2f\nNew Total: ₱%,.2f\nConfirm?", fee, newTotal), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("UPDATE bookings SET check_in_date=?, check_out_date=?, total_amount=?, reschedule_charge=?, status='Reserved' WHERE booking_id=?");
                        ps.setString(1, sdf.format(in)); ps.setString(2, sdf.format(out));
                        ps.setDouble(3, newTotal); ps.setDouble(4, fee); ps.setInt(5, bookingId);
                        ps.executeUpdate();
                        loadBookings();
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ============================================================
    // 7. Cancel 
    // ============================================================
    private void cancelBooking() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (!tableModel.getValueAt(row, 8).equals("Reserved")) {
            JOptionPane.showMessageDialog(this, "Only 'Reserved' bookings can be cancelled."); return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            int bookingId = (int) tableModel.getValueAt(row, 0);
            conn.prepareStatement("UPDATE bookings SET status = 'Cancelled' WHERE booking_id = " + bookingId).executeUpdate();
            conn.prepareStatement("UPDATE rooms r JOIN bookings b ON b.room_id = r.room_id SET r.is_available = 1 WHERE b.booking_id = " + bookingId).executeUpdate();
            loadBookings();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ============================================================
    // Helper: Get Rate
    // ============================================================
    private double getRateForType(String typeName) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT rate_per_day FROM room_types WHERE type_name = ?");
            ps.setString(1, typeName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("rate_per_day");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }
    
    // Popup window para sa Filter
    private void showFilterDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Filter Bookings", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        
        // Form sa gitna
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        
        String[] statusList = {"All", "Reserved", "Checked In", "Checked Out", "Cancelled"};
        JComboBox<String> cboStatus = new JComboBox<>(statusList);
        cboStatus.setSelectedItem(filterStatus);
        
        String[] paymentList = {"All", "Cash", "GCash/QR Scan"};
        JComboBox<String> cboPayment = new JComboBox<>(paymentList);
        cboPayment.setSelectedItem(filterPayment);
        
        String[] typeList = {"All", "Standard Room", "Junior Suite", "Executive Suite", "Presidential Suite"};
        JComboBox<String> cboType = new JComboBox<>(typeList);
        cboType.setSelectedItem(filterRoomType);
        
        form.add(new JLabel("Status:"));
        form.add(cboStatus);
        form.add(new JLabel("Payment:"));
        form.add(cboPayment);
        form.add(new JLabel("Room Type:"));
        form.add(cboType);
        
        // Buttons sa baba
        JPanel buttons = new JPanel();
        JButton btnApply = new JButton("Apply Filter");
        JButton btnClear = new JButton("Clear Filter");
        buttons.add(btnApply);
        buttons.add(btnClear);
        
        dialog.getContentPane().add(form, BorderLayout.CENTER);
        dialog.getContentPane().add(buttons, BorderLayout.SOUTH);
        
        // Pag pinindot Apply
        btnApply.addActionListener(e -> {
            filterStatus = (String) cboStatus.getSelectedItem();
            filterPayment = (String) cboPayment.getSelectedItem();
            filterRoomType = (String) cboType.getSelectedItem();
            loadBookings();
            dialog.dispose();
        });
        
        // Pag pinindot Clear
        btnClear.addActionListener(e -> {
            clearFilters();
            dialog.dispose();
        });
        
        dialog.setVisible(true);
    }
    
    // I-reset ang filter sa "All"
    private void clearFilters() {
        filterStatus = "All";
        filterPayment = "All";
        filterRoomType = "All";
        loadBookings();
        JOptionPane.showMessageDialog(this, "Filter cleared!");
    }
    
}