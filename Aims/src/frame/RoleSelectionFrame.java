package frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import model.Book;
import model.CD;
import model.DVD;
import model.Manager;


public class RoleSelectionFrame extends JFrame {
    private JButton customerButton;
    private JButton managerButton;

    public RoleSelectionFrame() {
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
            	DVD dvd = new DVD();
            	Book book = new Book();
            	CD cd = new CD();
                CustomerFrame customerFrame = new CustomerFrame(dvd,book,cd);
                customerFrame.setVisible(true);
                dispose();            }
        });

        add(customerButton);
        add(managerButton);
    }
}
