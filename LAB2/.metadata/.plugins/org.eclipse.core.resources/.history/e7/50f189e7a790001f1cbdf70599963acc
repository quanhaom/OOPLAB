package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Product;
import services.Store;

public class CustomerFrame extends BaseFrame {
    private List<Product> cart;
    private JLabel greetingLabel;
    private String userId;
    private LoginFrame loginFrame;

    public CustomerFrame(Store store, LoginFrame loginFrame, String userId, String Name) { 
        super(store);  
        this.loginFrame = loginFrame;
        this.cart = new ArrayList<>();  
        this.userId = userId;
        setTitle("Customer Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Check out");

        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(addToCartButton);
        JPanel greetingPanel = new JPanel();
        if (Name == null) {
        	greetingLabel = new JLabel("Hello, Stranger ");
        	greetingPanel.add(greetingLabel);
            greetingLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showLogout();
                }
            });
        }
        else {
        	greetingLabel = new JLabel("Hello, " + Name);
        	greetingPanel.add(greetingLabel);
            greetingLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showGreetingOptions();
                }
            });
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(greetingPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.SOUTH);

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCart();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });



        displayAllProducts();
    }

    private void showGreetingOptions() {
        String[] options = {"Log out", "Edit Profile"};
        int choice = JOptionPane.showOptionDialog(this, "Choose an option:", "Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            logout();
        } else if (choice == 1) {
            openEditProfile();
        }
    }
    public void openEditProfile() {
        new EditPro5( userId);
    }
    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();

        if (selectedRow != -1) {
            String productId = (String) tableModel.getValueAt(selectedRow, 0);
            for (Product product : cart) {
                if (product.getId().equals(productId)) {
                    product.setQuantity(product.getQuantity() + 1);
                    JOptionPane.showMessageDialog(this, product.getName() + " quantity increased to " + product.getQuantity());
                    return; 
                }
            }
            for (Product product : store.getProducts()) {
                if (product.getId().equals(productId)) {
                    Product newProduct = new Product(product.getId(), product.getName(), product.getPrice(), 1, product.getInputPrice());
                    cart.add(newProduct);
                    JOptionPane.showMessageDialog(this, product.getName() + " has been added to your cart.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Product not found in the store.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid product to add to cart.");
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        StringBuilder cartContents = new StringBuilder("Your Cart:\n");
        for (Product product : cart) {
            cartContents.append(String.format("%s - Price: %.2f - Quantity: %d\n", 
                product.getName(), product.getPrice(), product.getQuantity()));
        }

        JOptionPane.showMessageDialog(this, cartContents.toString());
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        StringBuilder checkoutSummary = new StringBuilder("You have checked out:\n");
        for (Product product : cart) {
            String productId = product.getId();
            int quantityToCheckout = product.getQuantity();

            for (Product storeProduct : store.getProducts()) {
                if (storeProduct.getId().equals(productId)) {
                    int currentStock = storeProduct.getQuantity();

                    if (currentStock < quantityToCheckout) {
                        JOptionPane.showMessageDialog(this, "Cannot check out " + quantityToCheckout + " of " + product.getName() + " due to insufficient stock.");
                        return;
                    }
                    storeProduct.setQuantity(currentStock - quantityToCheckout);
                    store.updateProductQuantity(storeProduct.getId(), currentStock - quantityToCheckout);
                    double profit = storeProduct.getPrice() - storeProduct.getInputPrice();
                    store.insertProductChangeHistory(storeProduct.getName(), storeProduct.getInputPrice(), storeProduct.getPrice(), profit, "sell", quantityToCheckout);

                    checkoutSummary.append(String.format("%s - Price: %.2f - Quantity: %d\n", 
                        storeProduct.getName(), storeProduct.getPrice(), quantityToCheckout));
                    break; 
                }
            }
        }

        JOptionPane.showMessageDialog(this, checkoutSummary.toString());
        cart.clear();
        displayAllProducts();
    }


    private void logout() {
        this.dispose();
        loginFrame.setVisible(true);
    }
    private void showLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            LoginFrame loginFrame = new LoginFrame(null); 
            loginFrame.setVisible(true);
        } 
    } 
}
