package gr.ntua.ece.db.hfri.erp.panels;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.SpinnerDateModel;
import javax.swing.DefaultListModel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.DefaultComboBoxModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.IOException;

import java.sql.SQLException;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

import java.util.regex.Pattern;

import java.text.ParseException;

import gr.ntua.ece.db.hfri.ERP;

import gr.ntua.ece.db.hfri.erp.Utils;
import gr.ntua.ece.db.hfri.erp.Database;
import gr.ntua.ece.db.hfri.erp.Settings;

import gr.ntua.ece.db.hfri.types.Project;
import gr.ntua.ece.db.hfri.types.Executive;
import gr.ntua.ece.db.hfri.types.ResearchWorker;

import gr.ntua.ece.db.hfri.erp.frames.Dashboard;

public class ProjectsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Dashboard dashboard;
	
	private JList<Project> list;
	private JScrollPane scrollPane;
	
	private JTextField searchField;
	private JComboBox<Executive> executiveFilter;
	private JComboBox<ResearchWorker> researchWorkerFilter;
	private JSpinner maxDurationYearsFilter;
	private JSpinner maxStartDateFilter;
	private JSpinner minFinishDateFilter;
	
	private int executiveFilterLastSelectedId;
	private int researchWorkerFilterLastSelectedId;
	
	private ItemListener executiveFilterChangeListener = new ItemListener () {
		public void itemStateChanged(ItemEvent event) {
			if(event.getStateChange() == ItemEvent.SELECTED) {
				try {
					executiveFilterLastSelectedId = ((Executive) executiveFilter.getSelectedItem()).getId();
					
					executiveFilter.removeItemListener(executiveFilterChangeListener);
					refresh();
					executiveFilter.addItemListener(executiveFilterChangeListener);
				} catch(SQLException | IOException ex) {}
			}
		}
	};
	
	private ItemListener researchWorkerFilterChangeListener = new ItemListener () {
		public void itemStateChanged(ItemEvent event) {
			if(event.getStateChange() == ItemEvent.SELECTED) {
				try {
					researchWorkerFilterLastSelectedId = ((ResearchWorker) researchWorkerFilter.getSelectedItem()).getId();
					
					researchWorkerFilter.removeItemListener(researchWorkerFilterChangeListener);
					refresh();
					researchWorkerFilter.addItemListener(researchWorkerFilterChangeListener);
				} catch(SQLException | IOException ex) {}
			}
		}
	};
	
	public ProjectsPanel(Dashboard dashboard) {
		this.dashboard = dashboard;
		
		setLayout(new BorderLayout(0, 0));
		
		list = new JList<>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new ProjectCellRenderer());
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				int index  = list.locationToIndex(event.getPoint());
				
				if(index > -1) {
					Project project = list.getModel().getElementAt(index);
					
					if(event.getButton() == 1 && event.getClickCount() == 1) {
						dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "PROJ " + project.getId()));
					} else if(event.getButton() == 3 && event.getClickCount() == 1) {
						JPopupMenu popupMenu = new JPopupMenu();
						
						JMenuItem popupMenuLabel = new JMenuItem("<html><center>" + project.getTitle() + "</center></html>");
						popupMenuLabel.setEnabled(false);
						popupMenu.add(popupMenuLabel);
						popupMenu.addSeparator();
						JMenuItem viewOrganisationMenuButton = new JMenuItem("View Organisation");
						viewOrganisationMenuButton.setActionCommand("ORG " + project.getOrganisationId());
						viewOrganisationMenuButton.addActionListener(dashboard);
						popupMenu.add(viewOrganisationMenuButton);
						JMenuItem viewExecutiveMenuButton = new JMenuItem("View Executive");
						viewExecutiveMenuButton.setActionCommand("EXEC " + project.getExecutiveId());
						viewExecutiveMenuButton.addActionListener(dashboard);
						popupMenu.add(viewExecutiveMenuButton);
						JMenuItem viewSupervisorMenuButton = new JMenuItem("View Supervisor");
						viewSupervisorMenuButton.setActionCommand("RW " + project.getSupervisorId());
						viewSupervisorMenuButton.addActionListener(dashboard);
						popupMenu.add(viewSupervisorMenuButton);
						JMenuItem viewResearchWorkersMenuButton = new JMenuItem("View Research Workers");
						viewResearchWorkersMenuButton.setActionCommand("PROJ_RW " + project.getId());
						viewResearchWorkersMenuButton.addActionListener(dashboard);
						popupMenu.add(viewResearchWorkersMenuButton);
						
						popupMenu.show(list, event.getX(), event.getY());
					}
				}
			}
		});
		list.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					Project project = list.getSelectedValue();
					
					if(project != null) dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "PROJ " + project.getId()));
				}
			}
		});
		list.setBackground(getBackground());
		
		scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
		
		JToolBar filtersBar = new JToolBar();
		filtersBar.setFloatable(false);
		add(filtersBar, BorderLayout.SOUTH);
		
		JLabel searchFieldLabel = new JLabel("Search");
		filtersBar.add(searchFieldLabel);
		
		filtersBar.addSeparator();
		
		searchField = new JTextField();
		searchField.setToolTipText("Search projects by title");
		searchField.getDocument().addDocumentListener(searchFieldChangeListener);
		filtersBar.add(searchField);
		
		filtersBar.addSeparator(new Dimension(20, 28));
		
		JLabel executiveFilterLabel = new JLabel("Executive:");
		filtersBar.add(executiveFilterLabel);
		
		filtersBar.addSeparator();
		
		executiveFilter = new JComboBox<Executive>();
		executiveFilter.addItemListener(executiveFilterChangeListener);
		filtersBar.add(executiveFilter);
		
		filtersBar.addSeparator();
		
		JLabel researchWorkerFilterLabel = new JLabel("Research Worker:");
		filtersBar.add(researchWorkerFilterLabel);
		
		filtersBar.addSeparator();
		
		researchWorkerFilter = new JComboBox<ResearchWorker>();
		researchWorkerFilter.addItemListener(researchWorkerFilterChangeListener);
		filtersBar.add(researchWorkerFilter);
		
		filtersBar.addSeparator();
		
		JLabel maxDurationYearsFilterLabel = new JLabel("Max Duration (Years):");
		filtersBar.add(maxDurationYearsFilterLabel);
		
		filtersBar.addSeparator();
		
		maxDurationYearsFilter = new JSpinner();
		maxDurationYearsFilter.setModel(new SpinnerNumberModel(4, 1, 4, 1));
		maxDurationYearsFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				try {
					refresh();
				} catch(SQLException | IOException ex) {}
			}
		});
		filtersBar.add(maxDurationYearsFilter);
		
		filtersBar.addSeparator();
		
		JLabel maxStartDateFilterLabel = new JLabel("Start Before:");
		filtersBar.add(maxStartDateFilterLabel);
		
		filtersBar.addSeparator();
		
		maxStartDateFilter = new JSpinner();
		try {
			maxStartDateFilter.setModel(new SpinnerDateModel(ERP.getSpinnerDateFormat().parse("01/01/2030"), null, null, Calendar.DAY_OF_YEAR));
		} catch(ParseException ex) {}
		maxStartDateFilter.setEditor(new JSpinner.DateEditor(maxStartDateFilter, ERP.getSpinnerDateFormat().toPattern()));
		maxStartDateFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				try {
					refresh();
				} catch(SQLException | IOException ex) {}
			}
		});
		filtersBar.add(maxStartDateFilter);
		
		filtersBar.addSeparator();
		
		JLabel minFinishDateFilterLabel = new JLabel("Finish After:");
		filtersBar.add(minFinishDateFilterLabel);
		
		filtersBar.addSeparator();
		
		minFinishDateFilter = new JSpinner();
		try {
			minFinishDateFilter.setModel(new SpinnerDateModel(ERP.getSpinnerDateFormat().parse("01/01/2000"), null, null, Calendar.DAY_OF_YEAR));
		} catch(ParseException ex) {}
		minFinishDateFilter.setEditor(new JSpinner.DateEditor(minFinishDateFilter, ERP.getSpinnerDateFormat().toPattern()));
		minFinishDateFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				try {
					refresh();
				} catch(SQLException | IOException ex) {}
			}
		});
		filtersBar.add(minFinishDateFilter);
	}
	
	public synchronized void refresh() throws IOException, SQLException {
		dashboard.setRefreshing(true);
		
		List<Executive> executives = Database.getExecutives();
		executives.add(0, new Executive(-1, "-"));
		executiveFilter.setModel(new DefaultComboBoxModel<Executive>(executives.toArray(new Executive[0])));
		executiveFilter.setRenderer(new ExecutiveCellRenderer());
		filterByExecutive(executiveFilterLastSelectedId);
		
		List<ResearchWorker> researchWorkers = Database.getResearchWorkers();
		researchWorkers.add(0, new ResearchWorker(-1, -1, "-", "", null, -1, null, null));
		researchWorkerFilter.setModel(new DefaultComboBoxModel<ResearchWorker>(researchWorkers.toArray(new ResearchWorker[0])));
		researchWorkerFilter.setRenderer(new ResearchWorkerCellRenderer());
		filterByResearchWorker(researchWorkerFilterLastSelectedId);
		
		list.removeAll();
		scrollPane.validate();
		
		ResearchWorker researchWorker = (ResearchWorker) researchWorkerFilter.getSelectedItem();
		
		DefaultListModel<Project> projectListModel = new DefaultListModel<>();
		
		List<Project> projects;
		if(Settings.getBoolean("miscellaneous.client-side-filtering")) projects = researchWorker.getId() > -1 ? Database.getResearchWorkerProjects(researchWorker.getId()) : Database.getProjects();
		else {
			Executive executive = (Executive) executiveFilter.getSelectedItem();
			
			String executiveId = executive.getId() > -1 ? "executive_id = " + executive.getId() : null;
			String maxDurationYears = "duration_years <= " + (int) maxDurationYearsFilter.getValue();
			String maxStartDate = "start_date < '" + Database.getSqlDateFormat().format((Date) maxStartDateFilter.getValue()) + "'";
			String minFinishDate = "finish_date > '" + Database.getSqlDateFormat().format((Date) minFinishDateFilter.getValue()) + "'";
			
			projects = researchWorker.getId() > -1 ? Database.getResearchWorkerProjects(researchWorker.getId(), executiveId, maxDurationYears, maxStartDate, minFinishDate) : Database.getProjects(executiveId, maxDurationYears, maxStartDate, minFinishDate);
		}
		
		for(Project project : projects) {
			projectListModel.addElement(project);
		}
		
		list.setModel(projectListModel);
		
		dashboard.setRefreshing(false);
		
		filter();
	}
	
	private void filter() {
		DefaultListModel<Project> newListModel = new DefaultListModel<>();
		
		for(int i = 0; i < list.getModel().getSize(); i++) {
			Project project = list.getModel().getElementAt(i);
			
			if(!isProjectFilteredOut(project)) newListModel.addElement(project);
		}
		
		list.setModel(newListModel);
		scrollPane.validate();
		
		dashboard.setResultCount(newListModel.getSize());
	}
	
	private boolean isProjectFilteredOut(Project project) {
		if(!searchField.getText().isBlank()) {
			Pattern pattern = Pattern.compile(Utils.removeDiacriticalMarks(searchField.getText()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			
			if(!pattern.matcher(Utils.removeDiacriticalMarks(project.getTitle())).find()) return true;
		}
		
		if(Settings.getBoolean("miscellaneous.client-side-filtering")) {
			Executive executive = (Executive) executiveFilter.getSelectedItem();
			
			if(executive.getId() > -1 && project.getExecutiveId() != executive.getId()) return true;
			if(project.getDurationYears() > (int) maxDurationYearsFilter.getValue()) return true;
			if(!project.getStartDate().before((Date) maxStartDateFilter.getValue())) return true;
			if(!project.getFinishDate().after((Date) minFinishDateFilter.getValue())) return true;
		}
		
		return false;
	}
	
	public void filterByExecutive(int executiveId) {
		for(int i = 0; i < executiveFilter.getModel().getSize(); i++) {
			Executive executive = (Executive) executiveFilter.getModel().getElementAt(i);
			
			if(executive.getId() == executiveId) executiveFilter.setSelectedIndex(i);
		}
	}
	
	public void filterByResearchWorker(int researchWorkerId) {
		for(int i = 0; i < researchWorkerFilter.getModel().getSize(); i++) {
			ResearchWorker researchWorker = (ResearchWorker) researchWorkerFilter.getModel().getElementAt(i);
			
			if(researchWorker.getId() == researchWorkerId) researchWorkerFilter.setSelectedIndex(i);
		}
	}
	
	private DocumentListener searchFieldChangeListener = new DocumentListener() {
		public void changedUpdate(DocumentEvent event) {
			try {
				refresh();
				filter();
			} catch(SQLException | IOException ex) {}
		}
		
		public void removeUpdate(DocumentEvent event) {
			try {
				refresh();
				filter();
			} catch(SQLException | IOException ex) {}
		}
		
		public void insertUpdate(DocumentEvent event) {
			filter();
		}
	};
	
	private static class ProjectCellRenderer extends JLabel implements ListCellRenderer<Project> {
		private static final long serialVersionUID = 1L;
		
		public ProjectCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Project> list, Project project, int index, boolean isSelected, boolean cellHasFocus) {
			String statusColorCode = project.getStatusCode() == 0 ? "#F58687" : (project.getStatusCode() == 1 ? "#A0E368" : (project.getStatusCode() == 2 ? "#53C6F2" : "black"));
			
			setText("<html><pre><font color='" + statusColorCode + "'>\u26ab</font> " + project.getTitle() + "</pre></html>");
			setBorder(BorderFactory.createRaisedBevelBorder());
			
			Color background;
			Color foreground;
			
			if(isSelected) {
				background = list.getSelectionBackground();
				foreground = list.getSelectionForeground();
			} else {
				background = list.getBackground();
				foreground = list.getForeground();
			}
			
			setBackground(background);
			setForeground(foreground);
			
			return this;
		}
	}
	
	private static class ExecutiveCellRenderer extends JLabel implements ListCellRenderer<Executive> {
		private static final long serialVersionUID = 1L;
		
		public ExecutiveCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Executive> list, Executive executive, int index, boolean isSelected, boolean cellHasFocus) {
			setText(executive.getName());
			
			Color background;
			Color foreground;
			
			if(isSelected) {
				background = list.getSelectionBackground();
				foreground = list.getSelectionForeground();
			} else {
				background = list.getBackground();
				foreground = list.getForeground();
			}
			
			setBackground(background);
			setForeground(foreground);
			
			return this;
		}
	}
	
	private static class ResearchWorkerCellRenderer extends JLabel implements ListCellRenderer<ResearchWorker> {
		private static final long serialVersionUID = 1L;
		
		public ResearchWorkerCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends ResearchWorker> list, ResearchWorker researchWorker, int index, boolean isSelected, boolean cellHasFocus) {
			setText(researchWorker.getFirstName() + " " + researchWorker.getLastName());
			
			Color background;
			Color foreground;
			
			if(isSelected) {
				background = list.getSelectionBackground();
				foreground = list.getSelectionForeground();
			} else {
				background = list.getBackground();
				foreground = list.getForeground();
			}
			
			setBackground(background);
			setForeground(foreground);
			
			return this;
		}
	}
	
}