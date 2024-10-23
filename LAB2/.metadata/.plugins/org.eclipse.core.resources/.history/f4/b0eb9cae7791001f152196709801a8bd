package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.DVD;


public class ManagerFrame extends JFrame {
    private DVD dvd; 

    public ManagerFrame(DVD dvd) { 
        this.dvd = dvd;
        setTitle("Manager Frame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel buttonPanel = new JPanel();
        JButton productsButton = new JButton("Products");
        JButton ordersButton = new JButton("Orders");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(productsButton);
        buttonPanel.add(ordersButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> logout());
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProducts();
            }
        });

        ordersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOrders();
            }
        });
    }

    private void showProducts() {
        ProductFrame productFrame = new ProductFrame(dvd);
        productFrame.setVisible(true);
        this.setVisible(false);
    }
    private void showOrders() {
    	OrdersFrame ordersframe = new OrdersFrame();
    	ordersframe.setVisible(true);
        this.setVisible(false);
    }

    private void logout() {
        RoleSelectionFrame roleSelectionFrame = new RoleSelectionFrame(dvd);
		roleSelectionFrame.setVisible(true);
        dispose();
    }
}
