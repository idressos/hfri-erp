package gr.ntua.ece.db.hfri.erp.frames;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;

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

import gr.ntua.ece.db.hfri.types.Organisation;
import gr.ntua.ece.db.hfri.types.Organisation.OrganisationType;

public class OrganisationFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private int organisationId;
	
	private JTextField idField;
	private JSpinner addressIdSpinner;
	private JTextField acronymField;
	private JTextField nameField;
	private JComboBox<String> typeSelector;
	private JSpinner budgetSpinner;
	
	public OrganisationFrame(int organisationId) throws SQLException {
		this.organisationId = organisationId;
		
		initialize();
		refresh();
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void initialize() throws SQLException {
		setTitle("New Organisation");
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
			
			JLabel acronymLabel = new JLabel("Acronym");
			GridBagConstraints acronymLabelConstraints = new GridBagConstraints();
			acronymLabelConstraints.anchor = GridBagConstraints.WEST;
			acronymLabelConstraints.insets = new Insets(0, 0, 10, 5);
			acronymLabelConstraints.gridx = 0;
			acronymLabelConstraints.gridy = 1;
			contentPane.add(acronymLabel, acronymLabelConstraints);
			
			acronymField = new JTextField();
			acronymField.setHorizontalAlignment(SwingConstants.CENTER);
			acronymField.setColumns(10);
			GridBagConstraints acronymFieldConstraints = new GridBagConstraints();
			acronymFieldConstraints.anchor = GridBagConstraints.WEST;
			acronymFieldConstraints.insets = new Insets(0, 5, 10, 5);
			acronymFieldConstraints.gridx = 1;
			acronymFieldConstraints.gridy = 1;
			contentPane.add(acronymField, acronymFieldConstraints);
			
			JLabel nameLabel = new JLabel("Name");
			GridBagConstraints nameLabelConstraints = new GridBagConstraints();
			nameLabelConstraints.anchor = GridBagConstraints.WEST;
			nameLabelConstraints.insets = new Insets(0, 0, 10, 5);
			nameLabelConstraints.gridx = 0;
			nameLabelConstraints.gridy = 2;
			contentPane.add(nameLabel, nameLabelConstraints);
			
			nameField = new JTextField();
			nameField.setHorizontalAlignment(SwingConstants.CENTER);
			nameField.setColumns(20);
			GridBagConstraints nameFieldConstraints = new GridBagConstraints();
			nameFieldConstraints.anchor = GridBagConstraints.WEST;
			nameFieldConstraints.insets = new Insets(0, 5, 10, 5);
			nameFieldConstraints.gridx = 1;
			nameFieldConstraints.gridy = 2;
			contentPane.add(nameField, nameFieldConstraints);
			
			JLabel typeLabel = new JLabel("Type");
			GridBagConstraints typeLabelConstraints = new GridBagConstraints();
			typeLabelConstraints.anchor = GridBagConstraints.WEST;
			typeLabelConstraints.insets = new Insets(0, 0, 10, 5);
			typeLabelConstraints.gridx = 0;
			typeLabelConstraints.gridy = 3;
			contentPane.add(typeLabel, typeLabelConstraints);
			
			typeSelector = new JComboBox<String>();
			typeSelector.setModel(new DefaultComboBoxModel<String>(new String[] {"College", "Research Center", "Company"}));
			GridBagConstraints typeSelectorConstraints = new GridBagConstraints();
			typeSelectorConstraints.anchor = GridBagConstraints.WEST;
			typeSelectorConstraints.insets = new Insets(0, 5, 10, 5);
			typeSelectorConstraints.gridx = 1;
			typeSelectorConstraints.gridy = 3;
			contentPane.add(typeSelector, typeSelectorConstraints);
			
			JLabel budgetLabel = new JLabel("Budget");
			GridBagConstraints budgetLabelConstraints = new GridBagConstraints();
			budgetLabelConstraints.anchor = GridBagConstraints.WEST;
			budgetLabelConstraints.insets = new Insets(0, 0, 10, 5);
			budgetLabelConstraints.gridx = 0;
			budgetLabelConstraints.gridy = 4;
			contentPane.add(budgetLabel, budgetLabelConstraints);
			
			budgetSpinner = new JSpinner();
			budgetSpinner.setModel(new SpinnerNumberModel(100000, 100000, 10000000, 1));
			GridBagConstraints budgetSpinnerConstraints = new GridBagConstraints();
			budgetSpinnerConstraints.anchor = GridBagConstraints.WEST;
			budgetSpinnerConstraints.insets = new Insets(0, 5, 10, 5);
			budgetSpinnerConstraints.gridx = 1;
			budgetSpinnerConstraints.gridy = 4;
			contentPane.add(budgetSpinner, budgetSpinnerConstraints);
			
			JLabel addressIdLabel = new JLabel("Address ID");
			GridBagConstraints addressIdLabelConstraints = new GridBagConstraints();
			addressIdLabelConstraints.anchor = GridBagConstraints.EAST;
			addressIdLabelConstraints.insets = new Insets(0, 15, 10, 5);
			addressIdLabelConstraints.gridx = 2;
			addressIdLabelConstraints.gridy = 1;
			contentPane.add(addressIdLabel, addressIdLabelConstraints);
			
			addressIdSpinner = new JSpinner();
			addressIdSpinner.setModel(new SpinnerNumberModel(1, 1, Database.getAddressCount(), 1));
			GridBagConstraints addressIdSpinnerConstraints = new GridBagConstraints();
			addressIdSpinnerConstraints.anchor = GridBagConstraints.WEST;
			addressIdSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			addressIdSpinnerConstraints.gridx = 3;
			addressIdSpinnerConstraints.gridy = 1;
			contentPane.add(addressIdSpinner, addressIdSpinnerConstraints);
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
		if(organisationId > -1) {
			setTitle("Edit Organisation");
			
			Organisation organisation = Database.getOrganisation(organisationId);
			
			idField.setText(String.valueOf(organisation.getId()));
			acronymField.setText(organisation.getAcronym());
			nameField.setText(organisation.getName());
			typeSelector.setSelectedIndex(organisation.getType() == OrganisationType.COLLEGE ? 0 : (organisation.getType() == OrganisationType.RESEARCH_CENTER ? 1 : 2));
			budgetSpinner.getModel().setValue(organisation.getBudget());
			addressIdSpinner.getModel().setValue(organisation.getAddressId());
		}
		
		validate();
	}
	
	public int getOrganisationId() {
		return organisationId;
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		
		if(command.equals("Refresh")) {
			try {
				refresh();
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while fetching organisation data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command.equals("Save")) {
			try {
				Organisation organisation = new Organisation(
					organisationId,
					acronymField.getText(),
					nameField.getText(),
					OrganisationType.valueOf(typeSelector.getSelectedItem().toString().toUpperCase().replaceAll(" ", "_")),
					(int) addressIdSpinner.getModel().getValue(),
					(int) budgetSpinner.getModel().getValue());
				
				if(organisationId > -1) {
					Database.update(organisation);
					
					dispose();
				} else {
					organisationId = Database.insert(organisation);
					
					setTitle("Edit Organisation");
					
					idField.setText(String.valueOf(organisationId));
				}
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while saving organisation data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
        } else if(command.equals("Discard")) {
        	dispose();
        }
    }
	
}