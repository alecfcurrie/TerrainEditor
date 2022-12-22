package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

// A class representing a dialog that allows the user to resize the given map
public class TerrainResizeDialog extends JDialog implements ActionListener {

    public static final String CANCEL = "Cancel";
    public static final String OK = "OK";
    private JTextField newWidth;
    private JTextField newHeight;

    private boolean cancelled = true;

    // EFFECTS: Constructs a new MapResizeDialog
    public TerrainResizeDialog(JFrame owner) {
        super(owner, "Map Resize Tool");
        setModal(true);
        initializeComponents();
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 0, 13));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    // MODIFIES: this
    // EFFECTS: Initializes all the components of the dialog
    private void initializeComponents() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        newWidth = new JTextField();
        newHeight = new JTextField();
        inputPanel.add(new JLabel("Enter the new width: "));
        inputPanel.add(newWidth);
        inputPanel.add(new JLabel("Enter the new height: "));
        inputPanel.add(newHeight);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton(CANCEL);
        cancelButton.setActionCommand(CANCEL);
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        JButton okButton = new JButton(OK);
        okButton.setActionCommand(OK);
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        add(inputPanel);
        add(buttonPanel);
    }

    // EFFECTS: Handles user input when they close the application
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(OK)) {
            cancelled = false;
        }
        dispose();
    }

    // EFFECTS: Parses and returns the width entered by the user. Throws NumberFormatException if it fails to parse
    //          the input
    public int getNewWidth() throws NumberFormatException {
        return Integer.parseInt(newWidth.getText());
    }

    // EFFECTS: Parses and returns the height entered by the user. Throws NumberFormatException if it fails to parse
    //          the input
    public int getNewHeight() throws NumberFormatException {
        return Integer.parseInt(newHeight.getText());
    }

    public boolean isCancelled() {
        return cancelled;
    }


}
