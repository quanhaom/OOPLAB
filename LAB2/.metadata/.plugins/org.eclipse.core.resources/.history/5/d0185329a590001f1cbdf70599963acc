package frame;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.Person;
import services.Store;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private Store store; 

    public LoginFrame(Store store) {
        this.store = store;

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        //icon
        URL url = getClass().getClassLoader().getResource("icon.png");
            Image icon = Toolkit.getDefaultToolkit().getImage(url);
            setIconImage(icon);


        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton dontLoginButton = new JButton("Don't Login");
        JButton signUpButton = new JButton("Sign Up");

        setLayout(new GridLayout(4, 2)); 
        add(userIdLabel);
        add(userIdField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(dontLoginButton);
        add(signUpButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(userIdField.getText(), new String(passwordField.getPassword()));
            }
        });

        dontLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StrangerFrame(store, null);
                dispose();
            }
        });
        
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpFrame(LoginFrame.this);
                dispose();
            }
        });
    }

    private void login(String username, String password) {
        List<Person> users = store.getUsers(); 
        for (Person user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) { 
                String name = user.getName();
                switch (user.getRole()) {
                    case "Employee":
                        new EmployeeFrame(store, this,user.getId(), name).setVisible(true);
                        break;
                    case "Customer":
                        new CustomerFrame(store, this, user.getId(), name).setVisible(true); 
                        break;
                    case "Manager":
                        new ManagerFrame(store, this, name).setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown role: " + user.getRole());
                        return;
                }
                dispose(); 
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
    }

    public static void main(String[] args) {
        Store store = new Store(); 

        LoginFrame loginFrame = new LoginFrame(store);
        loginFrame.setVisible(true); 
    }
}
