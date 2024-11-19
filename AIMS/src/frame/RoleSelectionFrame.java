package frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import model.DVD;
import model.Manager;


public class RoleSelectionFrame extends JFrame {
    private JButton customerButton;
    private JButton managerButton;

    public RoleSelectionFrame( DVD dvd) {
        setTitle("Select Role");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1));

        customerButton = new JButton("Customer");
        managerButton = new JButton("Manager");

        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Manager manager = new Manager(null,null);
                LoginFrame loginFrame = new LoginFrame(manager);
                loginFrame.setVisible(true);
                dispose();
            }
        });

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerFrame customerFrame = new CustomerFrame(dvd);
                customerFrame.setVisible(true);
                dispose();            }
        });

        add(customerButton);
        add(managerButton);
    }
}
