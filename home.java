import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Home {

	private JFrame frame;
	private JTextField accountField;
	private JTextField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Entry has been added");
			}
		});
		addButton.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		addButton.setBounds(234, 211, 89, 23);
		frame.getContentPane().add(addButton);
		
		JLabel lblNewRecord = new JLabel("New Record");
		lblNewRecord.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblNewRecord.setBounds(22, 11, 100, 30);
		frame.getContentPane().add(lblNewRecord);
		
		JLabel lblAccount = new JLabel("Account");
		lblAccount.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblAccount.setBounds(32, 67, 56, 14);
		frame.getContentPane().add(lblAccount);
		
		accountField = new JTextField();
		accountField.setText("ex. Facebook, Google");
		accountField.setBounds(29, 94, 294, 20);
		frame.getContentPane().add(accountField);
		accountField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblPassword.setBounds(32, 139, 56, 14);
		frame.getContentPane().add(lblPassword);
		
		passwordField = new JTextField();
		passwordField.setText("Enter the password you would like to save");
		passwordField.setBounds(29, 163, 294, 20);
		frame.getContentPane().add(passwordField);
		passwordField.setColumns(10);
	}
}
