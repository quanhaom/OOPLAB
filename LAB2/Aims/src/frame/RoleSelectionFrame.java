package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import services.Store;

public class RoleSelectionFrame extends JFrame {
    private JButton customerButton;
    private JButton managerButton;

    public RoleSelectionFrame(Store store) {
        setTitle("Select Role");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1));

        customerButton = new JButton("Customer");
        managerButton = new JButton("Manager");

        // Action listener for Manager button
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the LoginFrame for Manager
                LoginFrame loginFrame = new LoginFrame(store);
                loginFrame.setVisible(true);
                dispose(); // Close RoleSelectionFrame
            }
        });

        // Action listener for Customer button
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Customer Frame or handle customer logic here
                CustomerFrame customerFrame = new CustomerFrame(store,null);  // You might have a CustomerFrame
                customerFrame.setVisible(true);
                dispose(); // Close RoleSelectionFrame
            }
        });

        add(customerButton);
        add(managerButton);
    }
}
