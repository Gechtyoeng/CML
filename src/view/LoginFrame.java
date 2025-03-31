package view;
import javax.swing.*;

import database.UserDao;
import util.base.Person;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginFrame extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame(){
        setTitle("Clinic Management System - LOGIN");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel with a light blue background
        JPanel panel = new JPanel();
        panel.setBackground(new Color(173, 216, 230)); // Light blue
        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for better alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 5, 5, 5);

        JLabel titleLabel = new JLabel("CMS-Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Large bold font
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Username Label
        gbc.gridwidth = 1; // Reset width
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Username:"), gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.gridy = 2;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        // Add panel to frame
        add(panel);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();

            }
        });

        setVisible(true);
    }
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        //fetching the user base on username and password
        Person user = UserDao.fetchUser(null,username, password,null);

        // Temporary check (Replace with actual authentication logic)
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
       new LoginFrame();
    }
}
