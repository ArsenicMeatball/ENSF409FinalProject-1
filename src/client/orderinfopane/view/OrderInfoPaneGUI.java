package client.orderinfopane.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.controller.Client;
import client.orderinfopane.controller.BackwardArrowButtonListener;
import client.orderinfopane.controller.ForwardArrowButtonListener;
import client.view.GUI;
import common.model.OrderLine;

/**
 * The GUI that displays the information of an order
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 7th 2019
 */
public class OrderInfoPaneGUI extends JDialog
{
    /** the GUI that this JDialog should be a child of */
    private GUI parent;
    /** the index in the array that we are displaying the order of */
    private int currentIndex;
    /** the arraylist of orders we are displaying */
    private ArrayList<OrderLine> allOrders;
    
    private JLabel infoLabel;
    
    private JButton forwardArrowButton;
    private JButton backwardArrowButton;
    
    private JPanel centrePanel;
    private JPanel southPanel;
    
    /**
     * Generates a toolInfoPane with the given elements
     * 
     * @param user the user that we can communicate with
     * @param parent the parent of this JDialog
     * @param allOrders the order lines that can be displayed by this pane
     */
    public OrderInfoPaneGUI(Client user, ArrayList<OrderLine> allOrders)
    {
        super(user.getFrame(), "Order Info");
        try
        {
            this.parent = user.getFrame();
            this.allOrders = allOrders;
            initializeComponents();
            addToPanels();
            prepareListeners(user);
            prepareWindow();
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(parent, "No orders to display...");
        }
        catch(ArrayIndexOutOfBoundsException aoobe)
        {
            JOptionPane.showMessageDialog(parent, "No orders to display...");
        }
    }
    
    /**
     * initialize the components of the JDialog
     * 
     * @throw NullPointerException
     */
    private void initializeComponents() throws NullPointerException, ArrayIndexOutOfBoundsException
    {
        String orderInfoLabelFriendly = "<html>" + allOrders.get(0).toString().replaceAll("\n", "<br/>") + "</html>";
        
        forwardArrowButton = new JButton(">");
        backwardArrowButton = new JButton("<");
        
        backwardArrowButton.setEnabled(false);
        if(allOrders.size() > 1)
        {
            forwardArrowButton.setEnabled(true);
        }
        else
        {
            forwardArrowButton.setEnabled(false);
        }
        infoLabel = new JLabel(orderInfoLabelFriendly);
        
        centrePanel = new JPanel();
        southPanel = new JPanel();
    }
    
    /**
     * add the components to the right panels
     */
    private void addToPanels()
    {
        southPanel.add(backwardArrowButton);
        southPanel.add(forwardArrowButton);
        centrePanel.add(infoLabel);
    }
    
    /**
     * prepares the listeners for the arrow buttons
     */
    private void prepareListeners(Client user)
    {
        forwardArrowButton.addActionListener(new ForwardArrowButtonListener(this));
        backwardArrowButton.addActionListener(new BackwardArrowButtonListener(this));
    }
    
    /**
     * makes the pane display the next order
     */
    public void advanceOneOrder()
    {
        currentIndex++;
        if(currentIndex >= allOrders.size()-1)
        {
            forwardArrowButton.setEnabled(false);
        }
        else
        {
            forwardArrowButton.setEnabled(true);
        }
        if(allOrders.size() > 1)
        {
            backwardArrowButton.setEnabled(true);
        }
        try
        {
            String orderInfoLabelFriendly = "<html>" + allOrders.get(currentIndex).toString().replaceAll("\n", "<br/>") + "</html>";
            infoLabel.setText(orderInfoLabelFriendly);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, "An error occurred while trying to advance one order...");
        }
    }
    
    /**
     * makes the pane display one order back
     */
    public void goBackOneOrder()
    {
        currentIndex--;
        if(currentIndex <= 0)
        {
            backwardArrowButton.setEnabled(false);
        }
        else
        {
            backwardArrowButton.setEnabled(true);
        }
        if(allOrders.size() > 1)
        {
            forwardArrowButton.setEnabled(true);
        }
        try
        {
            String orderInfoLabelFriendly = "<html>" + allOrders.get(currentIndex).toString().replaceAll("\n", "<br/>") + "</html>";
            infoLabel.setText(orderInfoLabelFriendly);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, "An error occurred while trying to go back one order...");
        }
    }
    
    /**
     * prepares the window with a proper size and with the panels put in
     */
    private void prepareWindow()
    {
        setLayout(new BorderLayout());
        add("Center", centrePanel);
        add("South", southPanel);
        setSize(700,200);
        setResizable(false);
        setVisible(true);
    }
}