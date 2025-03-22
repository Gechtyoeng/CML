package view;
import javax.swing.*;

import database.UserDao;
import util.base.Person;

public class LoginFrame extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(){
        setTitle("LOGIN");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            Person user = UserDao.fetchUser(null, username, password, null);

            if (user != null) {
                new DashboeardFrame(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
