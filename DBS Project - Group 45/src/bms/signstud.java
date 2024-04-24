package bms;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class signstud extends JFrame implements ActionListener {
    long phone;
    String fname, lname, email, pw;
    String gender;
    JTextField bits_idtext, branchtext, semestertext;
    JButton next;
    conn c;
    signup_member sm;
    public signstud(String fname, String lname, String gender, String email, long phone, String pw, conn c, signup_member sm){
        this.sm = sm;
        this.c = c;
        this.pw = pw;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.setLayout(null);
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.white);

        JLabel text = new JLabel("Sign Up Student");
        text.setFont(new Font("Osward", Font.BOLD, 25));
        text.setBounds(260,20,400,40);
        add(text);

        JLabel bits_id = new JLabel("BITS ID:");
        bits_id.setFont(new Font("Raleway", Font.BOLD, 14));
        bits_id.setBounds(100,140,150,20);
        add(bits_id);
        bits_idtext = new JTextField();
        bits_idtext.setBounds(260, 140, 300, 30);
        bits_idtext.addActionListener(this);
        add(bits_idtext);

        JLabel branch = new JLabel("Branch:");
        branch.setFont(new Font("Raleway", Font.BOLD, 14));
        branch.setBounds(100,200,150,20);
        add(branch);
        branchtext = new JTextField();
        branchtext.setBounds(260, 200, 300, 30);
        branchtext.addActionListener(this);
        add(branchtext);

        JLabel semester = new JLabel("Semester:");
        semester.setFont(new Font("Raleway", Font.BOLD, 14));
        semester.setBounds(100,260,150,20);
        add(semester);
        semestertext = new JTextField();
        semestertext.setBounds(260, 260, 300, 30);
        semestertext.addActionListener(this);
        add(semestertext);

        next = new JButton("Sign Up");
        next.setForeground(Color.WHITE);
        next.setBackground(Color.BLACK);
        next.setFont(new Font("Raleway", Font.BOLD, 14));
        next.setBounds(650,500,100,40);
        next.addActionListener(this);
        add(next);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String bits_id = bits_idtext.getText();
        String branch = branchtext.getText();
        int sem = Integer.parseInt(semestertext.getText());

        if (bits_id.equals("") || branch.equals("") || sem == 0) {
            JOptionPane.showMessageDialog(null, "Please fill all the fields");
        }
        else {
            try (
                    CallableStatement cstmt = c.c.prepareCall("{call addStudent(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");) {
                cstmt.setString(1, fname);
                cstmt.setString(2, lname);
                cstmt.setString(3, gender);
                cstmt.setString(4, email);
                cstmt.setLong(5, phone);
                cstmt.setString(6,pw);
                cstmt.setString(7,bits_id);
                cstmt.setString(8,branch);
                cstmt.setInt(9,sem);
                cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
                cstmt.execute();
                if(cstmt.getInt(10) == 0) {
                    JOptionPane.showMessageDialog(null, "Signed up successfully");
                    setVisible(false);
                    new Login();
                }
                else if(cstmt.getInt(10) == 1) {
                    JOptionPane.showMessageDialog(null, "BITS ID already exists");
                }
                else if(cstmt.getInt(10) == 2) {
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