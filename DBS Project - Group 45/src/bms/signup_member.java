package bms;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class signup_member extends JFrame implements ActionListener {
    JTextField fnametext, lnametext, ptext, etext, pw;
    JButton next;
    JRadioButton male, female, other;
    JComboBox dropdown;
    conn c;
    public signup_member(conn c){
        this.setLayout(null);
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.white);
        this.c = c;

        JLabel text = new JLabel("Sign Up Member");
        text.setFont(new Font("Osward", Font.BOLD, 25));
        text.setBounds(260,20,400,40);
        add(text);

        JLabel fname = new JLabel("First Name:");
        fname.setFont(new Font("Raleway", Font.BOLD, 14));
        fname.setBounds(100,80,150,20);
        add(fname);
        fnametext = new JTextField();
        fnametext.setBounds(260, 80, 300, 30);
        fnametext.addActionListener(this);
        add(fnametext);

        JLabel lname = new JLabel("Last Name:");
        lname.setFont(new Font("Raleway", Font.BOLD, 14));
        lname.setBounds(100,140,150,20);
        add(lname);
        lnametext = new JTextField();
        lnametext.setBounds(260, 140, 300, 30);
        lnametext.addActionListener(this);
        add(lnametext);

        JLabel gender = new JLabel("Gender:");
        gender.setFont(new Font("Raleway", Font.BOLD, 14));
        gender.setBounds(100,200,150,20);
        add(gender);

        male = new JRadioButton("Male");
        male.setBounds(260, 200, 100, 30);
        male.setBackground(Color.WHITE);
        add(male);
        female = new JRadioButton("Female");
        female.setBounds(370, 200, 120, 30);
        female.setBackground(Color.WHITE);
        add(female);
        other = new JRadioButton("Other");
        other.setBounds(500, 200, 120, 30);
        other.setBackground(Color.WHITE);
        add(other);
        ButtonGroup gendergroup = new ButtonGroup();
        gendergroup.add(male);
        gendergroup.add(female);
        gendergroup.add(other);

        JLabel phone = new JLabel("Phone No:");
        phone.setFont(new Font("Raleway", Font.BOLD, 14));
        phone.setBounds(100,260,150,20);
        add(phone);
        ptext = new JTextField();
        ptext.setBounds(260, 260, 300, 30);
        ptext.addActionListener(this);
        add(ptext);

        JLabel email = new JLabel("Email:");
        email.setFont(new Font("Raleway", Font.BOLD, 14));
        email.setBounds(100,320,150,20);
        add(email);
        etext = new JTextField();
        etext.setBounds(260, 320, 300, 30);
        etext.addActionListener(this);
        add(etext);

        JLabel mtype = new JLabel("Member Type:");
        mtype.setFont(new Font("Raleway", Font.BOLD, 14));
        mtype.setBounds(100,380,150,20);
        add(mtype);

        String[] members = {"Student", "Teacher"};
        dropdown = new JComboBox<>(members);
        dropdown.setBounds(260,385,150,20);
        dropdown.addActionListener(this);
        add(dropdown);

        JLabel password = new JLabel("Password:");
        password.setFont(new Font("Raleway", Font.BOLD, 14));
        password.setBounds(100,440,150,20);
        add(password);
        pw = new JTextField();
        pw.setBounds(260, 440, 300, 30);
        pw.addActionListener(this);
        add(pw);

        next = new JButton("Next");
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
        if (e.getSource() == next) {
            String fname = fnametext.getText();
            String lname = lnametext.getText();
            long phone = Long.parseLong(ptext.getText());
            String email = etext.getText();
            String gender = "";
            String pass = pw.getText();

            if (male.isSelected()) {
                gender = "M";
            } else if (female.isSelected()) {
                gender = "F";
            } else if (other.isSelected()) {
                gender = "O";
            }
            String member = null;
            if (dropdown.getSelectedItem().toString().equals("Student")) {
                member = "Student";
            } else if (dropdown.getSelectedItem().toString().equals("Teacher")) {
                member = "Teacher";
            }

            try {
                if (ptext.getText().length() != 10) {
                    JOptionPane.showMessageDialog(null, "Phone number should be 10 digits long");
                    return;
                }
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid phone number");
                return;
            }

            if (!email.matches(".+@.+\\..+")) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email address");
                return;
            }

            try {
                if (fname.equals("") || lname.equals("") || phone == 0 || email.equals("") || member.equals("") || pass.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                } else {
                    setVisible(false);
                    if (member.equals("Student")) {
                        new signstud(fname, lname, gender, email, phone, pass, c, this);
                    } else if (member.equals("Teacher")) {
                        new signteacher(fname, lname, gender, email, phone, pass, c, this);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}