import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoomAvailabilityPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;

	/**
	 * Create the panel.
	 */
	public RoomAvailabilityPanel() {
		setLayout(new BorderLayout());		
		//title
		JLabel titleLabel = new JLabel("Room Availability Overview");
		titleLabel.setForeground(new Color(0, 0, 68));
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		titleLabel.setBorder(new EmptyBorder(15, 15, 10, 0));
		add(titleLabel, BorderLayout.NORTH);
		
		//table
		//column name
		String[] columns = {"Room No.", "Room Type", "Rate / Day (₱)", "Status"};
		tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // so the tables is not editable and it will be read only
            }
        };

		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		// ... your existing table setup
				table = new JTable(tableModel);
				scrollPane.setViewportView(table);
				table.setRowHeight(28);
				table.getTableHeader().setFont(new Font("SanSerif", Font.BOLD, 13));
				table.getTableHeader().setForeground(new Color(255, 255, 255)); 
				table.getTableHeader().setBackground(new Color(31, 71, 145)); 
				table.setFont(new Font("SansSerif", Font.PLAIN, 13));
				
				// =========================================================
				// ADDED: Custom Cell Renderer for the "Status" Column (Index 3)
				// =========================================================
				table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
				    @Override
				    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				        
				        // Get the default cell styling first
				        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				        
				        // Make the text bold for better visibility
				        c.setFont(new Font("SansSerif", Font.BOLD, 13));

				        if (value != null) {
				            String status = value.toString();
				            
				            if (status.equals("Available")) {
				                c.setForeground(new Color(46, 194, 126)); // Green
				            } else if (status.equals("Occupied")) {
				                c.setForeground(new Color(224, 27, 36));  // Red 
				            } else if (status.equals("Reserved")) {
				            	c.setForeground(new Color(55, 56, 163));  // Red 
				            } else {
				                c.setForeground(Color.BLACK); // Fallback
				            }
				            
				        }

				        // Fix color when the user clicks/selects the row so it remains readable
				        if (isSelected) {
				            c.setBackground(table.getSelectionBackground());
				        } else {
				            c.setBackground(table.getBackground());
				        }

				        return c;
				    }
				});
		
		
	
		
		
		
		//south panel or the submit button
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadRoom();
			}
		});
		refreshBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
		refreshBtn.setForeground(new Color(0, 0, 0));
		refreshBtn.setBackground(UIManager.getColor("Button.highlight"));
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		

		panel.add(refreshBtn);
		
		loadRoom();
	}
	
	
	
	
	
	private void loadRoom() {
		tableModel.setRowCount(0);
		try (Connection conn = DBConnection.getConnection()){
			String sql = "SELECT r.room_number, rt.type_name, rt.rate_per_day, " +
                    	"CASE " +
                    	"    WHEN b.status = 'Checked In' THEN 'Occupied' " +
                    	"    WHEN b.status = 'Reserved' THEN 'Reserved' " +
                    	"    ELSE 'Available' " +
                    	"END AS status " +
                    	"FROM rooms r " +
                    	"JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                    	"LEFT JOIN bookings b ON r.room_id = b.room_id " +
                    	"    AND b.status IN ('Reserved', 'Checked In') " +
                    	"ORDER BY " +
                    	"    CASE " +
                    	"        WHEN b.status = 'Checked In' THEN 1 " +
                    	"        WHEN b.status = 'Reserved' THEN 2 " +
                    	"        ELSE 3 " +
                    	"    END DESC, " +  // DESC para Occupied muna, then Reserved, then Available
                    	"    r.room_type_id, r.room_number";

			Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	tableModel.addRow(new Object[] {
            			rs.getString("room_number"),
            			rs.getString("type_name"),
            			String.format("%.2f", rs.getDouble("rate_per_day")),
            			rs.getString("status")
            	});
            }
			
		}catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
