package gr.ntua.ece.db.hfri.erp.frames;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.JTextField;
import javax.swing.JComponent;
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

import gr.ntua.ece.db.hfri.types.Program;

public class ProgramFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private int programId;
	
	private JTextField idField;
	private JTextField nameField;
	private JSpinner addressIdSpinner;
	
	public ProgramFrame(int programId) throws SQLException {
		this.programId = programId;
		
		initialize();
		refresh();
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void initialize() throws SQLException {
		setTitle("New Program");
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
			
			JLabel nameLabel = new JLabel("Name");
			GridBagConstraints nameLabelConstraints = new GridBagConstraints();
			nameLabelConstraints.anchor = GridBagConstraints.WEST;
			nameLabelConstraints.insets = new Insets(0, 0, 10, 5);
			nameLabelConstraints.gridx = 0;
			nameLabelConstraints.gridy = 1;
			contentPane.add(nameLabel, nameLabelConstraints);
			
			nameField = new JTextField();
			nameField.setHorizontalAlignment(SwingConstants.CENTER);
			nameField.setColumns(25);
			GridBagConstraints nameFieldConstraints = new GridBagConstraints();
			nameFieldConstraints.anchor = GridBagConstraints.WEST;
			nameFieldConstraints.insets = new Insets(0, 5, 10, 5);
			nameFieldConstraints.gridx = 1;
			nameFieldConstraints.gridy = 1;
			nameFieldConstraints.gridwidth = 2;
			contentPane.add(nameField, nameFieldConstraints);
			
			JLabel addressIdLabel = new JLabel("Address ID");
			GridBagConstraints addressIdLabelConstraints = new GridBagConstraints();
			addressIdLabelConstraints.anchor = GridBagConstraints.EAST;
			addressIdLabelConstraints.insets = new Insets(0, 15, 10, 5);
			addressIdLabelConstraints.gridx = 2;
			addressIdLabelConstraints.gridy = 0;
			contentPane.add(addressIdLabel, addressIdLabelConstraints);
			
			addressIdSpinner = new JSpinner();
			addressIdSpinner.setModel(new SpinnerNumberModel(1, 1, Database.getAddressCount(), 1));
			GridBagConstraints addressIdSpinnerConstraints = new GridBagConstraints();
			addressIdSpinnerConstraints.anchor = GridBagConstraints.WEST;
			addressIdSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			addressIdSpinnerConstraints.gridx = 3;
			addressIdSpinnerConstraints.gridy = 0;
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
		if(programId > -1) {
			setTitle("Edit Program");
			
			Program program = Database.getProgram(programId);
			
			idField.setText(String.valueOf(program.getId()));
			nameField.setText(program.getName());
			addressIdSpinner.getModel().setValue(program.getAddressId());
		}
		
		validate();
	}
	
	public int getProgramId() {
		return programId;
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		
		if(command.equals("Refresh")) {
			try {
				refresh();
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while fetching program data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command.equals("Save")) {
			try {
				Program program = new Program(
					programId,
					nameField.getText(),
					(int) addressIdSpinner.getModel().getValue());
				
				if(programId > -1) {
					Database.update(program);
					
					dispose();
				} else {
					programId = Database.insert(program);
					
					setTitle("Edit Program");
					
					idField.setText(String.valueOf(programId));
				}
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while saving program data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
        } else if(command.equals("Discard")) {
        	dispose();
        }
    }
	
}