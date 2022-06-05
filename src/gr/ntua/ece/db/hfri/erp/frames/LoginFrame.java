package gr.ntua.ece.db.hfri.erp.frames;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.sql.SQLException;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.JSchException;

import gr.ntua.ece.db.hfri.ERP;
import gr.ntua.ece.db.hfri.erp.Settings;
import gr.ntua.ece.db.hfri.erp.Database;
import gr.ntua.ece.db.hfri.erp.SSHTunnel;

public class LoginFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	public LoginFrame() {
		initialize();
	}
	
	private void initialize() {
		setTitle("Login");
		setIconImage(ERP.getApplicationIcon());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout contentPaneLayout = new GridBagLayout();
		contentPane.setLayout(contentPaneLayout);
		setContentPane(contentPane);
		
		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(getClass().getResource("/img/logo.png")));
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints logoConstraints = new GridBagConstraints();
		logoConstraints.insets = new Insets(20, 0, 30, 20);
		logoConstraints.gridx = 0;
		logoConstraints.gridy = 0;
		logoConstraints.gridwidth = 4;
		contentPane.add(logo, logoConstraints);
		
		JLabel usernameLabel = new JLabel("Username:");
		GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
		usernameLabelConstraints.insets = new Insets(0, 0, 5, 5);
		usernameLabelConstraints.gridx = 1;
		usernameLabelConstraints.gridy = 1;
		usernameLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(usernameLabel, usernameLabelConstraints);
		
		usernameField = new JTextField(Database.getLastLoginUsername());
		usernameField.setColumns(16);
		GridBagConstraints usernameFieldConstraints = new GridBagConstraints();
		usernameFieldConstraints.insets = new Insets(0, 5, 5, 0);
		usernameFieldConstraints.gridx = 2;
		usernameFieldConstraints.gridy = 1;
		contentPane.add(usernameField, usernameFieldConstraints);
		
		JLabel passwordLabel = new JLabel("Password:");
		GridBagConstraints passwordLabelConstraints = new GridBagConstraints();
		passwordLabelConstraints.insets = new Insets(0, 0, 10, 5);
		passwordLabelConstraints.gridx = 1;
		passwordLabelConstraints.gridy = 2;
		passwordLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(passwordLabel, passwordLabelConstraints);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(16);
		GridBagConstraints passwordFieldConstraints = new GridBagConstraints();
		passwordFieldConstraints.insets = new Insets(0, 5, 10, 0);
		passwordFieldConstraints.gridx = 2;
		passwordFieldConstraints.gridy = 2;
		contentPane.add(passwordField, passwordFieldConstraints);
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		GridBagConstraints loginButtonConstraints = new GridBagConstraints();
		loginButtonConstraints.insets = new Insets(10, 0, 0, 5);
		loginButtonConstraints.gridx = 0;
		loginButtonConstraints.gridy = 3;
		contentPane.add(loginButton, loginButtonConstraints);
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.addActionListener(this);
		GridBagConstraints settingsButtonConstraints = new GridBagConstraints();
		settingsButtonConstraints.insets = new Insets(10, 5, 0, 0);
		settingsButtonConstraints.gridx = 3;
		settingsButtonConstraints.gridy = 3;
		settingsButtonConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(settingsButton, settingsButtonConstraints);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		
		if(command.equals("Login")) {
			try {
				Database.configureServerConnection(usernameField.getText(), String.valueOf(passwordField.getPassword()));
				
				SSHTunnel sshTunnel = Database.getSshTunnel();
				if(sshTunnel != null) {
					HostKey hostKey = sshTunnel.getHostKey();
					
					if(!sshTunnel.knownHostsContains(hostKey)) {
						ERP.beep();
						
						boolean saveHostKey = JOptionPane.showConfirmDialog(this,
							"<html>" +
							"Your connection to the database is tunneled through an SSH server.<br>" +
							"The server has provided a " + hostKey.getType() + " host key that is not known by the program.<br><br>" +
							"Key Fingerprint:<br>"+ hostKey.getFingerPrint(null) + "<br><br>" +
							"Do you wish to save this key to the designated known hosts file?<br>" +
							"Doing so will allow you to enable Strict Host Key Checking, which is HIGHLY RECCOMENDED for security.<br>" +
							"Otherwise, you have no guarantee that the SSH server is the computer you think it is.<br><br>" +
							"<span style=\"color: red\">Warning</span>: Select YES only if you trust this host and your connection to it.<br>" +
							"</html>",
							"Remeber SSH server host key?",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0 ? true : false;
						
						if(saveHostKey) sshTunnel.addToKnownHosts(hostKey);
					}
				}
				
				if(Database.verifyConnection()) {
					Settings.set("database.last-login-username", usernameField.getText());
					try { Settings.save(); } catch(IOException ex) {}
					
					dispose();
					
					Dashboard dashboard = new Dashboard();
					dashboard.setVisible(true);
				}
			} catch(IOException | SQLException | JSchException ex) {
				if(ex instanceof SQLException || ex instanceof JSchException) {
					if(Database.getSshTunnel() != null && Database.getSshTunnel().isConnected()) {
						try {
							Database.getSshTunnel().removeAllLocalPortForwarding();
						} catch(JSchException e) {}
						
						Database.getSshTunnel().disconnect();
					}
				}
				
				JOptionPane.showMessageDialog(this, (ex instanceof JSchException ? "SSH: " : "") + ex.getLocalizedMessage().replaceAll("\\. ", ".\n"), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command.equals("Settings")) {
			SettingsFrame settingsFrame = new SettingsFrame();
			
			settingsFrame.setVisible(true);
		}
    }
	
}