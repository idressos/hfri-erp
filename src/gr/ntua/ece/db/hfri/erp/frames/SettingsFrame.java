package gr.ntua.ece.db.hfri.erp.frames;

import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;

import javax.swing.border.EmptyBorder;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gr.ntua.ece.db.hfri.ERP;
import gr.ntua.ece.db.hfri.erp.Settings;

public class SettingsFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> databaseTypeSelector;
	private JTextField databaseHostField;
	private JSpinner databasePortSpinner;
	private JTextField databaseSchemaField;
	private JCheckBox databaseAllowPublicKeyRetrievalCheckBox;
	private JCheckBox sshTunnelCheckBox;
	private JTextField sshHostField;
	private JSpinner sshPortSpinner;
	private JTextField sshUserField;
	private JPasswordField sshPasswordField;
	private JCheckBox sshStrictHostKeyCheckingCheckBox;
	private JTextField sshKnownHostsFileField;
	private JButton openSshKnownHostsFileButton;
	private JCheckBox sshRsaAuthCheckBox;
	private JTextField sshRsaKeyFileField;
	private JButton openSshRsaKeyFileButton;
	private JPasswordField sshRsaPassphraseField;
	private JCheckBox randomAvatarsCheckBox;
	private JCheckBox clientSideFilteringCheckBox;
	
	public SettingsFrame() {
		initialize();
		
		parseConfig();
		refresh();
	}
	
	private void initialize() {
		setTitle("Settings");
		setIconImage(ERP.getApplicationIcon());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout contentPaneLayout = new GridBagLayout();
		contentPane.setLayout(contentPaneLayout);
		setContentPane(contentPane);
		
		JLabel databaseSectionLabel = new JLabel("<html><h3>Database</h3></html>");
		GridBagConstraints databaseSectionLabelConstraints = new GridBagConstraints();
		databaseSectionLabelConstraints.insets = new Insets(0, 0, 5, 5);
		databaseSectionLabelConstraints.gridx = 0;
		databaseSectionLabelConstraints.gridy = 0;
		databaseSectionLabelConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(databaseSectionLabel, databaseSectionLabelConstraints);
		
		JLabel databaseTypeLabel = new JLabel("Type");
		GridBagConstraints databaseTypeLabelConstraints = new GridBagConstraints();
		databaseTypeLabelConstraints.insets = new Insets(0, 0, 5, 5);
		databaseTypeLabelConstraints.gridx = 0;
		databaseTypeLabelConstraints.gridy = 1;
		databaseTypeLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(databaseTypeLabel, databaseTypeLabelConstraints);
		
		databaseTypeSelector = new JComboBox<String>();
		databaseTypeSelector.setModel(new DefaultComboBoxModel<String>(new String[] {"MySQL", "MariaDB"}));
		GridBagConstraints databaseTypeSelectorConstraints = new GridBagConstraints();
		databaseTypeSelectorConstraints.insets = new Insets(0, 0, 5, 0);
		databaseTypeSelectorConstraints.gridx = 1;
		databaseTypeSelectorConstraints.gridy = 1;
		databaseTypeSelectorConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(databaseTypeSelector, databaseTypeSelectorConstraints);
		
		JLabel databaseHostLabel = new JLabel("Host");
		GridBagConstraints databaseHostLabelConstraints = new GridBagConstraints();
		databaseHostLabelConstraints.insets = new Insets(0, 0, 5, 5);
		databaseHostLabelConstraints.gridx = 0;
		databaseHostLabelConstraints.gridy = 2;
		databaseHostLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(databaseHostLabel, databaseHostLabelConstraints);
		
		databaseHostField = new JTextField();
		databaseHostField.setColumns(16);
		GridBagConstraints databaseHostFieldConstraints = new GridBagConstraints();
		databaseHostFieldConstraints.insets = new Insets(0, 0, 5, 0);
		databaseHostFieldConstraints.gridx = 1;
		databaseHostFieldConstraints.gridy = 2;
		databaseHostFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(databaseHostField, databaseHostFieldConstraints);
		
		JLabel serverPortLabel = new JLabel("Port");
		GridBagConstraints serverPortLabelConstraints = new GridBagConstraints();
		serverPortLabelConstraints.insets = new Insets(0, 0, 5, 5);
		serverPortLabelConstraints.gridx = 0;
		serverPortLabelConstraints.gridy = 3;
		serverPortLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(serverPortLabel, serverPortLabelConstraints);
		
		databasePortSpinner = new JSpinner();
		databasePortSpinner.setModel(new SpinnerNumberModel(22, 1, 65535, 1));
		databasePortSpinner.setEditor(new JSpinner.NumberEditor(databasePortSpinner, "#"));
		GridBagConstraints databasePortSpinnerConstraints = new GridBagConstraints();
		databasePortSpinnerConstraints.insets = new Insets(0, 0, 5, 0);
		databasePortSpinnerConstraints.gridx = 1;
		databasePortSpinnerConstraints.gridy = 3;
		databasePortSpinnerConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(databasePortSpinner, databasePortSpinnerConstraints);
		
		JLabel databaseSchemaLabel = new JLabel("Schema");
		GridBagConstraints databaseSchemaLabelConstraints = new GridBagConstraints();
		databaseSchemaLabelConstraints.insets = new Insets(0, 0, 5, 5);
		databaseSchemaLabelConstraints.gridx = 0;
		databaseSchemaLabelConstraints.gridy = 4;
		databaseSchemaLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(databaseSchemaLabel, databaseSchemaLabelConstraints);
		
		databaseSchemaField = new JTextField();
		databaseSchemaField.setColumns(16);
		GridBagConstraints databaseSchemaFieldConstraints = new GridBagConstraints();
		databaseSchemaFieldConstraints.insets = new Insets(0, 0, 5, 0);
		databaseSchemaFieldConstraints.gridx = 1;
		databaseSchemaFieldConstraints.gridy = 4;
		databaseSchemaFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(databaseSchemaField, databaseSchemaFieldConstraints);
		
		JLabel databaseAllowPublicKeyRetrievalLabel = new JLabel("Allow Public Key Retrieval");
		GridBagConstraints databaseAllowPublicKeyRetrievalLabelConstraints = new GridBagConstraints();
		databaseAllowPublicKeyRetrievalLabelConstraints.insets = new Insets(0, 0, 5, 5);
		databaseAllowPublicKeyRetrievalLabelConstraints.gridx = 3;
		databaseAllowPublicKeyRetrievalLabelConstraints.gridy = 1;
		databaseAllowPublicKeyRetrievalLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(databaseAllowPublicKeyRetrievalLabel, databaseAllowPublicKeyRetrievalLabelConstraints);
		
		databaseAllowPublicKeyRetrievalCheckBox = new JCheckBox();
		GridBagConstraints databaseAllowPublicKeyRetrievalCheckBoxConstraints = new GridBagConstraints();
		databaseAllowPublicKeyRetrievalCheckBoxConstraints.insets = new Insets(0, 0, 5, 0);
		databaseAllowPublicKeyRetrievalCheckBoxConstraints.gridx = 4;
		databaseAllowPublicKeyRetrievalCheckBoxConstraints.gridy = 1;
		databaseAllowPublicKeyRetrievalCheckBoxConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(databaseAllowPublicKeyRetrievalCheckBox, databaseAllowPublicKeyRetrievalCheckBoxConstraints);
		
		JLabel sshSectionLabel = new JLabel("<html><h3>SSH</h3></html>");
		GridBagConstraints sshSectionLabelConstraints = new GridBagConstraints();
		sshSectionLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshSectionLabelConstraints.gridx = 0;
		sshSectionLabelConstraints.gridy = 5;
		sshSectionLabelConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshSectionLabel, sshSectionLabelConstraints);
		
		JLabel sshTunnelLabel = new JLabel("Tunnel");
		GridBagConstraints sshTunnelLabelConstraints = new GridBagConstraints();
		sshTunnelLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshTunnelLabelConstraints.gridx = 0;
		sshTunnelLabelConstraints.gridy = 6;
		sshTunnelLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshTunnelLabel, sshTunnelLabelConstraints);
		
		sshTunnelCheckBox = new JCheckBox("");
		sshTunnelCheckBox.setActionCommand("SSH");
		sshTunnelCheckBox.addActionListener(this);
		GridBagConstraints sshTunnelCheckBoxConstraints = new GridBagConstraints();
		sshTunnelCheckBoxConstraints.insets = new Insets(0, 0, 5, 0);
		sshTunnelCheckBoxConstraints.gridx = 1;
		sshTunnelCheckBoxConstraints.gridy = 6;
		sshTunnelCheckBoxConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshTunnelCheckBox, sshTunnelCheckBoxConstraints);
		
		JLabel sshHostLabel = new JLabel("Host");
		GridBagConstraints sshHostLabelConstraints = new GridBagConstraints();
		sshHostLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshHostLabelConstraints.gridx = 0;
		sshHostLabelConstraints.gridy = 7;
		sshHostLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshHostLabel, sshHostLabelConstraints);
		
		sshHostField = new JTextField();
		sshHostField.setColumns(16);
		GridBagConstraints sshHostFieldConstraints = new GridBagConstraints();
		sshHostFieldConstraints.insets = new Insets(0, 0, 5, 0);
		sshHostFieldConstraints.gridx = 1;
		sshHostFieldConstraints.gridy = 7;
		sshHostFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshHostField, sshHostFieldConstraints);
		
		JLabel sshPortLabel = new JLabel("Port");
		GridBagConstraints sshPortLabelConstraints = new GridBagConstraints();
		sshPortLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshPortLabelConstraints.gridx = 0;
		sshPortLabelConstraints.gridy = 8;
		sshPortLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshPortLabel, sshPortLabelConstraints);
		
		sshPortSpinner = new JSpinner();
		sshPortSpinner.setModel(new SpinnerNumberModel(22, 1, 65535, 1));
		sshPortSpinner.setEditor(new JSpinner.NumberEditor(sshPortSpinner, "#"));
		GridBagConstraints sshPortSpinnerConstraints = new GridBagConstraints();
		sshPortSpinnerConstraints.insets = new Insets(0, 0, 5, 0);
		sshPortSpinnerConstraints.gridx = 1;
		sshPortSpinnerConstraints.gridy = 8;
		sshPortSpinnerConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshPortSpinner, sshPortSpinnerConstraints);
		
		JLabel sshUserLabel = new JLabel("User");
		GridBagConstraints sshUserLabelConstraints = new GridBagConstraints();
		sshUserLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshUserLabelConstraints.gridx = 0;
		sshUserLabelConstraints.gridy = 9;
		sshUserLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshUserLabel, sshUserLabelConstraints);
		
		sshUserField = new JTextField();
		sshUserField.setColumns(16);
		GridBagConstraints sshUserFieldConstraints = new GridBagConstraints();
		sshUserFieldConstraints.insets = new Insets(0, 0, 5, 0);
		sshUserFieldConstraints.gridx = 1;
		sshUserFieldConstraints.gridy = 9;
		sshUserFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshUserField, sshUserFieldConstraints);
		
		JLabel sshPasswordLabel = new JLabel("Password");
		GridBagConstraints sshPasswordLabelConstraints = new GridBagConstraints();
		sshPasswordLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshPasswordLabelConstraints.gridx = 0;
		sshPasswordLabelConstraints.gridy = 10;
		sshPasswordLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshPasswordLabel, sshPasswordLabelConstraints);
		
		sshPasswordField = new JPasswordField();
		sshPasswordField.setColumns(16);
		GridBagConstraints sshPasswordFieldConstraints = new GridBagConstraints();
		sshPasswordFieldConstraints.insets = new Insets(0, 0, 5, 0);
		sshPasswordFieldConstraints.gridx = 1;
		sshPasswordFieldConstraints.gridy = 10;
		sshPasswordFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshPasswordField, sshPasswordFieldConstraints);
		
		JLabel sshStrictHostKeyCheckingLabel = new JLabel("Strict Host Key Checking");
		GridBagConstraints sshStrictHostKeyCheckingLabelConstraints = new GridBagConstraints();
		sshStrictHostKeyCheckingLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshStrictHostKeyCheckingLabelConstraints.gridx = 3;
		sshStrictHostKeyCheckingLabelConstraints.gridy = 6;
		sshStrictHostKeyCheckingLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshStrictHostKeyCheckingLabel, sshStrictHostKeyCheckingLabelConstraints);
		
		sshStrictHostKeyCheckingCheckBox = new JCheckBox();
		sshStrictHostKeyCheckingCheckBox.setActionCommand("SHKC");
		sshStrictHostKeyCheckingCheckBox.addActionListener(this);
		GridBagConstraints sshStrictHostKeyCheckingCheckBoxConstraints = new GridBagConstraints();
		sshStrictHostKeyCheckingCheckBoxConstraints.insets = new Insets(0, 0, 5, 0);
		sshStrictHostKeyCheckingCheckBoxConstraints.gridx = 4;
		sshStrictHostKeyCheckingCheckBoxConstraints.gridy = 6;
		sshStrictHostKeyCheckingCheckBoxConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshStrictHostKeyCheckingCheckBox, sshStrictHostKeyCheckingCheckBoxConstraints);
		
		JLabel sshKnownHostsFileLabel = new JLabel("Known Hosts File");
		GridBagConstraints sshKnownHostsFileLabelConstraints = new GridBagConstraints();
		sshKnownHostsFileLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshKnownHostsFileLabelConstraints.gridx = 3;
		sshKnownHostsFileLabelConstraints.gridy = 7;
		sshKnownHostsFileLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshKnownHostsFileLabel, sshKnownHostsFileLabelConstraints);
		
		sshKnownHostsFileField = new JTextField();
		sshKnownHostsFileField.setColumns(16);
		GridBagConstraints sshKnownHostsFileFieldConstraints = new GridBagConstraints();
		sshKnownHostsFileFieldConstraints.insets = new Insets(0, 0, 5, 0);
		sshKnownHostsFileFieldConstraints.gridx = 4;
		sshKnownHostsFileFieldConstraints.gridy = 7;
		sshKnownHostsFileFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshKnownHostsFileField, sshKnownHostsFileFieldConstraints);
		
		openSshKnownHostsFileButton = new JButton("Open..");
		openSshKnownHostsFileButton.setPreferredSize(new Dimension((int) openSshKnownHostsFileButton.getPreferredSize().getWidth(), (int) sshKnownHostsFileField.getPreferredSize().getHeight()));
		openSshKnownHostsFileButton.setActionCommand("OKHF");
		openSshKnownHostsFileButton.addActionListener(this);
		GridBagConstraints openSshKnownHostsFileButtonConstraints = new GridBagConstraints();
		openSshKnownHostsFileButtonConstraints.insets = new Insets(0, 5, 5, 0);
		openSshKnownHostsFileButtonConstraints.gridx = 5;
		openSshKnownHostsFileButtonConstraints.gridy = 7;
		openSshKnownHostsFileButtonConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(openSshKnownHostsFileButton, openSshKnownHostsFileButtonConstraints);
		
		JLabel sshRsaAuthLabel = new JLabel("RSA Authentication");
		GridBagConstraints sshRsaAuthLabelConstraints = new GridBagConstraints();
		sshRsaAuthLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshRsaAuthLabelConstraints.gridx = 3;
		sshRsaAuthLabelConstraints.gridy = 8;
		sshRsaAuthLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshRsaAuthLabel, sshRsaAuthLabelConstraints);
		
		sshRsaAuthCheckBox = new JCheckBox("");
		sshRsaAuthCheckBox.setActionCommand("RSA");
		sshRsaAuthCheckBox.addActionListener(this);
		GridBagConstraints sshRsaAuthCheckBoxConstraints = new GridBagConstraints();
		sshRsaAuthCheckBoxConstraints.insets = new Insets(0, 0, 5, 0);
		sshRsaAuthCheckBoxConstraints.gridx = 4;
		sshRsaAuthCheckBoxConstraints.gridy = 8;
		sshRsaAuthCheckBoxConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshRsaAuthCheckBox, sshRsaAuthCheckBoxConstraints);
		
		JLabel sshRsaKeyFileLabel = new JLabel("Key File");
		GridBagConstraints sshRsaKeyFileLabelConstraints = new GridBagConstraints();
		sshRsaKeyFileLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshRsaKeyFileLabelConstraints.gridx = 3;
		sshRsaKeyFileLabelConstraints.gridy = 9;
		sshRsaKeyFileLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshRsaKeyFileLabel, sshRsaKeyFileLabelConstraints);
		
		sshRsaKeyFileField = new JTextField();
		sshRsaKeyFileField.setColumns(16);
		GridBagConstraints sshRsaKeyFileFieldConstraints = new GridBagConstraints();
		sshRsaKeyFileFieldConstraints.insets = new Insets(0, 0, 5, 0);
		sshRsaKeyFileFieldConstraints.gridx = 4;
		sshRsaKeyFileFieldConstraints.gridy = 9;
		sshRsaKeyFileFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshRsaKeyFileField, sshRsaKeyFileFieldConstraints);
		
		openSshRsaKeyFileButton = new JButton("Open..");
		openSshRsaKeyFileButton.setPreferredSize(new Dimension((int) openSshRsaKeyFileButton.getPreferredSize().getWidth(), (int) sshRsaKeyFileField.getPreferredSize().getHeight()));
		openSshRsaKeyFileButton.setActionCommand("ORKF");
		openSshRsaKeyFileButton.addActionListener(this);
		GridBagConstraints openSshRsaKeyFileButtonConstraints = new GridBagConstraints();
		openSshRsaKeyFileButtonConstraints.insets = new Insets(0, 5, 5, 0);
		openSshRsaKeyFileButtonConstraints.gridx = 5;
		openSshRsaKeyFileButtonConstraints.gridy = 9;
		openSshRsaKeyFileButtonConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(openSshRsaKeyFileButton, openSshRsaKeyFileButtonConstraints);
		
		JLabel sshTunnelRsaPassphraseLabel = new JLabel("Passphrase");
		GridBagConstraints sshTunnelRsaPassphraseLabelConstraints = new GridBagConstraints();
		sshTunnelRsaPassphraseLabelConstraints.insets = new Insets(0, 0, 5, 5);
		sshTunnelRsaPassphraseLabelConstraints.gridx = 3;
		sshTunnelRsaPassphraseLabelConstraints.gridy = 10;
		sshTunnelRsaPassphraseLabelConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(sshTunnelRsaPassphraseLabel, sshTunnelRsaPassphraseLabelConstraints);
		
		sshRsaPassphraseField = new JPasswordField();
		sshRsaPassphraseField.setColumns(16);
		GridBagConstraints sshRsaPassphraseFieldConstraints = new GridBagConstraints();
		sshRsaPassphraseFieldConstraints.insets = new Insets(0, 0, 5, 0);
		sshRsaPassphraseFieldConstraints.gridx = 4;
		sshRsaPassphraseFieldConstraints.gridy = 10;
		sshRsaPassphraseFieldConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(sshRsaPassphraseField, sshRsaPassphraseFieldConstraints);
		
		JLabel miscellaneousSectionLabel = new JLabel("<html><h3>Miscellaneous</h3></html>");
		GridBagConstraints miscellaneousSectionLabelConstraints = new GridBagConstraints();
		miscellaneousSectionLabelConstraints.insets = new Insets(5, 0, 5, 5);
		miscellaneousSectionLabelConstraints.gridx = 0;
		miscellaneousSectionLabelConstraints.gridy = 11;
		miscellaneousSectionLabelConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(miscellaneousSectionLabel, miscellaneousSectionLabelConstraints);
		
		randomAvatarsCheckBox = new JCheckBox("Random Avatars");
		randomAvatarsCheckBox.setToolTipText("<html>Fetch random avatars for research workers from <a href=\"https:/randomuser.me/\">randomuser.me</a> on every refresh.<br>Will slow down performance significantly due to network overhead.</html>");
		GridBagConstraints randomAvatarsCheckBoxConstraints = new GridBagConstraints();
		randomAvatarsCheckBoxConstraints.insets = new Insets(0, 0, 5, 0);
		randomAvatarsCheckBoxConstraints.gridx = 0;
		randomAvatarsCheckBoxConstraints.gridy = 12;
		randomAvatarsCheckBoxConstraints.gridwidth = 2;
		randomAvatarsCheckBoxConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(randomAvatarsCheckBox, randomAvatarsCheckBoxConstraints);
		
		clientSideFilteringCheckBox = new JCheckBox("Client-Side Filtering");
		clientSideFilteringCheckBox.setToolTipText("<html>Filter SQL query results on the client-side.<br>When disabled, filtering is done by using the WHERE clause in SQL queries.<br>Decreases response time when filtering by not refreshing the results.<br>Useful for reducing database connections on a development environment.</html>");
		GridBagConstraints clientSideFilteringCheckBoxConstraints = new GridBagConstraints();
		clientSideFilteringCheckBoxConstraints.insets = new Insets(0, 0, 5, 0);
		clientSideFilteringCheckBoxConstraints.gridx = 0;
		clientSideFilteringCheckBoxConstraints.gridy = 13;
		clientSideFilteringCheckBoxConstraints.anchor = GridBagConstraints.WEST;
		contentPane.add(clientSideFilteringCheckBox, clientSideFilteringCheckBoxConstraints);
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		GridBagConstraints saveButtonConstraints = new GridBagConstraints();
		saveButtonConstraints.insets = new Insets(10, 0, 0, 0);
		saveButtonConstraints.gridx = 2;
		saveButtonConstraints.gridy = 14;
		saveButtonConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(saveButton, saveButtonConstraints);
		
		JButton discardButton = new JButton("Discard");
		discardButton.addActionListener(this);
		GridBagConstraints discardButtonConstraints = new GridBagConstraints();
		discardButtonConstraints.insets = new Insets(10, 0, 0, 0);
		discardButtonConstraints.gridx = 3;
		discardButtonConstraints.gridy = 14;
		discardButtonConstraints.anchor = GridBagConstraints.EAST;
		contentPane.add(discardButton, discardButtonConstraints);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void parseConfig() {
		databaseTypeSelector.setSelectedIndex(Settings.getString("database.type").equalsIgnoreCase("MYSQL") ? 0 : 1);
		databaseHostField.setText(Settings.getString("database.host"));
		databasePortSpinner.setValue(Settings.getInt("database.port"));
		databaseSchemaField.setText(Settings.getString("database.schema"));
		databaseAllowPublicKeyRetrievalCheckBox.setSelected(Settings.getBoolean("database.allow-public-key-retrieval"));
		
		sshTunnelCheckBox.setSelected(Settings.getBoolean("ssh.tunnel"));
		sshHostField.setText(Settings.getString("ssh.host"));
		sshPortSpinner.setValue(Settings.getInt("ssh.port"));
		sshUserField.setText(Settings.getString("ssh.user"));
		sshPasswordField.setText(Settings.getString("ssh.password"));
		
		sshStrictHostKeyCheckingCheckBox.setSelected(Settings.getBoolean("ssh.strict-host-key-checking.enabled"));
		sshKnownHostsFileField.setText(Settings.getString("ssh.strict-host-key-checking.known-hosts-file"));
		
		sshRsaAuthCheckBox.setSelected(Settings.getBoolean("ssh.rsa-authentication.enabled"));
		sshRsaKeyFileField.setText(Settings.getString("ssh.rsa-authentication.key-file"));
		sshRsaPassphraseField.setText(Settings.getString("ssh.rsa-authentication.passphrase"));
		
		randomAvatarsCheckBox.setSelected(Settings.getBoolean("miscellaneous.random-avatars"));
		clientSideFilteringCheckBox.setSelected(Settings.getBoolean("miscellaneous.client-side-filtering"));
	}
	
	private void saveToConfig() throws IOException {
		Settings.set("database.type", databaseTypeSelector.getSelectedIndex() == 0 ? "mysql" : "mariadb");
		Settings.set("database.host", databaseHostField.getText());
		Settings.set("database.port", (int) databasePortSpinner.getValue());
		Settings.set("database.schema", databaseSchemaField.getText());
		Settings.set("database.allow-public-key-retrieval", databaseAllowPublicKeyRetrievalCheckBox.isSelected());
		
		Settings.set("ssh.tunnel", sshTunnelCheckBox.isSelected());
		Settings.set("ssh.host", sshHostField.getText());
		Settings.set("ssh.port", (int) sshPortSpinner.getValue());
		Settings.set("ssh.user", sshUserField.getText());
		Settings.set("ssh.password", String.valueOf(sshPasswordField.getPassword()));
		
		Settings.set("ssh.strict-host-key-checking.enabled", sshStrictHostKeyCheckingCheckBox.isSelected());
		Settings.set("ssh.strict-host-key-checking.known-hosts-file", sshKnownHostsFileField.getText());
		
		Settings.set("ssh.rsa-authentication.enabled", sshRsaAuthCheckBox.isSelected());
		Settings.set("ssh.rsa-authentication.key-file", sshRsaKeyFileField.getText());
		Settings.set("ssh.rsa-authentication.passphrase", String.valueOf(sshRsaPassphraseField.getPassword()));
		
		Settings.set("miscellaneous.random-avatars", randomAvatarsCheckBox.isSelected());
		Settings.set("miscellaneous.client-side-filtering", clientSideFilteringCheckBox.isSelected());
		
		Settings.save();
	}
	
	private void refresh() {
		boolean sshTunnelEnabled = sshTunnelCheckBox.isSelected();
		
		sshHostField.setEditable(sshTunnelEnabled);
		sshPortSpinner.setEnabled(sshTunnelEnabled);
		sshUserField.setEditable(sshTunnelEnabled);
		sshPasswordField.setEditable(sshTunnelEnabled);
		
		sshStrictHostKeyCheckingCheckBox.setEnabled(sshTunnelEnabled);
		sshKnownHostsFileField.setEditable(sshTunnelEnabled ? sshStrictHostKeyCheckingCheckBox.isSelected() : false);
		openSshKnownHostsFileButton.setEnabled(sshTunnelEnabled ? sshStrictHostKeyCheckingCheckBox.isSelected() : false);
		
		sshRsaAuthCheckBox.setEnabled(sshTunnelEnabled);
		sshRsaKeyFileField.setEditable(sshTunnelEnabled ? sshRsaAuthCheckBox.isSelected() : false);
		openSshRsaKeyFileButton.setEnabled(sshTunnelEnabled ? sshRsaAuthCheckBox.isSelected() : false);
		sshRsaPassphraseField.setEditable(sshTunnelEnabled ? sshRsaAuthCheckBox.isSelected() : false);
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		
		if(command.equals("Save")) {
			try {
				saveToConfig();
				
				dispose();
			} catch(IOException ex) {
				JOptionPane.showMessageDialog(this, ex.getLocalizedMessage().replaceAll("\\. ", ".\n"), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command.equals("Discard")) {
			dispose();
		} else if(command.equals("SSH") || command.equals("SHKC") || command.equals("RSA")) {
			refresh();
		} else if(command.equals("OKHF")) {
			final JFileChooser fileChooser = new JFileChooser();
			
			int returnVal = fileChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				sshKnownHostsFileField.setText(fileChooser.getSelectedFile().getPath());
			}
		} else if(command.equals("ORKF")) {
			final JFileChooser fileChooser = new JFileChooser();
			
			int returnVal = fileChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				sshRsaKeyFileField.setText(fileChooser.getSelectedFile().getPath());
			}
		}
    }
	
}