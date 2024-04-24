package bms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener {
    JLabel text, pin, card;
    JTextField cardtextbox;
    JPasswordField pintextbox;
    JButton clear, signup, login;
    conn c;
    Login() {
        this.c = new conn();
        setTitle("Library");
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/logo.png"));

        Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);

        ImageIcon i3 = new ImageIcon(i2);
        JLabel label = new JLabel(i3);
        label.setBounds(70, 15, 100, 100);
        add(label);

        text = new JLabel("Welcome to BITS Library");
        text.setFont(new Font("Osward", Font.BOLD, 38));
        text.setBounds(200, 50, 500, 40);
        add(text);

        card = new JLabel("Email: ");
        card.setFont(new Font("Raleway", Font.BOLD, 28));
        card.setBounds(200, 160, 150, 30);
        add(card);
        cardtextbox = new JTextField();
        cardtextbox.setBounds(350, 160, 200, 28);
        cardtextbox.setFont(new Font("Arial", Font.ITALIC, 20));
        add(cardtextbox);

        pin = new JLabel("Password:");
        pin.setFont(new Font("Raleway", Font.BOLD, 28));
        pin.setBounds(200, 230, 150, 30);
        add(pin);
        pintextbox = new JPasswordField();
        pintextbox.setBounds(350, 230, 200, 28);
        pintextbox.setFont(new Font("Arial", Font.ITALIC, 20));
        add(pintextbox);

        login = new JButton("Sign In");
        login.setBounds(285, 320, 95, 28);
        login.setBackground(Color.black);
        login.setForeground(Color.white);
        login.addActionListener(this);
        add(login);

        clear = new JButton("Clear");
        clear.setBounds(390, 320, 95, 28);
        clear.setBackground(Color.black);
        clear.setForeground(Color.white);
        clear.addActionListener(this);
        add(clear);

        signup = new JButton("Sign Up");
        signup.setBounds(285, 360, 200, 28);
        signup.setBackground(Color.black);
        signup.setForeground(Color.white);
        signup.addActionListener(this);
        add(signup);

        getContentPane().setBackground(Color.white);

        this.setSize(800, 480);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            cardtextbox.setText("");
            pintextbox.setText("");
        } else if (e.getSource() == signup) {
            setVisible(false);
            new signup_member(c).setVisible(true);
        } else if (e.getSource() == login) {
            String s = cardtextbox.getText();
            String p = pintextbox.getText();
            conn c = new conn();
            try (

                    CallableStatement cstmt = c.c.prepareCall("{call signIn(?, ?, ?, ?, ?)}");) {
                cstmt.setString(1, s);
                cstmt.setString(2, p);
                cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
                cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
                cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
                cstmt.execute();
                if(cstmt.getInt(5) == 0) {
                    setVisible(false);
                    if(cstmt.getString(4).equals("Member")) {
                        new member_dashboard(cstmt.getInt(3)).setVisible(true);
                    }
                    else if (cstmt.getString(4).equals("Librarian")) {
                        new librarian_dashboard(cstmt.getInt(3)).setVisible(true);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Incorrect Email or Password");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public static void main(String args[]) {
        Login l = new Login();
    }
}