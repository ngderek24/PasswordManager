import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;

public class Search extends JFrame {

	private JPanel contentPane;
	private JTextField accNameField;
	Connection connection = null;
	static Search frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Search();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Search() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		connection = sqliteConnection.dbConnector();
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblSearch.setBounds(22, 21, 60, 14);
		contentPane.add(lblSearch);
		
		JButton newRecordBtn = new JButton("New Record");
		newRecordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				Home home = new Home();
				home.setVisible(true);
			}
		});
		newRecordBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		newRecordBtn.setBounds(308, 18, 101, 23);
		contentPane.add(newRecordBtn);
		
		JLabel lblAccName = new JLabel("Enter Account Name to look up log-in information");
		lblAccName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblAccName.setBounds(22, 82, 300, 14);
		contentPane.add(lblAccName);
		
		accNameField = new JTextField();
		accNameField.setText("ex. Google, Facebook, etc.");
		accNameField.setBounds(20, 107, 313, 20);
		contentPane.add(accNameField);
		accNameField.setColumns(10);
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String query = "select * from AccountInfo where account = ?";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setString(1, accNameField.getText());
					
					ResultSet rs = pst.executeQuery();
					int count = 0;
					while(rs.next()){
						//display results
						String username = rs.getString("Username");
						String password = rs.getString("Password");
						JOptionPane.showMessageDialog(null, accNameField.getText() + "\nUsername: " + username + "\nPassword: " + password);
						count++;
					}
					if(count == 0)
						JOptionPane.showMessageDialog(null, "The account name is not in the database. Try again.");
					rs.close();
					pst.close();
				} catch(Exception err){
					JOptionPane.showMessageDialog(null, err);
				}
			}
		});
		searchButton.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		searchButton.setBounds(258, 157, 74, 23);
		contentPane.add(searchButton);
	}
}
