import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.sql.*;
import javax.swing.JTextField;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.security.Security;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;

public class PasswordManager {

	private JFrame frame;
	Connection connection = null;
	private JTextField accNameField;
	private JTextField nameAccField;
	private JTextField accField;
	private JTextField usernameField;
	private JTextField passwordField;
	private JTextField accountField;
	private JTextField userField;
	private JTextField passField;
	private JTextField randPassField;
	
	// encryption variables
	byte[] keyBytes = "eastog24".getBytes();
	byte[] ivBytes = "tinosaur".getBytes();
	SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
	IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	Cipher cipher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PasswordManager window = new PasswordManager();
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
	public PasswordManager() {
		initialize();
		connection = sqliteConnection.dbConnector();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Password Manager");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 414, 250);
		frame.getContentPane().add(tabbedPane);

		JPanel searchPanel = new JPanel();
		tabbedPane.addTab("Search", null, searchPanel, null);
		searchPanel.setLayout(null);

		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblSearch.setBounds(21, 21, 64, 14);
		searchPanel.add(lblSearch);

		JLabel lblAccName = new JLabel("Enter account name to search");
		lblAccName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblAccName.setBounds(21, 70, 185, 14);
		searchPanel.add(lblAccName);

		accNameField = new JTextField();
		accNameField.setColumns(10);
		accNameField.setBounds(21, 90, 306, 20);
		searchPanel.add(accNameField);

		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// prepare and run query
					String query = "select * from AccountInfo where account = ?";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setString(1, accNameField.getText());

					ResultSet rs = pst.executeQuery();
					int count = 0;
					while (rs.next()) {
						// display results
						String username = rs.getString("Username");
						String password = rs.getString("Password");
						
						// decrypt stored username
						Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
						SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
						IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
						cipher = Cipher.getInstance("DES/CTR/NoPadding", "BC");
						cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
						byte[] plainText = new byte[cipher.getOutputSize(username.length())];
						int ptLength = cipher.update(username.getBytes(), 0, username.length(), plainText, 0);
						ptLength += cipher.doFinal(plainText, ptLength);
						username = new String(plainText);
						
						// decrypt stored password
						plainText = new byte[cipher.getOutputSize(password.length())];
						ptLength = cipher.update(password.getBytes(), 0, password.length(), plainText, 0);
						ptLength += cipher.doFinal(plainText, ptLength);
						password = new String(plainText);
						
						JOptionPane.showMessageDialog(null,
								accNameField.getText() + "\nUsername: " + username + "\nPassword: " + password);
						count++;
					}
					if (count == 0)
						JOptionPane.showMessageDialog(null, "The account name is not in the database. Try again.");
					rs.close();
					pst.close();
				} catch (Exception err) {
					JOptionPane.showMessageDialog(null, err);
				}
			}
		});
		searchBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		searchBtn.setBounds(253, 124, 74, 23);
		searchPanel.add(searchBtn);

		JPanel insertPanel = new JPanel();
		tabbedPane.addTab("Insert", null, insertPanel, null);
		insertPanel.setLayout(null);
		
		JLabel lblInsRecord = new JLabel("Insert Record");
		lblInsRecord.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblInsRecord.setBounds(21, 11, 110, 19);
		insertPanel.add(lblInsRecord);
		
		JLabel lblAcc = new JLabel("Account");
		lblAcc.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblAcc.setBounds(21, 41, 56, 14);
		insertPanel.add(lblAcc);
		
		accField = new JTextField();
		accField.setColumns(10);
		accField.setBounds(21, 60, 306, 20);
		insertPanel.add(accField);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblUsername.setBounds(21, 91, 66, 14);
		insertPanel.add(lblUsername);
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		usernameField.setBounds(21, 110, 306, 20);
		insertPanel.add(usernameField);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblPassword.setBounds(21, 141, 56, 14);
		insertPanel.add(lblPassword);
		
		passwordField = new JTextField();
		passwordField.setColumns(10);
		passwordField.setBounds(21, 160, 306, 20);
		insertPanel.add(passwordField);
		
		JButton addEntryBtn = new JButton("Add Entry");
		addEntryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//add entry to database
				try{
					// encrypt username
					Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
					SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
					IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
					cipher = Cipher.getInstance("DES/CTR/NoPadding", "BC");
					cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
					
					byte[] input = usernameField.getText().getBytes();
					byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
					int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
					ctLength += cipher.doFinal(cipherText, ctLength);
					String encUsername = new String(cipherText);
					
					// encrypt password
					input = passwordField.getText().getBytes();
					cipherText = new byte[cipher.getOutputSize(input.length)];
					ctLength = cipher.update(input, 0, input.length, cipherText, 0);
					ctLength += cipher.doFinal(cipherText, ctLength);
					String encPassword = new String(cipherText);
					
					//prepare query
					String query = "insert into AccountInfo (Account, Username, Password) values (?, ?, ?)";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setString(1, accField.getText());
					pst.setString(2, encUsername);
					pst.setString(3, encPassword);
					
					//run query and close connection
					if(pst.executeUpdate() != 0)
						JOptionPane.showMessageDialog(null, "Entry has been successfully added");
					else
						JOptionPane.showMessageDialog(null, "Entry has been unsuccessfully added. Try again.");
					pst.close();
				} catch(Exception err){
					JOptionPane.showMessageDialog(null, err);
				}
			}
		});
		addEntryBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		addEntryBtn.setBounds(227, 191, 100, 23);
		insertPanel.add(addEntryBtn);

		JPanel updatePanel = new JPanel();
		tabbedPane.addTab("Update", null, updatePanel, null);
		updatePanel.setLayout(null);
		
		JLabel lblUpdateRecord = new JLabel("Update Record");
		lblUpdateRecord.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblUpdateRecord.setBounds(21, 11, 123, 19);
		updatePanel.add(lblUpdateRecord);
		
		JLabel labelAcc = new JLabel("Account");
		labelAcc.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		labelAcc.setBounds(21, 41, 56, 14);
		updatePanel.add(labelAcc);
		
		accountField = new JTextField();
		accountField.setColumns(10);
		accountField.setBounds(21, 60, 306, 20);
		updatePanel.add(accountField);
		
		JLabel labelUsername = new JLabel("Username");
		labelUsername.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		labelUsername.setBounds(21, 91, 66, 14);
		updatePanel.add(labelUsername);
		
		userField = new JTextField();
		userField.setColumns(10);
		userField.setBounds(21, 110, 306, 20);
		updatePanel.add(userField);
		
		JLabel labelPassword = new JLabel("Password");
		labelPassword.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		labelPassword.setBounds(21, 141, 56, 14);
		updatePanel.add(labelPassword);
		
		passField = new JTextField();
		passField.setColumns(10);
		passField.setBounds(21, 160, 306, 20);
		updatePanel.add(passField);
		
		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//prepare query
					String query = "update AccountInfo set Username='"+userField.getText()+"', Password='"+passField.getText()
									+"' where Account='"+accountField.getText()+"'";
					PreparedStatement pst = connection.prepareStatement(query);
					
					//run query and close connection
					if(pst.executeUpdate() != 0)
						JOptionPane.showMessageDialog(null, "Entry has been successfully updated");
					else
						JOptionPane.showMessageDialog(null, "Entry has been unsuccessfully updated. Try again.");
					pst.close();
				} catch(Exception err){
					JOptionPane.showMessageDialog(null, err);
				}
			}
		});
		updateBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		updateBtn.setBounds(249, 191, 78, 23);
		updatePanel.add(updateBtn);

		JPanel deletePanel = new JPanel();
		tabbedPane.addTab("Delete", null, deletePanel, null);
		deletePanel.setLayout(null);

		JLabel lblDelete = new JLabel("Delete Record");
		lblDelete.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblDelete.setBounds(21, 21, 117, 14);
		deletePanel.add(lblDelete);

		JLabel lblNameAcc = new JLabel("Enter account name to delete");
		lblNameAcc.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblNameAcc.setBounds(21, 70, 185, 14);
		deletePanel.add(lblNameAcc);

		nameAccField = new JTextField();
		nameAccField.setColumns(10);
		nameAccField.setBounds(21, 90, 306, 20);
		deletePanel.add(nameAccField);

		JButton deleteBtn = new JButton("Delete");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(null,
						"Do you really want to delete " + nameAccField.getText() + " account?", "Delete", JOptionPane.YES_NO_OPTION);
				if (action == 0) {
					try {
						// prepare query
						String query = "delete from AccountInfo where Account='" + nameAccField.getText() + "'";
						PreparedStatement pst = connection.prepareStatement(query);

						// run query and close connection
						if (pst.executeUpdate() != 0)
							JOptionPane.showMessageDialog(null, "Entry has been successfully deleted");
						else
							JOptionPane.showMessageDialog(null, "Entry has been unsuccessfully deleted. Try again.");
						pst.close();
					} catch (Exception err) {
						JOptionPane.showMessageDialog(null, err);
					}
				}
			}
		});
		deleteBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		deleteBtn.setBounds(253, 124, 74, 23);
		deletePanel.add(deleteBtn);

		JPanel passGenPanel = new JPanel();
		tabbedPane.addTab("Password Generator", null, passGenPanel, null);
		passGenPanel.setLayout(null);
		
		JLabel lblPassGen = new JLabel("Password Generator");
		lblPassGen.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblPassGen.setBounds(21, 21, 161, 19);
		passGenPanel.add(lblPassGen);
		
		JLabel lblPassLen = new JLabel("Length of Password (4-40)");
		lblPassLen.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblPassLen.setBounds(21, 70, 179, 16);
		passGenPanel.add(lblPassLen);
		
		JSpinner passLenSpinner = new JSpinner();
		passLenSpinner.setModel(new SpinnerNumberModel(4, 4, 40, 1));
		passLenSpinner.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		passLenSpinner.setBounds(21, 97, 69, 20);
		passGenPanel.add(passLenSpinner);
		
		JRadioButton rdbtnAllChar = new JRadioButton("All characters");
		rdbtnAllChar.setSelected(true);
		rdbtnAllChar.setBounds(96, 93, 109, 23);
		passGenPanel.add(rdbtnAllChar);
		
		JRadioButton rdbtnLetters_Num = new JRadioButton("Letters/Numbers");
		rdbtnLetters_Num.setBounds(96, 117, 120, 23);
		passGenPanel.add(rdbtnLetters_Num);
		
		ButtonGroup rdbtnGroup = new ButtonGroup();
		rdbtnGroup.add(rdbtnLetters_Num);
		rdbtnGroup.add(rdbtnAllChar);
		
		JLabel lblRandPass = new JLabel("Random Password");
		lblRandPass.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblRandPass.setBounds(21, 160, 120, 14);
		passGenPanel.add(lblRandPass);
		
		randPassField = new JTextField();
		randPassField.setBounds(21, 179, 304, 20);
		passGenPanel.add(randPassField);
		randPassField.setColumns(10);
		
		JButton generateBtn = new JButton("Generate");
		generateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//generate random password according to given length
				Random rand = new Random();
				String randPassword = "";
				if(rdbtnAllChar.isSelected()){
					for(int i = 0; i < (Integer)passLenSpinner.getValue(); i++){
						randPassword += (char)(rand.nextInt(90) + '!');
					}
				}
				else if(rdbtnLetters_Num.isSelected()){
					String letters_num = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
					for(int i = 0; i < (Integer)passLenSpinner.getValue(); i++){
						randPassword += letters_num.charAt(rand.nextInt(letters_num.length()));
					}
				}
				randPassField.setText(randPassword);
			}
		});
		generateBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		generateBtn.setBounds(235, 96, 90, 23);
		passGenPanel.add(generateBtn);
	}
}
