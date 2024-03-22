/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package registrationform1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationForm1 extends JFrame {
    private final JTextField nameTextField;
    private final JTextField mobileTextField;
    private final JRadioButton maleRadioButton;
    private final JRadioButton femaleRadioButton;
    private final JComboBox<String> dayComboBox;
    private final JComboBox<String> monthComboBox;
    private final JComboBox<String> yearComboBox;
    private final JTextArea addressTextArea;
    private final JCheckBox termsCheckBox;
    private final JButton submitButton;
    private final JButton resetButton;

    public RegistrationForm1() {
        // GUI initialization
                setTitle("Registration Form");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // North Panel
        JPanel northPanel = new JPanel(new GridLayout(0, 2));
        add(northPanel, BorderLayout.NORTH);

        northPanel.add(new JLabel("Name"));
        nameTextField = new JTextField();
        northPanel.add(nameTextField);

        northPanel.add(new JLabel("Mobile"));
        mobileTextField = new JTextField();
        northPanel.add(mobileTextField);

        northPanel.add(new JLabel("Gender"));
        JPanel genderPanel = new JPanel(new FlowLayout());
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        northPanel.add(genderPanel);

        northPanel.add(new JLabel("DOB"));
        JPanel dobPanel = new JPanel(new FlowLayout());
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = Integer.toString(i);
        }
        dayComboBox = new JComboBox<>(days);
        monthComboBox = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        yearComboBox = new JComboBox<>(new String[]{"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000"});
        dobPanel.add(dayComboBox);
        dobPanel.add(monthComboBox);
        dobPanel.add(yearComboBox);
        northPanel.add(dobPanel);

        northPanel.add(new JLabel("Address"));
        addressTextArea = new JTextArea(2, 20);
        northPanel.add(new JScrollPane(addressTextArea));

        termsCheckBox = new JCheckBox("Accept Terms And Conditions.");
        northPanel.add(termsCheckBox);

        // South Panel
        JPanel southPanel = new JPanel(new FlowLayout());
        add(southPanel, BorderLayout.SOUTH);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this::submitAction);
        southPanel.add(submitButton);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(this::resetAction);
        southPanel.add(resetButton);
        
        // Action listeners
        submitButton.addActionListener(this::submitAction);
        resetButton.addActionListener(this::resetAction);
    }

    private void submitAction(ActionEvent e) {
        if (termsCheckBox.isSelected()) {
            insertData();
        } else {
            JOptionPane.showMessageDialog(this, "Please accept the terms and conditions.");
        }
    }

    private void resetAction(ActionEvent e) {
        // Reset fields
                nameTextField.setText("");
        mobileTextField.setText("");
        addressTextArea.setText("");
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        dayComboBox.setSelectedIndex(0);
        monthComboBox.setSelectedIndex(0);
        yearComboBox.setSelectedIndex(0);
        termsCheckBox.setSelected(false);
        // ... (Reset logic)
    }

    private void insertData() {
        String name = nameTextField.getText();
        String mobile = mobileTextField.getText();
        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        String day = (String) dayComboBox.getSelectedItem();
        String month = (String) monthComboBox.getSelectedItem();
        String year = (String) yearComboBox.getSelectedItem();
        String dob = year + "-" + month + "-" + day; // Make sure to match the format expected by your DB
        String address = addressTextArea.getText();

        String databaseUrl = "jdbc:mysql://localhost:3306/form";
        String user = "root";
        String password = "";
        String sql = "INSERT INTO records (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(databaseUrl, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, mobile);
            stmt.setString(3, gender);
            stmt.setString(4, dob);
            stmt.setString(5, address);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data inserted successfully.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationForm1 frame = new RegistrationForm1();
            frame.setVisible(true);
        });
    }
}
