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
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

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

import gr.ntua.ece.db.hfri.types.Project;

public class ProjectFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private int projectId;
	
	private JTextField idField;
	private JTextField titleField;
	private JTextField descriptionField;
	private JSpinner startDateSpinner;
	private JSpinner finishDateSpinner;
	private JTextField durationYearsField;
	private JLabel statusField;
	private JSpinner organisationIdSpinner;
	private JSpinner executiveIdSpinner;
	private JSpinner supervisorIdSpinner;
	
	public ProjectFrame(int projectId) throws SQLException {
		this.projectId = projectId;
		
		initialize();
		refresh();
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void initialize() throws SQLException {
		setTitle("New Project");
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
			
			JLabel titleLabel = new JLabel("Title");
			GridBagConstraints titleLabelConstraints = new GridBagConstraints();
			titleLabelConstraints.anchor = GridBagConstraints.WEST;
			titleLabelConstraints.insets = new Insets(0, 0, 10, 5);
			titleLabelConstraints.gridx = 0;
			titleLabelConstraints.gridy = 1;
			contentPane.add(titleLabel, titleLabelConstraints);
			
			titleField = new JTextField();
			titleField.setHorizontalAlignment(SwingConstants.CENTER);
			titleField.setColumns(22);
			GridBagConstraints titleFieldConstraints = new GridBagConstraints();
			titleFieldConstraints.anchor = GridBagConstraints.WEST;
			titleFieldConstraints.insets = new Insets(0, 5, 10, 5);
			titleFieldConstraints.gridx = 1;
			titleFieldConstraints.gridy = 1;
			contentPane.add(titleField, titleFieldConstraints);
			
			JLabel descriptionLabel = new JLabel("Description");
			GridBagConstraints descriptionLabelConstraints = new GridBagConstraints();
			descriptionLabelConstraints.anchor = GridBagConstraints.WEST;
			descriptionLabelConstraints.insets = new Insets(0, 0, 10, 5);
			descriptionLabelConstraints.gridx = 0;
			descriptionLabelConstraints.gridy = 2;
			contentPane.add(descriptionLabel, descriptionLabelConstraints);
			
			descriptionField = new JTextField();
			descriptionField.setHorizontalAlignment(SwingConstants.CENTER);
			descriptionField.setColumns(25);
			GridBagConstraints descriptionFieldConstraints = new GridBagConstraints();
			descriptionFieldConstraints.anchor = GridBagConstraints.WEST;
			descriptionFieldConstraints.insets = new Insets(0, 5, 10, 5);
			descriptionFieldConstraints.gridx = 1;
			descriptionFieldConstraints.gridy = 2;
			contentPane.add(descriptionField, descriptionFieldConstraints);
			
			JLabel startDateLabel = new JLabel("Start Date");
			GridBagConstraints startDateLabelConstraints = new GridBagConstraints();
			startDateLabelConstraints.anchor = GridBagConstraints.WEST;
			startDateLabelConstraints.insets = new Insets(0, 0, 10, 5);
			startDateLabelConstraints.gridx = 0;
			startDateLabelConstraints.gridy = 3;
			contentPane.add(startDateLabel, startDateLabelConstraints);
			
			startDateSpinner = new JSpinner();
			startDateSpinner.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
			startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, ERP.getSpinnerDateFormat().toPattern()));
			GridBagConstraints startDateSpinnerConstraints = new GridBagConstraints();
			startDateSpinnerConstraints.anchor = GridBagConstraints.WEST;
			startDateSpinnerConstraints.fill = GridBagConstraints.HORIZONTAL;
			startDateSpinnerConstraints.insets = new Insets(0, 5, 10, 5);
			startDateSpinnerConstraints.gridx = 1;
			startDateSpinnerConstraints.gridy = 3;
			contentPane.add(startDateSpinner, startDateSpinnerConstraints);
			
			JLabel finishDateLabel = new JLabel("Finish Date");
			GridBagConstraints finishDateLabelConstraints = new GridBagConstraints();
			finishDateLabelConstraints.anchor = GridBagConstraints.WEST;
			finishDateLabelConstraints.insets = new Insets(0, 0, 10, 5);
			finishDateLabelConstraints.gridx = 0;
			finishDateLabelConstraints.gridy = 4;
			contentPane.add(finishDateLabel, finishDateLabelConstraints);
			
			finishDateSpinner = new JSpinner();
			finishDateSpinner.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
			finishDateSpinner.setEditor(new JSpinner.DateEditor(finishDateSpinner, ERP.getSpinnerDateFormat().toPattern()));
			GridBagConstraints finishDateSpinnerConstraints = new GridBagConstraints();
			finishDateSpinnerConstraints.anchor = GridBagConstraints.WEST;
			finishDateSpinnerConstraints.fill = GridBagConstraints.HORIZONTAL;
			finishDateSpinnerConstraints.insets = new Insets(0, 5, 10, 5);
			finishDateSpinnerConstraints.gridx = 1;
			finishDateSpinnerConstraints.gridy = 4;
			contentPane.add(finishDateSpinner, finishDateSpinnerConstraints);
			
			JLabel organisationIdLabel = new JLabel("Organisation ID");
			GridBagConstraints organisationIdLabelConstraints = new GridBagConstraints();
			organisationIdLabelConstraints.anchor = GridBagConstraints.EAST;
			organisationIdLabelConstraints.insets = new Insets(0, 15, 10, 5);
			organisationIdLabelConstraints.gridx = 2;
			organisationIdLabelConstraints.gridy = 0;
			contentPane.add(organisationIdLabel, organisationIdLabelConstraints);
			
			organisationIdSpinner = new JSpinner();
			organisationIdSpinner.setModel(new SpinnerNumberModel(1, 1, Database.getOrganisationCount(), 1));
			GridBagConstraints organisationIdSpinnerConstraints = new GridBagConstraints();
			organisationIdSpinnerConstraints.anchor = GridBagConstraints.WEST;
			organisationIdSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			organisationIdSpinnerConstraints.gridx = 3;
			organisationIdSpinnerConstraints.gridy = 0;
			contentPane.add(organisationIdSpinner, organisationIdSpinnerConstraints);
			
			JLabel executiveIdLabel = new JLabel("Executive ID");
			GridBagConstraints executiveIdLabelConstraints = new GridBagConstraints();
			executiveIdLabelConstraints.anchor = GridBagConstraints.EAST;
			executiveIdLabelConstraints.insets = new Insets(0, 15, 10, 5);
			executiveIdLabelConstraints.gridx = 2;
			executiveIdLabelConstraints.gridy = 1;
			contentPane.add(executiveIdLabel, executiveIdLabelConstraints);
			
			executiveIdSpinner = new JSpinner();
			executiveIdSpinner.setModel(new SpinnerNumberModel(1, 1, Database.getExecutiveCount(), 1));
			GridBagConstraints executiveIdSpinnerConstraints = new GridBagConstraints();
			executiveIdSpinnerConstraints.anchor = GridBagConstraints.WEST;
			executiveIdSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			executiveIdSpinnerConstraints.gridx = 3;
			executiveIdSpinnerConstraints.gridy = 1;
			contentPane.add(executiveIdSpinner, executiveIdSpinnerConstraints);
			
			JLabel supervisorIdLabel = new JLabel("Supervisor ID");
			GridBagConstraints supervisorIdLabelConstraints = new GridBagConstraints();
			supervisorIdLabelConstraints.anchor = GridBagConstraints.EAST;
			supervisorIdLabelConstraints.insets = new Insets(0, 15, 10, 5);
			supervisorIdLabelConstraints.gridx = 2;
			supervisorIdLabelConstraints.gridy = 2;
			contentPane.add(supervisorIdLabel, supervisorIdLabelConstraints);
			
			supervisorIdSpinner = new JSpinner();
			supervisorIdSpinner.setModel(new SpinnerNumberModel(1, 1, Database.getResearchWorkerCount(), 1));
			GridBagConstraints supervisorIdSpinnerConstraints = new GridBagConstraints();
			supervisorIdSpinnerConstraints.anchor = GridBagConstraints.WEST;
			supervisorIdSpinnerConstraints.insets = new Insets(0, 5, 10, 0);
			supervisorIdSpinnerConstraints.gridx = 3;
			supervisorIdSpinnerConstraints.gridy = 2;
			contentPane.add(supervisorIdSpinner, supervisorIdSpinnerConstraints);
			
			JLabel durationYearsLabel = new JLabel("Duration (Years)");
			GridBagConstraints durationYearsLabelConstraints = new GridBagConstraints();
			durationYearsLabelConstraints.anchor = GridBagConstraints.EAST;
			durationYearsLabelConstraints.insets = new Insets(0, 15, 10, 5);
			durationYearsLabelConstraints.gridx = 2;
			durationYearsLabelConstraints.gridy = 4;
			contentPane.add(durationYearsLabel, durationYearsLabelConstraints);
			
			durationYearsField = new JTextField("-");
			durationYearsField.setHorizontalAlignment(SwingConstants.CENTER);
			durationYearsField.setColumns(4);
			durationYearsField.setEditable(false);
			GridBagConstraints durationYearsFieldConstraints = new GridBagConstraints();
			durationYearsFieldConstraints.anchor = GridBagConstraints.WEST;
			durationYearsFieldConstraints.insets = new Insets(0, 5, 10, 0);
			durationYearsFieldConstraints.gridx = 3;
			durationYearsFieldConstraints.gridy = 4;
			contentPane.add(durationYearsField, durationYearsFieldConstraints);
			
			JLabel statusLabel = new JLabel("Status");
			GridBagConstraints statusLabelConstraints = new GridBagConstraints();
			statusLabelConstraints.anchor = GridBagConstraints.EAST;
			statusLabelConstraints.insets = new Insets(0, 15, 10, 5);
			statusLabelConstraints.gridx = 2;
			statusLabelConstraints.gridy = 3;
			contentPane.add(statusLabel, statusLabelConstraints);
			
			statusField = new JLabel("UNDETERMINED");
			GridBagConstraints statusFieldConstraints = new GridBagConstraints();
			statusFieldConstraints.anchor = GridBagConstraints.WEST;
			statusFieldConstraints.insets = new Insets(0, 5, 10, 5);
			statusFieldConstraints.gridx = 3;
			statusFieldConstraints.gridy = 3;
			contentPane.add(statusField, statusFieldConstraints);
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
		if(projectId > -1) {
			setTitle("Edit Project");
			
			Project project = Database.getProject(projectId);
			
			idField.setText(String.valueOf(project.getId()));
			titleField.setText(project.getTitle());
			descriptionField.setText(project.getDescription());
			startDateSpinner.getModel().setValue(project.getStartDate());
			finishDateSpinner.getModel().setValue(project.getFinishDate());
			durationYearsField.setText(String.valueOf(project.getDurationYears()));
			statusField.setText(projectStatusHtml(project.getStatusCode()));
			organisationIdSpinner.getModel().setValue(project.getOrganisationId());
			executiveIdSpinner.getModel().setValue(project.getExecutiveId());
			supervisorIdSpinner.getModel().setValue(project.getSupervisorId());
		}
		
		validate();
	}
	
	private static String projectStatusHtml(int statusCode) {
		if(statusCode == 0) return "<html><font color='#D82423'>INACTIVE</font></html>";
		if(statusCode == 1) return "<html><font color='#639435'>ACTIVE</font></html>";
		if(statusCode == 2) return "<html><font color='#43A4CC'>SCHEDULED</font></html>";
		
		return "UNKNOWN";
	}
	
	public int getProjectId() {
		return projectId;
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		
		if(command.equals("Refresh")) {
			try {
				refresh();
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while fetching project data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command.equals("Save")) {
			int durationYears = Utils.getDateDiffYears(
									(Date) startDateSpinner.getModel().getValue(),
									(Date) finishDateSpinner.getModel().getValue());
			
			if(durationYears < 1 || durationYears > 4) {
				JOptionPane.showMessageDialog(this, "Project duration must be between 1 and 4 years.", "Error", JOptionPane.ERROR_MESSAGE);
			} else try {
				Project project = new Project(
					projectId,
					titleField.getText(),
					descriptionField.getText(),
					(Date) startDateSpinner.getModel().getValue(),
					(Date) finishDateSpinner.getModel().getValue(),
					durationYears,
					(int) organisationIdSpinner.getValue(),
					(int) executiveIdSpinner.getValue(),
					(int) supervisorIdSpinner.getValue());
				
				if(projectId > -1) {
					Database.update(project);
					
					dispose();
				} else {
					projectId = Database.insert(project);
					
					setTitle("Edit Project");
					
					idField.setText(String.valueOf(projectId));
					durationYearsField.setText(String.valueOf(project.getDurationYears()));
					statusField.setText(projectStatusHtml(project.getStatusCode()));
				}
			} catch(SQLException ex) {
				JOptionPane.showMessageDialog(this, "An SQL exception occured while saving project data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
        } else if(command.equals("Discard")) {
        	dispose();
        }
    }
	
}