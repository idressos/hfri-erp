package gr.ntua.ece.db.hfri.erp.panels;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import java.sql.SQLException;

import gr.ntua.ece.db.hfri.erp.Database;

import gr.ntua.ece.db.hfri.erp.frames.Dashboard;

import gr.ntua.ece.db.hfri.types.Executive;
import gr.ntua.ece.db.hfri.types.ResearchWorker;

public class HomePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Dashboard dashboard;
	
	private JList<Object[]> topExecutivesByFundingsList;
	private JList<Object[]> mostActiveYoungResearchWorkersList;
	private JList<Object[]> researchWorkersInProjectsWithoutCommissionsList;
	
	public HomePanel(Dashboard dashboard) {
		this.dashboard = dashboard;
		
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 20, 20);
		layout.setAlignOnBaseline(true);
		
		setLayout(layout);
		
		{ // Top Executives by Fundings Panel
			ResultsPanel topExecutivesByFundingsPanel = new ResultsPanel();
			topExecutivesByFundingsPanel.setLayout(new BorderLayout(0, 0));
			add(topExecutivesByFundingsPanel);
			
			JLabel topExecutivesByFundingsLabel = new JLabel("<html><center><h3>Top 5 Executives by<br>Total Funded To Organisations</h3></center></html>");
			topExecutivesByFundingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
			topExecutivesByFundingsPanel.add(topExecutivesByFundingsLabel, BorderLayout.NORTH);
			
			topExecutivesByFundingsList = new JList<Object[]>();
			topExecutivesByFundingsList.setBorder(BorderFactory.createRaisedSoftBevelBorder());
			topExecutivesByFundingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			topExecutivesByFundingsList.setCellRenderer(new TopExecutivesByFundingsCellRenderer());
			topExecutivesByFundingsList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					int index  = topExecutivesByFundingsList.locationToIndex(event.getPoint());
					
					if(index > -1) {
						Executive executive = (Executive) topExecutivesByFundingsList.getModel().getElementAt(index)[0];
						
						if(event.getButton() == 3 && event.getClickCount() == 1) {
							JPopupMenu popupMenu = new JPopupMenu();
							
							JMenuItem popupMenuLabel = new JMenuItem("<html><center>" + executive.getName() + "</center></html>");
							popupMenuLabel.setEnabled(false);
							popupMenu.add(popupMenuLabel);
							popupMenu.addSeparator();
							JMenuItem viewProjectsMenuButton = new JMenuItem("View Projects");
							viewProjectsMenuButton.setActionCommand("EXEC_PROJ " + executive.getId());
							viewProjectsMenuButton.addActionListener(dashboard);
							popupMenu.add(viewProjectsMenuButton);
							
							popupMenu.show(topExecutivesByFundingsList, event.getX(), event.getY());
						}
					}
				}
			});
			topExecutivesByFundingsList.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent event) {
					if(event.getKeyCode() == KeyEvent.VK_ENTER) {
						Executive executive = (Executive) topExecutivesByFundingsList.getSelectedValue()[0];
						
						if(executive != null) dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "EXEC " + executive.getId()));
					}
				}
			});
			topExecutivesByFundingsList.setBackground(getBackground());
			topExecutivesByFundingsPanel.add(topExecutivesByFundingsList, BorderLayout.CENTER);
		}
		
		{ // Most Active Young Research Workers Panel
			ResultsPanel mostActiveYoungResearchWorkersPanel = new ResultsPanel();
			mostActiveYoungResearchWorkersPanel.setLayout(new BorderLayout(0, 0));
			add(mostActiveYoungResearchWorkersPanel);
			
			JLabel mostActiveYoungResearchWorkersLabel = new JLabel("<html><center><h3>Research Workers Aged Less Than 40<br>in Most Active Projects</h3></center></html>");
			mostActiveYoungResearchWorkersLabel.setHorizontalAlignment(SwingConstants.CENTER);
			mostActiveYoungResearchWorkersPanel.add(mostActiveYoungResearchWorkersLabel, BorderLayout.NORTH);
			
			mostActiveYoungResearchWorkersList = new JList<Object[]>();
			mostActiveYoungResearchWorkersList.setBorder(BorderFactory.createRaisedSoftBevelBorder());
			mostActiveYoungResearchWorkersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			mostActiveYoungResearchWorkersList.setCellRenderer(new MostActiveYoungResearchWorkersCellRenderer());
			mostActiveYoungResearchWorkersList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					int index  = mostActiveYoungResearchWorkersList.locationToIndex(event.getPoint());
					
					if(index > -1) {
						ResearchWorker researchWorker = (ResearchWorker) mostActiveYoungResearchWorkersList.getModel().getElementAt(index)[0];
						
						if(event.getButton() == 3 && event.getClickCount() == 1) {
							JPopupMenu popupMenu = new JPopupMenu();
							
							JMenuItem popupMenuLabel = new JMenuItem("<html><center>" + researchWorker.getFirstName() + " " + researchWorker.getLastName() + "</center></html>");
							popupMenuLabel.setEnabled(false);
							popupMenu.add(popupMenuLabel);
							popupMenu.addSeparator();
							JMenuItem viewProjectsMenuButton = new JMenuItem("View Projects");
							viewProjectsMenuButton.setActionCommand("RW_PROJ " + researchWorker.getId());
							viewProjectsMenuButton.addActionListener(dashboard);
							popupMenu.add(viewProjectsMenuButton);
							
							popupMenu.show(mostActiveYoungResearchWorkersList, event.getX(), event.getY());
						}
					}
				}
			});
			mostActiveYoungResearchWorkersList.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent event) {
					if(event.getKeyCode() == KeyEvent.VK_ENTER) {
						ResearchWorker researchWorker = (ResearchWorker) mostActiveYoungResearchWorkersList.getSelectedValue()[0];
						
						if(researchWorker != null) dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "RW " + researchWorker.getId()));
					}
				}
			});
			mostActiveYoungResearchWorkersList.setBackground(getBackground());
			mostActiveYoungResearchWorkersPanel.add(mostActiveYoungResearchWorkersList, BorderLayout.CENTER);
		}
		
		{ // Research Workers In Projects Without Commissions
			ResultsPanel researchWorkersInProjectsWithoutCommissionsPanel = new ResultsPanel();
			researchWorkersInProjectsWithoutCommissionsPanel.setLayout(new BorderLayout(0, 0));
			add(researchWorkersInProjectsWithoutCommissionsPanel);
			
			JLabel researchWorkersInProjectsWithoutCommissionsLabel = new JLabel("<html><center><h3>Research Workers In 2 Or More Projects<br>Without Commissions</h3></center></html>");
			researchWorkersInProjectsWithoutCommissionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
			researchWorkersInProjectsWithoutCommissionsPanel.add(researchWorkersInProjectsWithoutCommissionsLabel, BorderLayout.NORTH);
			
			researchWorkersInProjectsWithoutCommissionsList = new JList<Object[]>();
			researchWorkersInProjectsWithoutCommissionsList.setBorder(BorderFactory.createRaisedSoftBevelBorder());
			researchWorkersInProjectsWithoutCommissionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			researchWorkersInProjectsWithoutCommissionsList.setCellRenderer(new ResearchWorkersInProjectsWithoutCommissionsCellRenderer());
			researchWorkersInProjectsWithoutCommissionsList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					int index  = researchWorkersInProjectsWithoutCommissionsList.locationToIndex(event.getPoint());
					
					if(index > -1) {
						ResearchWorker researchWorker = (ResearchWorker) researchWorkersInProjectsWithoutCommissionsList.getModel().getElementAt(index)[0];
						
						if(event.getButton() == 3 && event.getClickCount() == 1) {
							JPopupMenu popupMenu = new JPopupMenu();
							
							JMenuItem popupMenuLabel = new JMenuItem("<html><center>" + researchWorker.getFirstName() + " " + researchWorker.getLastName() + "</center></html>");
							popupMenuLabel.setEnabled(false);
							popupMenu.add(popupMenuLabel);
							popupMenu.addSeparator();
							JMenuItem viewProjectsMenuButton = new JMenuItem("View Projects");
							viewProjectsMenuButton.setActionCommand("RW_PROJ " + researchWorker.getId());
							viewProjectsMenuButton.addActionListener(dashboard);
							popupMenu.add(viewProjectsMenuButton);
							
							popupMenu.show(researchWorkersInProjectsWithoutCommissionsList, event.getX(), event.getY());
						}
					}
				}
			});
			researchWorkersInProjectsWithoutCommissionsList.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent event) {
					if(event.getKeyCode() == KeyEvent.VK_ENTER) {
						ResearchWorker researchWorker = (ResearchWorker) researchWorkersInProjectsWithoutCommissionsList.getSelectedValue()[0];
						
						if(researchWorker != null) dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "RW " + researchWorker.getId()));
					}
				}
			});
			researchWorkersInProjectsWithoutCommissionsList.setBackground(getBackground());
			researchWorkersInProjectsWithoutCommissionsPanel.add(researchWorkersInProjectsWithoutCommissionsList, BorderLayout.CENTER);
		}
	}
	
	public void refresh() throws SQLException {
		dashboard.setRefreshing(true);
		
		{
			topExecutivesByFundingsList.removeAll();
			
			DefaultListModel<Object[]> topExecutivesByFundingsListModel = new DefaultListModel<>();
			
			for(Object[] array : Database.getTopExecutivesByFundings()) {
				topExecutivesByFundingsListModel.addElement(array);
			}
			
			topExecutivesByFundingsList.setModel(topExecutivesByFundingsListModel);
		}
		
		{
			mostActiveYoungResearchWorkersList.removeAll();
			
			DefaultListModel<Object[]> mostActiveYoungResearchWorkersListModel = new DefaultListModel<>();
			
			for(Object[] array : Database.getMostActiveYoungResearchWorkers()) {
				mostActiveYoungResearchWorkersListModel.addElement(array);
			}
			
			mostActiveYoungResearchWorkersList.setModel(mostActiveYoungResearchWorkersListModel);
		}
		
		{
			researchWorkersInProjectsWithoutCommissionsList.removeAll();
			
			DefaultListModel<Object[]> researchWorkersInProjectsWithoutCommissionsListModel = new DefaultListModel<>();
			
			for(Object[] array : Database.getResearchWorkersInProjectsWithoutCommissions()) {
				researchWorkersInProjectsWithoutCommissionsListModel.addElement(array);
			}
			
			researchWorkersInProjectsWithoutCommissionsList.setModel(researchWorkersInProjectsWithoutCommissionsListModel);
		}
		
		validate();
		
		dashboard.setRefreshing(false);
		
		dashboard.setResultCount(-1);
	}
	
	private static boolean validateTopExecutivesByFundingsArray(Object[] array) {
		if(array.length != 3) return false;
		if(array[0] == null || !(array[0] instanceof Executive)) return false;
		if(array[1] == null || !(array[1] instanceof String)) return false;
		if(array[2] == null || !(array[2] instanceof Integer)) return false;
		
		return true;
	}
	
	private static boolean validateMostActiveYoungResearchWorkersArray(Object[] array) {
		if(array.length != 2) return false;
		if(array[0] == null || !(array[0] instanceof ResearchWorker)) return false;
		if(array[1] == null || !(array[1] instanceof Integer)) return false;
		
		return true;
	}
	
	private static boolean validateResearchWorkersInProjectsWithoutCommissionsArray(Object[] array) {
		if(array.length != 2) return false;
		if(array[0] == null || !(array[0] instanceof ResearchWorker)) return false;
		if(array[1] == null || !(array[1] instanceof Integer)) return false;
		
		return true;
	}
	
	private static class TopExecutivesByFundingsCellRenderer extends JLabel implements ListCellRenderer<Object[]> {
		private static final long serialVersionUID = 1L;
		
		public TopExecutivesByFundingsCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Object[]> list, Object[] array, int index, boolean isSelected, boolean cellHasFocus) {
			if(validateTopExecutivesByFundingsArray(array)) {
				Executive executive = (Executive) array[0];
				String organisationName = (String) array[1];
				int totalFunded = (int) array[2];
				
				setText(executive.getName() + " has funded " + organisationName + " $" + totalFunded + " in total");
			}
			
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
	
	private static class MostActiveYoungResearchWorkersCellRenderer extends JLabel implements ListCellRenderer<Object[]> {
		private static final long serialVersionUID = 1L;
		
		public MostActiveYoungResearchWorkersCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Object[]> list, Object[] array, int index, boolean isSelected, boolean cellHasFocus) {
			if(validateMostActiveYoungResearchWorkersArray(array)) {
				ResearchWorker researchWorker = (ResearchWorker) array[0];
				Integer activeProjectCount = (Integer) array[1];
				
				setText(researchWorker.getFirstName() + " " + researchWorker.getLastName() + ": " + activeProjectCount.toString() + " active project" + (activeProjectCount > 1 ? "s" : ""));
			}
			
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
	
	private static class ResearchWorkersInProjectsWithoutCommissionsCellRenderer extends JLabel implements ListCellRenderer<Object[]> {
		private static final long serialVersionUID = 1L;
		
		public ResearchWorkersInProjectsWithoutCommissionsCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Object[]> list, Object[] array, int index, boolean isSelected, boolean cellHasFocus) {
			if(validateResearchWorkersInProjectsWithoutCommissionsArray(array)) {
				ResearchWorker researchWorker = (ResearchWorker) array[0];
				Integer noCommissionProjectCount = (Integer) array[1];
				
				setText(researchWorker.getFirstName() + " " + researchWorker.getLastName() + " works in " + noCommissionProjectCount.toString() + " project" + (noCommissionProjectCount > 1 ? "s" : "") + " without commissions");
			}
			
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
	
	private static class ResultsPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
			return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
		}
		
		@Override
		public int getBaseline(int width, int height) {
			return 0;
		}
	}
	
}