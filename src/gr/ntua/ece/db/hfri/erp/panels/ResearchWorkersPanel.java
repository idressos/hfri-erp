package gr.ntua.ece.db.hfri.erp.panels;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.JSpinner;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.DefaultComboBoxModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;

import java.io.IOException;

import java.sql.SQLException;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

import java.util.regex.Pattern;

import gr.ntua.ece.db.hfri.ERP;

import gr.ntua.ece.db.hfri.erp.Utils;
import gr.ntua.ece.db.hfri.erp.Database;
import gr.ntua.ece.db.hfri.erp.Settings;

import gr.ntua.ece.db.hfri.erp.frames.Dashboard;

import gr.ntua.ece.db.hfri.types.Project;
import gr.ntua.ece.db.hfri.types.ResearchWorker;

import gr.ntua.ece.swing.layouts.WrapLayout;

public class ResearchWorkersPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Dashboard dashboard;
	
	private JPanel panel;
	private JScrollPane scrollPane;
	
	private JTextField searchField;
	private JComboBox<Project> projectFilter;
	private JSpinner maxAgeFilter;
	private JSpinner maxJoinDateFilter;
	private JCheckBox maleFilter;
	private JCheckBox femaleFilter;
	
	private int projectFilterLastSelectedId = 0;
	
	private ItemListener projectFilterChangeListener = new ItemListener () {
		public void itemStateChanged(ItemEvent event) {
			if(event.getStateChange() == ItemEvent.SELECTED) {
				try {
					projectFilterLastSelectedId = ((Project) projectFilter.getSelectedItem()).getId();
					
					projectFilter.removeItemListener(projectFilterChangeListener);
					refresh();
					projectFilter.addItemListener(projectFilterChangeListener);
				} catch(SQLException | IOException ex) {}
			}
		}
	};
	
	public ResearchWorkersPanel(Dashboard dashboard) {
		this.dashboard = dashboard;
		
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setLayout(new WrapLayout(FlowLayout.LEADING, 35, 30));
		
		scrollPane = new JScrollPane(panel);
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
		searchField.setToolTipText("Search research workers by name");
		searchField.getDocument().addDocumentListener(searchFieldChangeListener);
		filtersBar.add(searchField);
		
		filtersBar.addSeparator(new Dimension(ERP.getScreenSize().width/6, 0));
		
		JLabel projectFilterLabel = new JLabel("Project:");
		filtersBar.add(projectFilterLabel);
		
		filtersBar.addSeparator();
		
		projectFilter = new JComboBox<Project>();
		projectFilter.addItemListener(projectFilterChangeListener);
		filtersBar.add(projectFilter);
		
		filtersBar.addSeparator();
		
		JLabel maxAgeFilterLabel = new JLabel("Max Age:");
		filtersBar.add(maxAgeFilterLabel);
		
		filtersBar.addSeparator();
		
		maxAgeFilter = new JSpinner();
		maxAgeFilter.setModel(new SpinnerNumberModel(90, 18, 90, 1));
		maxAgeFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				try {
					if(Settings.getBoolean("miscellaneous.client-side-filtering")) filter(); else refresh();
				} catch(SQLException | IOException ex) {}
			}
		});
		filtersBar.add(maxAgeFilter);
		
		filtersBar.addSeparator();
		
		JLabel maxJoinDateFilterLabel = new JLabel("Joined Before:");
		filtersBar.add(maxJoinDateFilterLabel);
		
		filtersBar.addSeparator();
		
		maxJoinDateFilter = new JSpinner();
		maxJoinDateFilter.setModel(new SpinnerDateModel(new Date(new Date().getTime() + 86400000), null, null, Calendar.DAY_OF_YEAR));
		maxJoinDateFilter.setEditor(new JSpinner.DateEditor(maxJoinDateFilter, ERP.getSpinnerDateFormat().toPattern()));
		maxJoinDateFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				try {
					if(Settings.getBoolean("miscellaneous.client-side-filtering")) filter(); else refresh();
				} catch(SQLException | IOException ex) {}
			}
		});
		filtersBar.add(maxJoinDateFilter);
		
		filtersBar.addSeparator();
		
		maleFilter = new JCheckBox("Male");
		maleFilter.setToolTipText("Show male research workers");
		maleFilter.setSelected(true);
		maleFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				try {
					if(!maleFilter.isSelected()) femaleFilter.setEnabled(false);
					else femaleFilter.setEnabled(true);
					
					if(Settings.getBoolean("miscellaneous.client-side-filtering")) filter(); else refresh();
				} catch(SQLException | IOException ex) {}
			}
		});
		filtersBar.add(maleFilter);
		
		femaleFilter = new JCheckBox("Female");
		femaleFilter.setToolTipText("Show female research workers");
		femaleFilter.setSelected(true);
		femaleFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				try {
					if(!femaleFilter.isSelected()) maleFilter.setEnabled(false);
					else maleFilter.setEnabled(true);
					
					if(Settings.getBoolean("miscellaneous.client-side-filtering")) filter(); else refresh();
				} catch(SQLException | IOException ex) {}
			}
		});
		filtersBar.add(femaleFilter);
	}
	
	public synchronized void refresh() throws IOException, SQLException {
		dashboard.setRefreshing(true);
		
		List<Project> projects = Database.getProjects();
		projects.add(0, new Project(-1, "-", null, null, null, -1, -1, -1, -1));
		projectFilter.setModel(new DefaultComboBoxModel<Project>(projects.toArray(new Project[0])));
		projectFilter.setRenderer(new ProjectCellRenderer());
		filterByProject(projectFilterLastSelectedId);
		
		panel.removeAll();
		scrollPane.validate();
		
		Project project = (Project) projectFilter.getSelectedItem();
		
		List<ResearchWorker> researchWorkers;
		if(Settings.getBoolean("miscellaneous.client-side-filtering")) researchWorkers = project.getId() > -1 ? Database.getProjectResearchWorkers(project.getId()) : Database.getResearchWorkers();
		else {
			String maxAge = "age <= " + (int) maxAgeFilter.getValue();
			String maxJoinDate = "join_date < '" + Database.getSqlDateFormat().format((Date) maxJoinDateFilter.getValue()) + "'";
			String sex = (maleFilter.isSelected() && femaleFilter.isSelected() ? null : (
						  maleFilter.isSelected() ? "sex = 'MALE'" : (
						  femaleFilter.isSelected() ? "sex = 'FEMALE'" : null)));
			
			researchWorkers = project.getId() > -1 ? Database.getProjectResearchWorkers(project.getId(), maxAge, maxJoinDate, sex) : Database.getResearchWorkers(maxAge, maxJoinDate, sex);
		}
		
		for(ResearchWorker researchWorker : researchWorkers) {
			panel.add(new ResearchWorkerComponent(researchWorker, dashboard));
		}
		
		dashboard.setRefreshing(false);
		
		filter();
	}
	
	private void filter() {
		int count = 0;
		int filtered = 0;
		
		for(Component component : panel.getComponents()) {
			if(component instanceof ResearchWorkerComponent) {
				ResearchWorkerComponent researchWorkerComponent = (ResearchWorkerComponent) component;
				count++;
				
				if(isComponentFilteredOut(researchWorkerComponent)) { researchWorkerComponent.setVisible(false); filtered++; }
				else researchWorkerComponent.setVisible(true);
			}
		}
		
		dashboard.setResultCount(count - filtered);
		scrollPane.validate();
	}
	
	private boolean isComponentFilteredOut(ResearchWorkerComponent researchWorkerComponent) {
		if(!searchField.getText().isBlank()) {
			Pattern pattern = Pattern.compile(Utils.removeDiacriticalMarks(searchField.getText()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			
			if(!pattern.matcher(Utils.removeDiacriticalMarks(researchWorkerComponent.getText())).find()) return true;
		}
		
		if(Settings.getBoolean("miscellaneous.client-side-filtering")) {
			if(researchWorkerComponent.getResearchWorker().getAge() > (int) maxAgeFilter.getValue()) return true;
			if(!researchWorkerComponent.getResearchWorker().getJoinDate().before((Date) maxJoinDateFilter.getValue())) return true;
			if(researchWorkerComponent.getResearchWorker().getSex() == ResearchWorker.Sex.MALE && !maleFilter.isSelected()) return true;
			if(researchWorkerComponent.getResearchWorker().getSex() == ResearchWorker.Sex.FEMALE && !femaleFilter.isSelected()) return true;
		}
		
		return false;
	}
	
	public void filterByProject(int projectId) {
		for(int i = 0; i < projectFilter.getModel().getSize(); i++) {
			Project project = (Project) projectFilter.getModel().getElementAt(i);
			
			if(project.getId() == projectId) projectFilter.setSelectedIndex(i);
		}
	}
	
	private DocumentListener searchFieldChangeListener = new DocumentListener() {
		public void changedUpdate(DocumentEvent event) {
			filter();
		}
		
		public void removeUpdate(DocumentEvent event) {
			filter();
		}
		
		public void insertUpdate(DocumentEvent event) {
			filter();
		}
	};
	
	private static class ResearchWorkerComponent extends JLabel {
		private static final long serialVersionUID = 1L;
		
		private ResearchWorker researchWorker;
		
		public ResearchWorkerComponent(ResearchWorker researchWorker, Dashboard dashboard) throws IOException {
			this.researchWorker = researchWorker;
			
			setText(researchWorker.getFirstName() + " " + researchWorker.getLastName());
			
			setIcon(new ImageIcon(Utils.getResearchWorkerAvatar(researchWorker, dashboard.getSize().width)));
			setHorizontalTextPosition(SwingConstants.CENTER);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			JPopupMenu popupMenu = new JPopupMenu();
			JMenuItem popupMenuLabel = new JMenuItem("<html><center>" + researchWorker.getFirstName() + " " + researchWorker.getLastName() + "</center></html>");
			popupMenuLabel.setEnabled(false);
			popupMenu.add(popupMenuLabel);
			popupMenu.addSeparator();
			JMenuItem viewOrganisationMenuButton = new JMenuItem("View Organisation");
			viewOrganisationMenuButton.setActionCommand("ORG " + researchWorker.getOrganisationId());
			viewOrganisationMenuButton.addActionListener(dashboard);
			popupMenu.add(viewOrganisationMenuButton);
			JMenuItem viewProjectsMenuButton = new JMenuItem("View Projects");
			viewProjectsMenuButton.setActionCommand("RW_PROJ " + researchWorker.getId());
			viewProjectsMenuButton.addActionListener(dashboard);
			popupMenu.add(viewProjectsMenuButton);
			setComponentPopupMenu(popupMenu);
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					if(event.getButton() == 1) {
						dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "RW " + researchWorker.getId()));
					}
				}
				
				public void mouseEntered(MouseEvent event) {
					setForeground(new Color(103, 124, 138));
				}
				
				public void mouseExited(MouseEvent event) {
					setForeground(null);
				}
			});
		}
		
		public ResearchWorker getResearchWorker() {
			return researchWorker;
		}
	}
	
	private static class ProjectCellRenderer extends JLabel implements ListCellRenderer<Project> {
		private static final long serialVersionUID = 1L;
		
		public ProjectCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Project> list, Project project, int index, boolean isSelected, boolean cellHasFocus) {
			setText(project.getTitle());
			
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