package frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Book;
import model.CD;
import model.DVD;
import model.Manager;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private Manager manager;

    public LoginFrame(Manager manager) {
        this.manager = manager;

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JLabel userIdLabel = new JLabel("Username:");
        userIdField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        setLayout(new GridLayout(5, 2));
        add(userIdLabel);
        add(userIdField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(backButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(userIdField.getText(), new String(passwordField.getPassword()));
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
				RoleSelectionFrame roleSelectionFrame = new RoleSelectionFrame();
        		roleSelectionFrame.setVisible(true);
                dispose();
            }
        });
    }

    private void login(String username, String password) {
        List<Manager> users = manager.getUsers(); 
        if (users == null) {
            JOptionPane.showMessageDialog(this, "No users found.");
            return;
        }
        
        for (Manager user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) { 
                DVD dvd = new DVD();
                Book book = new Book();
                CD cd = new CD();
                new ManagerFrame(dvd,book,cd).setVisible(true);
                dispose(); 
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
    }
}
