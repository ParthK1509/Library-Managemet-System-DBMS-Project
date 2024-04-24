package bms;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class signteacher extends JFrame implements ActionListener {
    long phone;
    String fname, lname, email, pw;
    String gender;
    JTextField teacher_idtext, depttext, designtext;
    JButton next;
    conn c;
    signup_member sm;

    public signteacher(String fname, String lname, String gender, String email, long phone, String pw, conn c, signup_member sm) {
        this.sm = sm;
        this.c = c;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.pw = pw;
        setLayout(null);
        setSize(800, 600);
        setLocation(350, 200);
        getContentPane().setBackground(Color.white);

        JLabel text = new JLabel("Sign Up Teacher");
        text.setFont(new Font("Osward", Font.BOLD, 25));
        text.setBounds(260, 20, 400, 40);
        add(text);

        JLabel teacher_id = new JLabel("Teacher ID:");
        teacher_id.setFont(new Font("Raleway", Font.BOLD, 14));
        teacher_id.setBounds(100, 140, 150, 20);
        add(teacher_id);
        teacher_idtext = new JTextField();
        teacher_idtext.setBounds(260, 140, 300, 30);
        teacher_idtext.addActionListener(this);
        add(teacher_idtext);

        JLabel dept = new JLabel("Department:");
        dept.setFont(new Font("Raleway", Font.BOLD, 14));
        dept.setBounds(100, 200, 150, 20);
        add(dept);
        depttext = new JTextField();
        depttext.setBounds(260, 200, 300, 30);
        depttext.addActionListener(this);
        add(depttext);

        JLabel design = new JLabel("Designation:");
        design.setFont(new Font("Raleway", Font.BOLD, 14));
        design.setBounds(100, 260, 150, 20);
        add(design);
        designtext = new JTextField();
        designtext.setBounds(260, 260, 300, 30);
        designtext.addActionListener(this);
        add(designtext);

        next = new JButton("Sign Up");
        next.setForeground(Color.WHITE);
        next.setBackground(Color.BLACK);
        next.setFont(new Font("Raleway", Font.BOLD, 14));
        next.setBounds(650, 500, 100, 40);
        next.addActionListener(this);
        add(next);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String teacher_id = teacher_idtext.getText();
        String dept = depttext.getText();
        String design = designtext.getText();

        if (teacher_id.equals(("")) || dept.equals("") || design.equals("")) {
            JOptionPane.showMessageDialog(null, "please fill all the fields");
        }
        else {
            try (
                    CallableStatement cstmt = c.c.prepareCall("{call addTeacher(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");) {
                cstmt.setString(1, fname);
                cstmt.setString(2, lname);
                cstmt.setString(3, gender);
                cstmt.setString(4, email);
                cstmt.setLong(5, phone);
                cstmt.setString(6, pw);
                cstmt.setString(7, teacher_id);
                cstmt.setString(8, design);
                cstmt.setString(9, dept);
                cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
                cstmt.execute();
                if (cstmt.getInt(10) == 0) {
                    JOptionPane.showMessageDialog(null, "Signed Up successfully");
                    setVisible(false);
                    new Login();
                } else if (cstmt.getInt(10) == 1) {
                    JOptionPane.showMessageDialog(null, "Teacher ID already exists");
                } else if (cstmt.getInt(10) == 2) {
                    JOptionPane.showMessageDialog(null, "Email ID already exists");
                    this.setVisible(false);
                    sm.setVisible(true);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}