package frame;

import services.MySQLConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.DVD;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersFrame extends JFrame {
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private MySQLConnection mySQLConnection;

    public OrdersFrame() {
        mySQLConnection = new MySQLConnection();
        setTitle("Orders Management");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Card Owner", "Card Number", "Total Cost", "Status", "Address", "Instructions"}, 0);
        ordersTable = new JTable(tableModel);
        loadOrders();

        JButton acceptButton = new JButton("Accept Order");
        JButton denyButton = new JButton("Deny Order");
        JButton backButton = new JButton("Back");

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrderStatus("accepted");
            }
        });

        denyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrderStatus("denied");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                DVD dvd = new DVD();
                new ManagerFrame(dvd).setVisible(true);
            }
        });

        // Layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(denyButton);
        buttonPanel.add(backButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        try (Connection conn = mySQLConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT orderID, cardOwner, cardNumber, totalCost, status, address, instructions FROM orders");
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                String orderID = rs.getString("orderID");
                String cardOwner = rs.getString("cardOwner");
                String cardNumber = rs.getString("cardNumber");
                double totalCost = rs.getDouble("totalCost");
                String status = rs.getString("status");
                String address = rs.getString("address");
                String instructions = rs.getString("instructions");
                tableModel.addRow(new Object[]{orderID, cardOwner, cardNumber, totalCost, status, address, instructions});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderStatus(String newStatus) {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderID = (String) tableModel.getValueAt(selectedRow, 0);

            try (Connection conn = mySQLConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE orders SET status = ? WHERE orderID = ?")) {
                
                pstmt.setString(1, newStatus);
                pstmt.setString(2, orderID);
                pstmt.executeUpdate();
                
                tableModel.setValueAt(newStatus, selectedRow, 4);
                JOptionPane.showMessageDialog(this, "Status updated to " + newStatus + " successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
        }
    }

}
