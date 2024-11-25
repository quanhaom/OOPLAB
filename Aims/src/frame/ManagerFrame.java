package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Book;
import model.CD;
import model.DVD;

public class ManagerFrame extends JFrame {
    private DVD dvd; 
    private Book book;
    private CD cd;

    public ManagerFrame(DVD dvd, Book book, CD cd ) { 
        this.dvd = dvd;
        this.book = book;
        this.cd = cd;
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
        ProductFrame productFrame = new ProductFrame(dvd,book,cd);
        productFrame.setVisible(true);
        this.setVisible(false);
    }
    private void showOrders() {
    	OrdersFrame ordersframe = new OrdersFrame();
    	ordersframe.setVisible(true);
        this.setVisible(false);
    }

    private void logout() {
        RoleSelectionFrame roleSelectionFrame = new RoleSelectionFrame();
		roleSelectionFrame.setVisible(true);
        dispose();
    }
}
