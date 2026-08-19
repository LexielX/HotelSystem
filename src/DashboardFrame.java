import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DashboardFrame extends JFrame {

	private JFrame frame;


	/**
	 * Create the application.
	 */
	public DashboardFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setUndecorated(true);
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(39, 0, 110));
		panel.setPreferredSize(new Dimension(900, 60));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblHotelReservationSystem = new JLabel("Sync Suites Hotel ");
		lblHotelReservationSystem.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblHotelReservationSystem.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHotelReservationSystem.setForeground(new Color(255, 255, 255));
		panel.add(lblHotelReservationSystem, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(39, 0, 110));
		panel_1.setForeground(new Color(255, 255, 255));
		panel.add(panel_1, BorderLayout.EAST);
		
		JButton LogoutBtn = new JButton("Logout");
		LogoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?","Confirm Logout", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					frame.dispose();
					new LoginFrame();
				}
			}
		});
		LogoutBtn.setForeground(new Color(255, 255, 255));
		LogoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
		LogoutBtn.setBackground(new Color(224, 27, 36));
		panel_1.add(LogoutBtn);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		NewBookingPanel panel_2 = new NewBookingPanel();
		panel_2.setName("");
		tabbedPane.addTab("New Booking", null, panel_2, null);
		
		RoomAvailabilityPanel panel_3 = new RoomAvailabilityPanel();
		tabbedPane.addTab("Room Availability", null, panel_3, null);
		
		JPanel panel_4 = new ManageBookingPanel();
		tabbedPane.addTab("Manage Booking", null, panel_4, null);
		
		frame.setVisible(true);
	}

}
