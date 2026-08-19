import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private JFrame frmHotelReservationAdmin;
    private JTextField userField;
    private JPasswordField passField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginFrame window = new LoginFrame();
                    window.frmHotelReservationAdmin.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    //verification process
    private void handleLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frmHotelReservationAdmin, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            	new DashboardFrame(); 
                
                // Dispose the login window
                frmHotelReservationAdmin.dispose(); 
            } else {
                JOptionPane.showMessageDialog(frmHotelReservationAdmin, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frmHotelReservationAdmin, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Create the application.
     */
    public LoginFrame() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmHotelReservationAdmin = new JFrame();
        frmHotelReservationAdmin.setForeground(new Color(255, 255, 255));
        frmHotelReservationAdmin.setFont(new Font("Source Code Pro", Font.BOLD, 40));
        frmHotelReservationAdmin.setTitle("SYNC SUITES HOTEL RESERVATION ADMIN LOGIN");
        frmHotelReservationAdmin.getContentPane().setBackground(new Color(46, 44, 122));
        frmHotelReservationAdmin.getContentPane().setLayout(null);
        frmHotelReservationAdmin.setSize(479, 343);
        frmHotelReservationAdmin.setLocationRelativeTo(null);

        JLabel lblNewLabel = new JLabel("ADMIN LOGIN");
        lblNewLabel.setBounds(139, 71, 176, 34);
        lblNewLabel.setFont(new Font("Source Code Pro", Font.BOLD, 26));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        frmHotelReservationAdmin.getContentPane().add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Serif", Font.BOLD, 14));
        lblUsername.setBounds(99, 130, 92, 17);
        lblUsername.setForeground(new Color(255, 255, 255));
        frmHotelReservationAdmin.getContentPane().add(lblUsername);

        userField = new JTextField();
        userField.setBounds(191, 130, 130, 21);
        frmHotelReservationAdmin.getContentPane().add(userField);
        userField.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Serif", Font.BOLD, 14));
        lblPassword.setForeground(new Color(255, 255, 255));
        lblPassword.setBounds(99, 169, 82, 17);
        frmHotelReservationAdmin.getContentPane().add(lblPassword);

        passField = new JPasswordField();
        passField.setBounds(191, 167, 130, 21);
        frmHotelReservationAdmin.getContentPane().add(passField);

        JButton btnNewButton = new JButton("LOGIN");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        btnNewButton.setBounds(99, 222, 222, 27);
        frmHotelReservationAdmin.getContentPane().add(btnNewButton);
        
        //para pag pinondot yung enter rekta login na
        passField.addActionListener(e -> handleLogin());

        frmHotelReservationAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frmHotelReservationAdmin.setVisible(true);
    }
}