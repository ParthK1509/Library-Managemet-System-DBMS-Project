package bms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class AddBookDetailsPane extends JFrame {

    private JLabel titleLabel, yearLabel, shelfNoLabel, hallNoLabel, pubNameLabel, authorFirstNameLabel, authorLastNameLabel, locationLabel, genreLabel;
    private JTextField titleField, yearField, shelfNoField, hallNoField, pubNameField, authorFirstNameField, authorLastNameField, locationField, genreField;
    private JButton addButton;
    private conn c;

    public AddBookDetailsPane(conn c) {
        this.c = c;

        setTitle("Add Book Details");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setVisible(true);

        titleLabel = createStyledLabel("Title:");
        yearLabel = createStyledLabel("Year:");
        shelfNoLabel = createStyledLabel("Shelf Number:");
        hallNoLabel = createStyledLabel("Hall Number:");
        pubNameLabel = createStyledLabel("Publisher Name:");
        authorFirstNameLabel = createStyledLabel("Author First Name:");
        authorLastNameLabel = createStyledLabel("Author Last Name:");
        locationLabel = createStyledLabel("Location:");
        genreLabel = createStyledLabel("Genre:");

        titleField = createStyledTextField();
        yearField = createStyledTextField();
        shelfNoField = createStyledTextField();
        hallNoField = createStyledTextField();
        pubNameField = createStyledTextField();
        authorFirstNameField = createStyledTextField();
        authorLastNameField = createStyledTextField();
        locationField = createStyledTextField();
        genreField = createStyledTextField();

        addButton = new JButton("Add Book");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(52, 152, 219));

        setLayout(new GridLayout(11, 2));

        add(titleLabel);
        add(titleField);
        add(yearLabel);
        add(yearField);
        add(shelfNoLabel);
        add(shelfNoField);
        add(hallNoLabel);
        add(hallNoField);
        add(pubNameLabel);
        add(pubNameField);
        add(authorFirstNameLabel);
        add(authorFirstNameField);
        add(authorLastNameLabel);
        add(authorLastNameField);
        add(locationLabel);
        add(locationField);
        add(genreLabel);
        add(genreField);
        add(new JLabel());
        add(new JLabel());
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addBook();
            }

        });

    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(new Color(44, 62, 80));
        textField.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        return textField;
    }

    private void addBook() {
        String title = titleField.getText();
        int year = Integer.parseInt(yearField.getText());
        int shelfNo = Integer.parseInt(shelfNoField.getText());
        int hallNo = Integer.parseInt(hallNoField.getText());
        String pubName = pubNameField.getText();
        String authorFirstName = authorFirstNameField.getText();
        String authorLastName = authorLastNameField.getText();
        String location = locationField.getText();
        String genre = genreField.getText();

        String message = "";

        try {

            CallableStatement cstmt = c.c.prepareCall("{CALL addBook(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cstmt.setString(1, title);
            cstmt.setInt(2, year);
            cstmt.setInt(3, shelfNo);
            cstmt.setInt(4, hallNo);
            cstmt.setString(5, pubName);
            cstmt.setString(6, authorFirstName);
            cstmt.setString(7, authorLastName);
            cstmt.setString(8, location);
            cstmt.setString(9, genre);
            cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
            cstmt.execute();

            int status = cstmt.getInt(10);

            cstmt.close();

            if (status == 0) {
                message = "Book was added successfully.";
            } else {
                message = "Book copy was added successfully.";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            message = "Error occurred while adding the book.";
        }

        JOptionPane.showMessageDialog(this, message);
    }

}