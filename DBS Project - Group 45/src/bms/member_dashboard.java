package bms;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class member_dashboard extends JFrame implements ActionListener {
    int lib_ID;
    JButton explore, add, viewhold, ret, renew, hold, history, rate, update, logout;
    JTextField search, firstNameField, lastNameField;
    JScrollPane sc;
    JComboBox options;
    String query;
    conn c;
    JTable table;
    DefaultTableModel model;
    ResultSet rs;
    int selectedBookID = -1;
    JLabel welcome;

    member_dashboard(int lib_ID) throws SQLException {
        this.lib_ID = lib_ID;
        c = new conn();
        query = String.format("select concat(fname,' ',lname) from member where lib_id = %d", lib_ID);
        rs = c.s.executeQuery(query);
        String name = "";
        if (rs.next()) {
            name = rs.getString(1);
        }

        this.setLayout(null);
        this.setSize(1170,1080);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        welcome = new JLabel("Welcome " + name);
        welcome.setFont(new Font("Osward", Font.BOLD, (int) (25 * 1.5)));
        welcome.setBounds((int) (200 * 1.5), (int) (20 * 1.5), (int) (400 * 1.5), (int) (30 * 1.5));
        this.add(welcome);

        String[] opt = {"name", "genre", "author"};
        options = new JComboBox(opt);
        options.setFont(new Font("Arial", Font.BOLD, (int) (15 * 1.5)));
        options.setBounds((int) (20 * 1.5), (int) (80 * 1.5), (int) (150 * 1.5), (int) (25 * 1.5));
        options.addActionListener(this);
        this.add(options);

        search = new JTextField();
        search.setText("Search");
        search.setBounds((int) (180 * 1.5), (int) (80 * 1.5), (int) (450 * 1.5), (int) (25 * 1.5));

        this.add(search);

        firstNameField = new JTextField();
        firstNameField.setText("First Name");
        firstNameField.setBounds((int) (180 * 1.5), (int) (80 * 1.5), (int) (200 * 1.5), (int) (25 * 1.5));
        firstNameField.setVisible(false);
        this.add(firstNameField);

        lastNameField = new JTextField();
        lastNameField.setText("Last Name");
        lastNameField.setBounds((int) (400 * 1.5), (int) (80 * 1.5), (int) (230 * 1.5), (int) (25 * 1.5));
        lastNameField.setVisible(false);
        this.add(lastNameField);

        explore = new JButton();
        explore.setBounds((int) (640 * 1.5), (int) (80 * 1.5), (int) (120 * 1.5), (int) (25 * 1.5));
        explore.setBackground(Color.orange);
        explore.setForeground(Color.black);
        explore.setText("Search");
        explore.addActionListener(this);
        this.add(explore);

        table = new JTable();
        table.setBackground(Color.black);
        table.setForeground(Color.white);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {

                        String columnName = model.getColumnName(0);
                        if (columnName.equalsIgnoreCase("book_id")) {

                            selectedBookID = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                        } else {
                            selectedBookID = -1;
                        }
                    }
                }
            }
        });

        sc = new JScrollPane(table);
        sc.setBounds((int) (20 * 1.5), (int) (120 * 1.5), (int) (610 * 1.5), (int) (310 * 1.5));
        add(sc);

        add = new JButton();
        add.setText("Issue");
        add.setBackground(Color.blue);
        add.setForeground(Color.white);
        add.setBounds((int) (640 * 1.5), (int) (120 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        add.addActionListener(this);
        add(add);

        ret = new JButton();
        ret.setText("Return");
        ret.setBackground(Color.blue);
        ret.setForeground(Color.white);
        ret.setBounds((int) (640 * 1.5), (int) (160 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        ret.addActionListener(this);
        add(ret);

        history = new JButton();
        history.setText("Borrow History");
        history.setBackground(Color.blue);
        history.setForeground(Color.white);
        history.setBounds((int) (640 * 1.5), (int) (200 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        history.addActionListener(this);
        add(history);

        viewhold = new JButton();
        viewhold.setText("View Holds");
        viewhold.setBackground(Color.blue);
        viewhold.setForeground(Color.white);
        viewhold.setBounds((int) (640 * 1.5), (int) (240 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        viewhold.addActionListener(this);
        add(viewhold);

        renew = new JButton();
        renew.setText("Renew");
        renew.setBackground(Color.blue);
        renew.setForeground(Color.white);
        renew.setBounds((int) (640 * 1.5), (int) (280 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        renew.addActionListener(this);
        add(renew);

        hold = new JButton();
        hold.setText("Place Hold");
        hold.setBackground(Color.blue);
        hold.setForeground(Color.white);
        hold.setBounds((int) (640 * 1.5), (int) (320 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        hold.addActionListener(this);
        add(hold);

        update = new JButton();
        update.setText("Update Profile");
        update.setBackground(Color.blue);
        update.setForeground(Color.white);
        update.setBounds((int) (640 * 1.5), (int) (360 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        update.addActionListener(this);
        add(update);

        rate = new JButton();
        rate.setText("Rate");
        rate.setBackground(Color.blue);
        rate.setForeground(Color.white);
        rate.setBounds((int) (640 * 1.5), (int) (400 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        rate.addActionListener(this);
        add(rate);

        logout = new JButton();
        logout.setText("Log Out");
        logout.setBackground(Color.black);
        logout.setForeground(Color.white);
        logout.setBounds((int) (640 * 1.5), (int) (20 * 1.5), (int) (120 * 1.5), (int) (30 * 1.5));
        logout.addActionListener(this);
        add(logout);

        try {
            CallableStatement cstmt = c.c.prepareCall("{call borrowHistory(?)}");
            cstmt.setInt(1, lib_ID);
            boolean hadResults = cstmt.execute();
            if (hadResults) {
                rs = cstmt.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();
                model = new DefaultTableModel();
                table.setModel(model);
                int columnsNumber = rsmd.getColumnCount();
                String[] colsname = new String[columnsNumber];
                for (int i = 0; i < columnsNumber; i++) {
                    colsname[i] = rsmd.getColumnName(i + 1);
                }
                model.setColumnIdentifiers(colsname);
                while (rs.next()) {
                    String[] rowData = new String[columnsNumber];
                    for (int i = 0; i < columnsNumber; i++) {
                        rowData[i] = rs.getString(i + 1);
                    }
                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String option = options.getSelectedItem().toString();

        if (option.equals("author")) {
            search.setVisible(false);
            firstNameField.setVisible(true);
            lastNameField.setVisible(true);
        } else {
            search.setVisible(true);
            firstNameField.setVisible(false);
            lastNameField.setVisible(false);
        }

        if (e.getSource() == explore && option.equals("name")) {
            try (CallableStatement cstmt = c.c.prepareCall("{call searchByBookName(?, ?)}")) {
                cstmt.setString(1, search.getText());
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.execute();

                int status = cstmt.getInt(2);
                if (status == 1) {
                    JOptionPane.showMessageDialog(this, "Title entered was not found");
                } else {
                    clearTable();
                    ResultSet rs = cstmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    String[] colsname = new String[columnsNumber];
                    for (int i = 0; i < columnsNumber; i++) {
                        colsname[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colsname);
                    while (rs.next()) {
                        String[] rowData = new String[columnsNumber];
                        for (int i = 0; i < columnsNumber; i++) {
                            rowData[i] = rs.getString(i + 1);
                        }
                        model.addRow(rowData);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == explore && option.equals("genre")) {
            try (CallableStatement cstmt = c.c.prepareCall("{call searchByGenre(?, ?)}")) {
                cstmt.setString(1, search.getText());
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.execute();

                if (cstmt.getInt(2) == 1) {
                    JOptionPane.showMessageDialog(this, "Genre entered was not found");
                } else {
                    clearTable();
                    ResultSet rs = cstmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    String[] colsname = new String[columnsNumber];
                    for (int i = 0; i < columnsNumber; i++) {
                        colsname[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colsname);
                    while (rs.next()) {
                        String[] rowData = new String[columnsNumber];
                        for (int i = 0; i < columnsNumber; i++) {
                            rowData[i] = rs.getString(i + 1);
                        }
                        model.addRow(rowData);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == explore && option.equals("author")) {
            try (CallableStatement cstmt = c.c.prepareCall("{call searchByAuthorName(?, ?, ?)}")) {
                cstmt.setString(1, firstNameField.getText());
                cstmt.setString(2, lastNameField.getText());
                cstmt.registerOutParameter(3, Types.INTEGER);
                cstmt.execute();

                if (cstmt.getInt(3) == 1) {
                    JOptionPane.showMessageDialog(this, "Author entered was not found");
                } else {
                    clearTable();
                    ResultSet rs = cstmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    String[] colsname = new String[columnsNumber];
                    for (int i = 0; i < columnsNumber; i++) {
                        colsname[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colsname);
                    while (rs.next()) {
                        String[] rowData = new String[columnsNumber];
                        for (int i = 0; i < columnsNumber; i++) {
                            rowData[i] = rs.getString(i + 1);
                        }
                        model.addRow(rowData);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == add) {
            if (selectedBookID == -1) {
                JOptionPane.showMessageDialog(this, "Select a book first.");
            } else {
                try (CallableStatement cstmt = c.c.prepareCall("{call withdrawBook(?, ?, ?)}")) {
                    cstmt.setInt(1, selectedBookID);
                    cstmt.setInt(2, lib_ID);
                    cstmt.registerOutParameter(3, Types.INTEGER);
                    cstmt.execute();

                    int outStatus = cstmt.getInt(3);
                    switch (outStatus) {
                        case 0:
                            JOptionPane.showMessageDialog(this, "Book issued successfully.");
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(this, "No copy available for issue.");
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(this, "Book already borrowed by you.");
                            break;
                        case 3:
                            JOptionPane.showMessageDialog(this, "Book does not exist in the library.");
                            break;
                        case 4:
                            JOptionPane.showMessageDialog(this, "Your account is locked.");
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown status code: " + outStatus);
                    }

                    if (outStatus == 0) {
                        try (CallableStatement historyStmt = c.c.prepareCall("{call borrowHistory(?)}")) {
                            historyStmt.setInt(1, lib_ID);
                            boolean hadResults = historyStmt.execute();
                            if (hadResults) {
                                ResultSet rs = historyStmt.getResultSet();
                                ResultSetMetaData rsmd = rs.getMetaData();
                                model = new DefaultTableModel();
                                table.setModel(model);
                                int columnsNumber = rsmd.getColumnCount();
                                String[] colsname = new String[columnsNumber];
                                for (int i = 0; i < columnsNumber; i++) {
                                    colsname[i] = rsmd.getColumnName(i + 1);
                                }
                                model.setColumnIdentifiers(colsname);
                                while (rs.next()) {
                                    String[] rowData = new String[columnsNumber];
                                    for (int i = 0; i < columnsNumber; i++) {
                                        rowData[i] = rs.getString(i + 1);
                                    }
                                    model.addRow(rowData);
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                selectedBookID = -1;
            }
        } else if (e.getSource() == ret) {
            if (selectedBookID == -1) {
                JOptionPane.showMessageDialog(this, "Select a book to return first.");
            } else {
                try (CallableStatement cstmt = c.c.prepareCall("{call returnBook(?, ?, ?)}")) {
                    cstmt.setInt(1, lib_ID);
                    cstmt.setInt(2, selectedBookID);
                    cstmt.registerOutParameter(3, Types.INTEGER);
                    cstmt.execute();

                    int outStatus = cstmt.getInt(3);
                    switch (outStatus) {
                        case 0:
                            JOptionPane.showMessageDialog(this, "Book returned successfully.");
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(this, "You have not issued this book");
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown status code: " + outStatus);
                    }

                    if (outStatus == 0) {
                        try (CallableStatement historyStmt = c.c.prepareCall("{call borrowHistory(?)}")) {
                            historyStmt.setInt(1, lib_ID);
                            boolean hadResults = historyStmt.execute();
                            if (hadResults) {
                                ResultSet rs = historyStmt.getResultSet();
                                ResultSetMetaData rsmd = rs.getMetaData();
                                model = new DefaultTableModel();
                                table.setModel(model);
                                int columnsNumber = rsmd.getColumnCount();
                                String[] colsname = new String[columnsNumber];
                                for (int i = 0; i < columnsNumber; i++) {
                                    colsname[i] = rsmd.getColumnName(i + 1);
                                }
                                model.setColumnIdentifiers(colsname);
                                while (rs.next()) {
                                    String[] rowData = new String[columnsNumber];
                                    for (int i = 0; i < columnsNumber; i++) {
                                        rowData[i] = rs.getString(i + 1);
                                    }
                                    model.addRow(rowData);
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                selectedBookID = -1;
            }
        } else if (e.getSource() == renew) {
            if (selectedBookID == -1) {
                JOptionPane.showMessageDialog(this, "Select a book to renew first.");
            } else {
                try (CallableStatement cstmt = c.c.prepareCall("{call renewBook(?, ?, ?)}")) {
                    cstmt.setInt(1, selectedBookID);
                    cstmt.setInt(2, lib_ID);
                    cstmt.registerOutParameter(3, Types.INTEGER);
                    cstmt.execute();

                    int outStatus = cstmt.getInt(3);
                    switch (outStatus) {
                        case 0:
                            JOptionPane.showMessageDialog(this, "Book renewed successfully.");
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(this, "You have not issued this book.");
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(this, "There is a pending hold on this book.");
                            break;
                        case 3:
                            JOptionPane.showMessageDialog(this, "Borrow duration exceeded 20 days.");
                            break;
                        case 4:
                            JOptionPane.showMessageDialog(this, "Your account is locked.");
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown status code: " + outStatus);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == hold) {

            if (selectedBookID == -1) {
                JOptionPane.showMessageDialog(this, "Select a book first.");
            } else {

                try (CallableStatement cstmt = c.c.prepareCall("{call placeHold(?, ?, ?)}")) {
                    cstmt.setInt(1, lib_ID);
                    cstmt.setInt(2, selectedBookID);
                    cstmt.registerOutParameter(3, Types.INTEGER);
                    cstmt.execute();

                    int outStatus = cstmt.getInt(3);
                    switch (outStatus) {
                        case 0:
                            JOptionPane.showMessageDialog(this, "Hold placed successfully.");
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(this, "You have already issued this book.");
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(this, "Your account is locked.");
                            break;
                        case 3:
                            JOptionPane.showMessageDialog(this, "Book is currently available for borrowing.");
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown status code: " + outStatus);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == update) {

            String[] options = {"Name", "Gender", "Password"};
            String selectedOption = (String) JOptionPane.showInputDialog(null, "Choose option to update:", "Update Details",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (selectedOption != null) {
                if (selectedOption.equals("Name")) {

                    String newFirstName = JOptionPane.showInputDialog("Enter new first name:");
                    String newLastName = JOptionPane.showInputDialog("Enter new last name:");

                    try (CallableStatement cstmt = c.c.prepareCall("{call updateName(?, ?, ?, ?)}")) {
                        cstmt.setString(1, newFirstName);
                        cstmt.setString(2, newLastName);
                        cstmt.setInt(3, lib_ID);
                        cstmt.registerOutParameter(4, Types.INTEGER);
                        cstmt.execute();

                        int outStatus = cstmt.getInt(4);
                        if (outStatus == 0) {
                            JOptionPane.showMessageDialog(this, "Name updated successfully.");
                            query = String.format("select concat(fname,' ',lname) from member where lib_id = %d", lib_ID);
                            rs = c.s.executeQuery(query);
                            String name1 = "";
                            if (rs.next()) {
                                name1 = rs.getString(1);
                            }
                            welcome.setText("Welcome " + name1);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update name.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else if (selectedOption.equals("Gender")) {

                    String newGender = JOptionPane.showInputDialog("Enter new gender (M/F):");

                    try (CallableStatement cstmt = c.c.prepareCall("{call updateGender(?, ?, ?)}")) {
                        cstmt.setString(1, newGender);
                        cstmt.setInt(2, lib_ID);
                        cstmt.registerOutParameter(3, Types.INTEGER);
                        cstmt.execute();

                        int outStatus = cstmt.getInt(3);
                        if (outStatus == 0) {
                            JOptionPane.showMessageDialog(this, "Gender updated successfully.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update gender.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else if (selectedOption.equals("Password")) {

                    String oldPassword = JOptionPane.showInputDialog("Enter old password:");
                    String newPassword = JOptionPane.showInputDialog("Enter new password:");

                    try (CallableStatement cstmt = c.c.prepareCall("{call updatePassword(?, ?, ?, ?)}")) {
                        cstmt.setString(1, oldPassword);
                        cstmt.setString(2, newPassword);
                        cstmt.setInt(3, lib_ID);
                        cstmt.registerOutParameter(4, Types.INTEGER);
                        cstmt.execute();

                        int outStatus = cstmt.getInt(4);
                        if (outStatus == 0) {
                            JOptionPane.showMessageDialog(this, "Password updated successfully.");
                        } else if (outStatus == 1) {
                            JOptionPane.showMessageDialog(this, "Incorrect old password.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update password.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (e.getSource() == history) {
            try {
                CallableStatement cstmt = c.c.prepareCall("{call borrowHistory(?)}");
                cstmt.setInt(1, lib_ID);
                boolean hadResults = cstmt.execute();
                if (hadResults) {
                    rs = cstmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    model = new DefaultTableModel();
                    table.setModel(model);
                    int columnsNumber = rsmd.getColumnCount();
                    String[] colsname = new String[columnsNumber];
                    for (int i = 0; i < columnsNumber; i++) {
                        colsname[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colsname);
                    while (rs.next()) {
                        String[] rowData = new String[columnsNumber];
                        for (int i = 0; i < columnsNumber; i++) {
                            rowData[i] = rs.getString(i + 1);
                        }
                        model.addRow(rowData);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == viewhold) {
            try {
                CallableStatement cstmt = c.c.prepareCall("{call viewHolds(?)}");
                cstmt.setInt(1, lib_ID);
                boolean hadResults = cstmt.execute();
                if (hadResults) {
                    rs = cstmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    model = new DefaultTableModel();
                    table.setModel(model);
                    int columnsNumber = rsmd.getColumnCount();
                    String[] colsname = new String[columnsNumber];
                    for (int i = 0; i < columnsNumber; i++) {
                        colsname[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colsname);
                    while (rs.next()) {
                        String[] rowData = new String[columnsNumber];
                        for (int i = 0; i < columnsNumber; i++) {
                            rowData[i] = rs.getString(i + 1);
                        }
                        model.addRow(rowData);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == rate) {
            if (selectedBookID == -1) {
                JOptionPane.showMessageDialog(this, "Select a book first.");
            } else {

                Integer[] ratings = {1, 2, 3, 4, 5};
                Integer selectedRating = (Integer) JOptionPane.showInputDialog(this,
                        "Select a rating:", "Rate Book", JOptionPane.PLAIN_MESSAGE, null,
                        ratings, ratings[0]);

                if (selectedRating != null) {
                    try (CallableStatement cstmt = c.c.prepareCall("{call insertRating(?, ?, ?, ?)}")) {
                        cstmt.setInt(1, lib_ID);
                        cstmt.setInt(2, selectedBookID);
                        cstmt.setInt(3, selectedRating);
                        cstmt.registerOutParameter(4, Types.INTEGER);
                        cstmt.execute();

                        int outStatus = cstmt.getInt(4);
                        if (outStatus == 0) {
                            JOptionPane.showMessageDialog(this, "Your rating has been recorded.");
                        } else if (outStatus == 1) {
                            JOptionPane.showMessageDialog(this, "You have not yet issued this book.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Unknown status code: " + outStatus);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (e.getSource() == logout) {
            setVisible(false);
            lib_ID = -1;
            new Login().setVisible(true);
        }
    }

    private void clearTable() {
        model.setRowCount(0);
    }

    public static void main(String args[]) throws SQLException {
        new member_dashboard(11);
    }
}