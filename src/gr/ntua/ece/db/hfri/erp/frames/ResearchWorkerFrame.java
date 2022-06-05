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
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;

import javax.swing.border.EmptyBorder;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Date;
import java.util.Calendar;

import java.sql.SQLException;

import gr.ntua.ece.db.hfri.ERP;

import gr.ntua.ece.db.hfri.erp.Utils;
import gr.ntua.ece.db.hfri.erp.Database;

import gr.ntua.ece.db.hfri.types.ResearchWorker;
import gr.ntua.ece.db.hfri.types.ResearchWorker.Sex;

public class ResearchWorkerFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private int researchWorkerId;
	
	private JTextField idField;
	private JSpinner organisationIdSpinner;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JComboBox<String> sexSelector;
	private JTextField ageField;
	private JSpinner birthDateSpinner;
	private JSpinner joinDateSpinner;
	
	public ResearchWorkerFrame(int researchWorkerId) throws SQLException {
		this.researchWorkerId = researchWorkerId;
		
		initialize();
		refresh();
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void initialize() throws SQLException {
		setTitle("New Research Worker");
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
			
			JLabel firstNameLabel = new JLabel("First Name");
			GridBagConstraints firstNameLabelConstraints = new GridBagConstraints();
			firstNameLabelConstraints.anchor = GridBagConstraints.WEST;
			firstNameLabelConstraints.insets = new Insets(0, 0, 10, 5);
			firstNameLabelConstraints.gridx = 0;
			firstNameLabelConstraints.gridy = 1;
			contentPane.add(firstNameLabel, firstNameLabelConstraints);
			
			firstNameField = new JTextField();
			firstNameField.setHorizontalAlignment(SwingConstants.CENTER);
			firstNameField.setColumns(15);
			GridBagConstraints firstNameFieldConstraints = new GridBagConstraints();
			firstNameFieldConstraints.anchor = GridBagConstraints.WEST;
			firstNameFieldConstraints.insets = new Insets(0, 5, 10, 5);
			firstNameFieldConstraints.gridx = 1;
			firstNameFieldConstraints.gridy = 1;
			contentPane.add(firstNameField, firstNameFieldConstraints);
			
			JLabel lastNameLabel = new JLabel("Last Name");
			GridBagConstraints lastNameLabelConstraints = new GridBagConstraints();
			lastNameLabelConstraints.anchor = GridBagConstraints.WEST;
			lastNameLabelConstraints.insets = new Insets(0, 0, 10, 5);
			lastNameLabelConstraints.gridx = 0;
			lastNameLabelConstraints.gridy = 2;
			contentPane.add(lastNameLabel, lastNameLabelConstraints);
			
			lastNameField = new JTextField();
			lastNameField.setHorizontalAlignment(SwingConstants.CENTER);
			lastNameField.setColumns(15);
			GridBagConstraints lastNameFieldConstraints = new GridBagConstraints();
			lastNameFieldConstraints.anchor = GridBagConstraints.WEST;
			lastNameFieldConstraints.insets = new Insets(0, 5, 10, 5);
			lastNameFieldConstraints.gridx = 1;
			lastNameFieldConstraints.gridy = 2;
			contentPane.add(lastNameField, lastNameFieldConstraints);
			
			JLabel sexLabel = new JLabel("Sex");
			GridBagConstraints sexLabelConstraints = new GridBagConstraints();
			sexLabelConstraints.anchor = GridBagConstraints.WEST;
			sexLabelConstraints.insets = new Insets(0, 0, 10, 5);
			sexLabelConstraints.gridx = 0;
			sexLabelConstraints.gridy = 3;
			contentPane.add(sexLabel, sexLabelConstraints);
			
			sexSelector = new JComboBox<String>();
			sexSelector.setModel(new DefaultComboBoxModel<String>(new String[] {"Male", "Female"}));
			GridBagConstraints sexSelectorConstraints = new GridBagConstraints();
			sexSelectorConstraints.anchor = GridBagConstraints.WEST;
			sexSelectorConstraints.insets = new Insets(0, 5, 10, 5);
			sexSelectorConstraints.gridx = 1;
			sexSelectorConstraints.gridy = 3;
			contentPane.add(sexSelector, sexSelectorConstraints);
			
			JLabel birthDateLabel = new JLabel("Birth Date");
			GridBagConstraints birthDateLabelConstraints = new GridBagConstraints();
			birthDateLabelConstraints.anchor = GridBagConstraints.WEST;
			birthDateLabelConstraints.insets = new Insets(0, 0, 10, 5);
			birthDateLabelConstraints.gridx = 0;
			birthDateLabelConstraints.gridy = 4;
			contentPane.add(birthDateLabel, birthDateLabelConstraints);
			
			birthDateSpinner = new JSpinner();
			birthDateSpinner.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
			birthDateSpinner.setEditor(new JSpinner.DateEditor(birthDateSpinner, ERP.getSpinnerDateFormat().toPattern()));
			GridBagConstraints birthDateSpinnerConstraints = new GridBagConstraints();
			birthDateSpinnerConstraints.anchor = GridBagConstraints.WEST;
			birthDateSpinnerConstraints.fill = GridBagConstraints.HORIZONTAL;
			birthDateSpinnerConstraints.insets = new Insets(0, 5, 10, 5);
			birthDateSpinnerConstraints.gridx = 1;
			birthDateSpinnerConstraints.gridy = 4;
			contentPane.add(birthDateSpinner, birthDateSpinnerConstraints);
			
			JLabel organisationIdLabel = new JLabel("Organisation ID");
			GridBagConstraints organisationIdLabelConstraints = new GridBagConstraints();
			organisationIdLabelConstraints.anchor = GridBagConstraints.EAST;
			organisationIdLabelConstraints.insets = new Insets(0, 15, 10, 5);
			organisationIdLabelConstraints.gridx = 2;
			organisationIdLabelConstraints.gridy = 1;
			contentPane.add(organisationIdLabel, organisationIdLabelConstraints);
			
			organisationIdSpinner = new JSpinner();
			organisationIdSpinner.setModel(new SpinnerNumberModel(1, 1, Database.getOrganisationCount(), 1));
			GridBagConstraints organisationIdSpinnerConstraints = new GridBagConstraints();
			organisationIdSpinnerConstraints.anchor = GridBagConstraints.WEST;
			organisationIdSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			organisationIdSpinnerConstraints.gridx = 3;
			organisationIdSpinnerConstraints.gridy = 1;
			contentPane.add(organisationIdSpinner, organisationIdSpinnerConstraints);
			
			JLabel joinDateLabel = new JLabel("Join Date");
			GridBagConstraints joinDateLabelConstraints = new GridBagConstraints();
			joinDateLabelConstraints.anchor = GridBagConstraints.EAST;
			joinDateLabelConstraints.insets = new Insets(0, 15, 10, 5);
			joinDateLabelConstraints.gridx = 2;
			joinDateLabelConstraints.gridy = 2;
			contentPane.add(joinDateLabel, joinDateLabelConstraints);
			
			joinDateSpinner = new JSpinner();
			joinDateSpinner.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
			joinDateSpinner.setEditor(new JSpinner.DateEditor(joinDateSpinner, ERP.getSpinnerDateFormat().toPattern()));
			GridBagConstraints joinDateSpinnerConstraints = new GridBagConstraints();
			joinDateSpinnerConstraints.anchor = GridBagConstraints.WEST;
			joinDateSpinnerConstraints.fill = GridBagConstraints.HORIZONTAL;
			joinDateSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			joinDateSpinnerConstraints.gridx = 3;
			joinDateSpinnerConstraints.gridy = 2;
			contentPane.add(joinDateSpinner, joinDateSpinnerConstraints);
			
			JLabel ageLabel = new JLabel("Age");
			GridBagConstraints ageLabelConstraints = new GridBagConstraints();
			ageLabelConstraints.anchor = GridBagConstraints.EAST;
			ageLabelConstraints.insets = new Insets(0, 15, 10, 5);
			ageLabelConstraints.gridx = 2;
			ageLabelConstraints.gridy = 4;
			contentPane.add(ageLabel, ageLabelConstraints);
			
			ageField = new JTextField("-");
			ageField.setHorizontalAlignment(SwingConstants.CENTER);
			ageField.setColumns(4);
			ageField.setEditable(false);
			GridBagConstraints ageFieldConstraints = new GridBagConstraints();
			ageFieldConstraints.anchor = GridBagConstraints.WEST;
			ageFieldConstraints.insets = new Insets(0, 5, 10, 0);
			ageFieldConstraints.gridx = 3;
			ageFieldConstraints.gridy = 4;
			contentPane.add(ageField, ageFieldConstraints);
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
		if(researchWorkerId > -1) {
			setTitle("Edit Research Worker");
			
			ResearchWorker researchWorker = Database.getResearchWorker(researchWorkerId);
			
			firstNameField.setText(researchWorker.getFirstName());
			lastNameField.setText(researchWorker.getLastName());
			sexSelector.setSelectedIndex(researchWorker.getSex() == Sex.MALE ? 0 : 1);
			ageField.setText(String.valueOf(researchWorker.getAge()));
			birthDateSpinner.getModel().setValue(researchWorker.getBirthDate());
			joinDateSpinner.getModel().setValue(researchWorker.getJoinDate());
			idField.setText(String.valueOf(researchWorker.getId()));
			organisationIdSpinner.getModel().setValue(researchWorker.getOrganisationId());
		}
		
		validate();
	}
	
	public int getResearchWorkerId() {
		return researchWorkerId;
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		
		if(command.equals("Refresh")) {
			try {
				refresh();
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while fetching research worker data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command.equals("Save")) {
			try {
				ResearchWorker researchWorker = new ResearchWorker(
					researchWorkerId,
					(int) organisationIdSpinner.getModel().getValue(),
					firstNameField.getText(),
					lastNameField.getText(),
					Sex.valueOf(sexSelector.getSelectedItem().toString().toUpperCase()),
					Utils.getDateDiffYears(((Date) birthDateSpinner.getModel().getValue()), new Date()),
					((Date) birthDateSpinner.getModel().getValue()),
					((Date) joinDateSpinner.getModel().getValue()));
				
				if(researchWorkerId > -1) {
					Database.update(researchWorker);
					
					dispose();
				} else {
					researchWorkerId = Database.insert(researchWorker);
					
					setTitle("Edit Research Worker");
					
					idField.setText(String.valueOf(researchWorkerId));
					ageField.setText(String.valueOf(researchWorker.getAge()));
				}
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while saving research worker data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
        } else if(command.equals("Discard")) {
        	dispose();
        }
    }
	
}