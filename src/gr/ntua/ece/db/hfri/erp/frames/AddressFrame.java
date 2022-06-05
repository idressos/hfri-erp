package gr.ntua.ece.db.hfri.erp.frames;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;

import javax.swing.border.EmptyBorder;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;

import gr.ntua.ece.db.hfri.ERP;

import gr.ntua.ece.db.hfri.erp.Database;

import gr.ntua.ece.db.hfri.types.Address;

public class AddressFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private int addressId;
	
	private JTextField idField;
	private JSpinner numberSpinner;
	private JTextField streetField;
	private JTextField cityField;
	private JTextField postalCodeField;
	
	public AddressFrame(int addressId) throws SQLException {
		this.addressId = addressId;
		
		initialize();
		refresh();
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void initialize() throws SQLException {
		setTitle("New Address");
		setIconImage(ERP.getApplicationIcon());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 0, 0);
		setResizable(false);
		
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 5, 5, 5));
		contentPane.setLayout(new GridBagLayout());
		setContentPane(contentPane);
		
		{
			JLabel idLabel = new JLabel("ID");
			GridBagConstraints idLabelConstraints = new GridBagConstraints();
			idLabelConstraints.anchor = GridBagConstraints.WEST;
			idLabelConstraints.insets = new Insets(0, 0, 10, 5);
			idLabelConstraints.gridx = 0;
			idLabelConstraints.gridy = 0;
			contentPane.add(idLabel, idLabelConstraints);
			
			idField = new JTextField("-");
			idField.setHorizontalAlignment(SwingConstants.CENTER);
			idField.setColumns(4);
			idField.setEditable(false);
			GridBagConstraints idFieldConstraints = new GridBagConstraints();
			idFieldConstraints.anchor = GridBagConstraints.WEST;
			idFieldConstraints.insets = new Insets(0, 5, 10, 5);
			idFieldConstraints.gridx = 1;
			idFieldConstraints.gridy = 0;
			contentPane.add(idField, idFieldConstraints);
			
			JLabel streetLabel = new JLabel("Street");
			GridBagConstraints streetLabelConstraints = new GridBagConstraints();
			streetLabelConstraints.anchor = GridBagConstraints.WEST;
			streetLabelConstraints.insets = new Insets(0, 0, 10, 5);
			streetLabelConstraints.gridx = 0;
			streetLabelConstraints.gridy = 1;
			contentPane.add(streetLabel, streetLabelConstraints);
			
			streetField = new JTextField();
			streetField.setHorizontalAlignment(SwingConstants.CENTER);
			streetField.setColumns(15);
			GridBagConstraints streetFieldConstraints = new GridBagConstraints();
			streetFieldConstraints.anchor = GridBagConstraints.WEST;
			streetFieldConstraints.insets = new Insets(0, 5, 10, 5);
			streetFieldConstraints.gridx = 1;
			streetFieldConstraints.gridy = 1;
			contentPane.add(streetField, streetFieldConstraints);
			
			JLabel cityLabel = new JLabel("City");
			GridBagConstraints cityLabelConstraints = new GridBagConstraints();
			cityLabelConstraints.anchor = GridBagConstraints.WEST;
			cityLabelConstraints.insets = new Insets(0, 0, 10, 5);
			cityLabelConstraints.gridx = 0;
			cityLabelConstraints.gridy = 2;
			contentPane.add(cityLabel, cityLabelConstraints);
			
			cityField = new JTextField();
			cityField.setHorizontalAlignment(SwingConstants.CENTER);
			cityField.setColumns(15);
			GridBagConstraints cityFieldConstraints = new GridBagConstraints();
			cityFieldConstraints.anchor = GridBagConstraints.WEST;
			cityFieldConstraints.insets = new Insets(0, 5, 10, 5);
			cityFieldConstraints.gridx = 1;
			cityFieldConstraints.gridy = 2;
			contentPane.add(cityField, cityFieldConstraints);
			
			JLabel postalCodeLabel = new JLabel("Postal Code");
			GridBagConstraints postalCodeLabelConstraints = new GridBagConstraints();
			postalCodeLabelConstraints.anchor = GridBagConstraints.WEST;
			postalCodeLabelConstraints.insets = new Insets(0, 0, 10, 5);
			postalCodeLabelConstraints.gridx = 0;
			postalCodeLabelConstraints.gridy = 3;
			contentPane.add(postalCodeLabel, postalCodeLabelConstraints);
			
			postalCodeField = new JTextField();
			postalCodeField.setHorizontalAlignment(SwingConstants.CENTER);
			postalCodeField.setColumns(8);
			GridBagConstraints postalCodeFieldConstraints = new GridBagConstraints();
			postalCodeFieldConstraints.anchor = GridBagConstraints.WEST;
			postalCodeFieldConstraints.insets = new Insets(0, 5, 10, 5);
			postalCodeFieldConstraints.gridx = 1;
			postalCodeFieldConstraints.gridy = 3;
			contentPane.add(postalCodeField, postalCodeFieldConstraints);
			
			JLabel numberLabel = new JLabel("Number");
			GridBagConstraints numberLabelConstraints = new GridBagConstraints();
			numberLabelConstraints.anchor = GridBagConstraints.EAST;
			numberLabelConstraints.insets = new Insets(0, 15, 10, 5);
			numberLabelConstraints.gridx = 2;
			numberLabelConstraints.gridy = 1;
			contentPane.add(numberLabel, numberLabelConstraints);
			
			numberSpinner = new JSpinner();
			numberSpinner.setModel(new SpinnerNumberModel(1, 1, 20000, 1));
			GridBagConstraints numberSpinnerConstraints = new GridBagConstraints();
			numberSpinnerConstraints.anchor = GridBagConstraints.WEST;
			numberSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			numberSpinnerConstraints.gridx = 3;
			numberSpinnerConstraints.gridy = 1;
			contentPane.add(numberSpinner, numberSpinnerConstraints);
		}
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(this);
		GridBagConstraints refreshButtonConstraints = new GridBagConstraints();
		refreshButtonConstraints.insets = new Insets(5, 0, 0, 0);
		refreshButtonConstraints.gridx = 0;
		refreshButtonConstraints.gridy = 6;
		contentPane.add(refreshButton, refreshButtonConstraints);
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		saveButton.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints saveButtonConstraints = new GridBagConstraints();
		saveButtonConstraints.anchor = GridBagConstraints.WEST;
		saveButtonConstraints.insets = new Insets(5, 0, 0, 90);
		saveButtonConstraints.gridx = 3;
		saveButtonConstraints.gridy = 6;
		contentPane.add(saveButton, saveButtonConstraints);
		
		JButton discardButton = new JButton("Discard");
		discardButton.addActionListener(this);
		discardButton.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints discardButtonConstraints = new GridBagConstraints();
		discardButtonConstraints.anchor = GridBagConstraints.EAST;
		discardButtonConstraints.insets = new Insets(5, 0, 0, 0);
		discardButtonConstraints.gridx = 3;
		discardButtonConstraints.gridy = 6;
		contentPane.add(discardButton, discardButtonConstraints);
	}
	
	private void refresh() throws SQLException {
		if(addressId > -1) {
			setTitle("Edit Address");
			
			Address address = Database.getAddress(addressId);
			
			idField.setText(String.valueOf(address.getId()));
			streetField.setText(address.getStreet());
			cityField.setText(address.getCity());
			postalCodeField.setText(address.getPostalCode());
			numberSpinner.getModel().setValue(address.getNumber());
		}
		
		validate();
	}
	
	public int getAddressId() {
		return addressId;
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		
		if(command.equals("Refresh")) {
			try {
				refresh();
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while fetching address data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command.equals("Save")) {
			try {
				Address address = new Address(
					addressId,
					(int) numberSpinner.getModel().getValue(),
					streetField.getText(),
					cityField.getText(),
					postalCodeField.getText());
				
				if(addressId > -1) {
					Database.update(address);
					
					dispose();
				} else {
					addressId = Database.insert(address);
					
					setTitle("Edit Address");
					
					idField.setText(String.valueOf(addressId));
				}
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while saving address data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
        } else if(command.equals("Discard")) {
        	dispose();
        }
    }
	
}