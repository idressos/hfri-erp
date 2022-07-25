package gr.ntua.ece.db.hfri.erp.frames;

import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JProgressBar;

import javax.swing.border.EmptyBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

import com.jcraft.jsch.JSchException;

import gr.ntua.ece.db.hfri.ERP;

import gr.ntua.ece.db.hfri.erp.Database;

import gr.ntua.ece.db.hfri.erp.panels.*;

public class Dashboard extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	// Menu Items
	private JMenuItem refreshMenuItem;
	
	// Content Pane Items
	private JTabbedPane tabbedPane;
	private JProgressBar progressBar;
	
	// Tabs
	private HomePanel homeTab;
	private ProgramsPanel programsTab;
	private ProjectsPanel projectsTab;
	private ResearchWorkersPanel researchWorkersTab;
	
	// Toolbar Items
	private JTextField resultCountField;
	
	private List<JFrame> openFrames = new ArrayList<JFrame>();
	
	public Dashboard() {
		initialize();
		
		refresh(true);
	}
	
	private void initialize() {
		setTitle(ERP.getApplicationName());
		setIconImage(ERP.getApplicationIcon());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, ERP.getScreenSize().width * 15/16, ERP.getScreenSize().height * 15/16);
		setMinimumSize(new Dimension(ERP.getScreenSize().width * 15/16, ERP.getScreenSize().height * 15/16));
		setLocationRelativeTo(null);
		
		{ // Menu Bar
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			
			JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);
			
			refreshMenuItem = new JMenuItem("Refresh");
			refreshMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
			refreshMenuItem.addActionListener(this);
			fileMenu.add(refreshMenuItem);
			
			JMenuItem exitMenuItem = new JMenuItem("Exit");
			exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
			exitMenuItem.addActionListener(this);
			fileMenu.add(exitMenuItem);
			
			JMenu entityMenu = new JMenu("Entity");
			menuBar.add(entityMenu);
			
			JMenuItem newProjectMenuItem = new JMenuItem("New Project..");
			newProjectMenuItem.setActionCommand("PROJ -1");
			newProjectMenuItem.addActionListener(this);
			entityMenu.add(newProjectMenuItem);
			
			JMenuItem newAddressMenuItem = new JMenuItem("New Address..");
			newAddressMenuItem.setActionCommand("ADDR -1");
			newAddressMenuItem.addActionListener(this);
			entityMenu.add(newAddressMenuItem);
			
			JMenuItem newProgramMenuItem = new JMenuItem("New Program..");
			newProgramMenuItem.setActionCommand("PROG -1");
			newProgramMenuItem.addActionListener(this);
			entityMenu.add(newProgramMenuItem);
			
			JMenuItem newExecutiveMenuItem = new JMenuItem("New Executive..");
			newExecutiveMenuItem.setActionCommand("EXEC -1");
			newExecutiveMenuItem.addActionListener(this);
			entityMenu.add(newExecutiveMenuItem);
			
			JMenuItem newOrganisationMenuItem = new JMenuItem("New Organisation..");
			newOrganisationMenuItem.setActionCommand("ORG -1");
			newOrganisationMenuItem.addActionListener(this);
			entityMenu.add(newOrganisationMenuItem);
			
			JMenuItem newResearchWorkerMenuItem = new JMenuItem("New Research Worker..");
			newResearchWorkerMenuItem.setActionCommand("RW -1");
			newResearchWorkerMenuItem.addActionListener(this);
			entityMenu.add(newResearchWorkerMenuItem);
			
			JMenu connectionMenu = new JMenu("Connection");
			menuBar.add(connectionMenu);
			
			JMenuItem statusMenuItem = new JMenuItem("Status");
			statusMenuItem.addActionListener(this);
			connectionMenu.add(statusMenuItem);
			
			JMenuItem disconnectMenuItem = new JMenuItem("Disconnect");
			disconnectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
			disconnectMenuItem.addActionListener(this);
			connectionMenu.add(disconnectMenuItem);
			
			JMenu helpMenu = new JMenu("Help");
			menuBar.add(helpMenu);
			
			JMenuItem aboutMenuItem = new JMenuItem("About");
			aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
			aboutMenuItem.addActionListener(this);
			helpMenu.add(aboutMenuItem);
		}
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		{ // Home Tab
			homeTab = new HomePanel(this);
			tabbedPane.addTab("Home", null, homeTab, null);
		}
		
		{ // Programs Tab
			programsTab = new ProgramsPanel(this);
			tabbedPane.addTab("Programs", null, programsTab, null);
		}
		
		{ // Projects Tab
			projectsTab = new ProjectsPanel(this);
			tabbedPane.addTab("Projects", null, projectsTab, null);
		}
		
		{ // Research Workers Tab
			researchWorkersTab = new ResearchWorkersPanel(this);
			tabbedPane.addTab("Research Workers", null, researchWorkersTab, null);
		}
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				setResultCount(-1);
				refresh(false);
			}
		});
		
		{ // Toolbar
			JToolBar toolbar = new JToolBar();
			toolbar.setFloatable(false);
			toolbar.setBorder(new EmptyBorder(5, 0, 0, 0));
			contentPane.add(toolbar, BorderLayout.SOUTH);
			
			progressBar = new JProgressBar();
			toolbar.add(progressBar);
			
			toolbar.addSeparator(new Dimension(ERP.getScreenSize().width * 9/16, 0));
			
			JLabel resultCountLabel = new JLabel("Results:");
			toolbar.add(resultCountLabel);
			
			toolbar.addSeparator();
			
			resultCountField = new JTextField("-");
			resultCountField.setHorizontalAlignment(JTextField.CENTER);
			resultCountField.setEditable(false);
			resultCountField.setColumns(5);
			resultCountField.setMaximumSize(new Dimension((int) resultCountField.getMinimumSize().getWidth(), (int) resultCountField.getMaximumSize().getHeight()));
			toolbar.add(resultCountField);
		}
	}
	
	private void refresh(boolean refreshAll) {
		new Thread(new Runnable() {
			public void run() {
				setRefreshing(true);
				
				try {
					if(tabbedPane.getSelectedIndex() == 0 || refreshAll) homeTab.refresh();
					if(tabbedPane.getSelectedIndex() == 1 || refreshAll) programsTab.refresh();
					if(tabbedPane.getSelectedIndex() == 2 || refreshAll) projectsTab.refresh();
					if(tabbedPane.getSelectedIndex() == 3 || refreshAll) researchWorkersTab.refresh();
				} catch(IOException ex) {
					JOptionPane.showMessageDialog(Dashboard.this, "An I/O exception occured while refreshing.", "Error", JOptionPane.ERROR_MESSAGE);
				} catch(SQLException ex) {
					JOptionPane.showMessageDialog(Dashboard.this, "An SQL exception occured while refreshing.", "Error", JOptionPane.ERROR_MESSAGE);
				} finally {
					setRefreshing(false);
				}
			}
		}, "Refresh Task").start();
	}
	
	public synchronized void setRefreshing(boolean refreshing) {
		if(refreshing) {
			refreshMenuItem.setEnabled(false);
			progressBar.setIndeterminate(true);
		} else {
			progressBar.setIndeterminate(false);
			refreshMenuItem.setEnabled(true);
		}
	}
	
	public synchronized void setResultCount(int count) {
		if(count < 0) resultCountField.setText("-");
		else resultCountField.setText(String.valueOf(count));
	}
	
	private synchronized boolean requestFocusIfAlreadyOpen(JFrame frame) {
		List<JFrame> undisplayableFrames = new ArrayList<JFrame>();
		
		boolean found = false;
		
		for(JFrame openFrame : openFrames) {
			if(!openFrame.isDisplayable()) {
				undisplayableFrames.add(frame);
				continue;
			}
			
			if(openFrame.getClass().equals(frame.getClass())) {
				if(frame instanceof AddressFrame) {
					AddressFrame openAddressFrame = (AddressFrame) openFrame;
					AddressFrame addressFrame = (AddressFrame) frame;
					
					if(openAddressFrame.getAddressId() == addressFrame.getAddressId()) {
						openAddressFrame.requestFocus();
						
						found = true;
						break;
					}
				}
				if(frame instanceof ExecutiveFrame) {
					ExecutiveFrame openExecutiveFrame = (ExecutiveFrame) openFrame;
					ExecutiveFrame executiveFrame = (ExecutiveFrame) frame;
					
					if(openExecutiveFrame.getExecutiveId() == executiveFrame.getExecutiveId()) {
						openExecutiveFrame.requestFocus();
						
						found = true;
						break;
					}
				}
				if(frame instanceof OrganisationFrame) {
					OrganisationFrame openOrganisationFrame = (OrganisationFrame) openFrame;
					OrganisationFrame organisationFrame = (OrganisationFrame) frame;
					
					if(openOrganisationFrame.getOrganisationId() == organisationFrame.getOrganisationId()) {
						openOrganisationFrame.requestFocus();
						
						found = true;
						break;
					}
				}
				if(frame instanceof ProgramFrame) {
					ProgramFrame openProgramFrame = (ProgramFrame) openFrame;
					ProgramFrame programFrame = (ProgramFrame) frame;
					
					if(openProgramFrame.getProgramId() == programFrame.getProgramId()) {
						openProgramFrame.requestFocus();
						
						found = true;
						break;
					}
				}
				if(frame instanceof ProjectFrame) {
					ProjectFrame openProjectFrame = (ProjectFrame) openFrame;
					ProjectFrame projectFrame = (ProjectFrame) frame;
					
					if(openProjectFrame.getProjectId() == projectFrame.getProjectId()) {
						openProjectFrame.requestFocus();
						
						found = true;
						break;
					}
				}
				if(frame instanceof ResearchWorkerFrame) {
					ResearchWorkerFrame openResearchWorkerFrame = (ResearchWorkerFrame) openFrame;
					ResearchWorkerFrame researchWorkerFrame = (ResearchWorkerFrame) frame;
					
					if(openResearchWorkerFrame.getResearchWorkerId() == researchWorkerFrame.getResearchWorkerId()) {
						openResearchWorkerFrame.requestFocus();
						
						found = true;
						break;
					}
				}
			}
		}
		
		for(JFrame undisplayableFrame : undisplayableFrames) openFrames.remove(undisplayableFrame);
		
		return found;
	}
	
	@Override
    public void actionPerformed(ActionEvent actionEvent) {
		String[] command = actionEvent.getActionCommand().split(" ");
		
		if(command[0].equals("RW")) {
			try {
				ResearchWorkerFrame researchWorkerFrame = new ResearchWorkerFrame(Integer.parseInt(command[1]));
				
				if(!requestFocusIfAlreadyOpen(researchWorkerFrame)) {
					openFrames.add(researchWorkerFrame);
					
					researchWorkerFrame.setVisible(true);
				} else researchWorkerFrame.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(Dashboard.this, "An SQL exception occured while fetching research worker data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command[0].equals("ORG")) {
			try {
				OrganisationFrame organisationFrame = new OrganisationFrame(Integer.parseInt(command[1]));
				
				if(!requestFocusIfAlreadyOpen(organisationFrame)) {
					openFrames.add(organisationFrame);
					
					organisationFrame.setVisible(true);
				} else organisationFrame.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(Dashboard.this, "An SQL exception occured while fetching organisation data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command[0].equals("EXEC")) {
			try {
				ExecutiveFrame executiveFrame = new ExecutiveFrame(Integer.parseInt(command[1]));
				
				if(!requestFocusIfAlreadyOpen(executiveFrame)) {
					openFrames.add(executiveFrame);
					
					executiveFrame.setVisible(true);
				} else executiveFrame.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(Dashboard.this, "An SQL exception occured while fetching executive data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command[0].equals("PROG")) {
			try {
				ProgramFrame programFrame = new ProgramFrame(Integer.parseInt(command[1]));
				
				if(!requestFocusIfAlreadyOpen(programFrame)) {
					openFrames.add(programFrame);
					
					programFrame.setVisible(true);
				} else programFrame.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(Dashboard.this, "An SQL exception occured while fetching program data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command[0].equals("PROJ")) {
			try {
				ProjectFrame projectFrame = new ProjectFrame(Integer.parseInt(command[1]));
				
				if(!requestFocusIfAlreadyOpen(projectFrame)) {
					openFrames.add(projectFrame);
					
					projectFrame.setVisible(true);
				} else projectFrame.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(Dashboard.this, "An SQL exception occured while fetching project data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command[0].equals("ADDR")) {
			try {
				AddressFrame addressFrame = new AddressFrame(Integer.parseInt(command[1]));
				
				if(!requestFocusIfAlreadyOpen(addressFrame)) {
					openFrames.add(addressFrame);
					
					addressFrame.setVisible(true);
				} else addressFrame.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(Dashboard.this, "An SQL exception occured while fetching address data.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(command[0].equals("RW_PROJ")) {
			tabbedPane.setSelectedIndex(2);
			projectsTab.filterByResearchWorker(Integer.parseInt(command[1]));
		} else if(command[0].equals("EXEC_PROJ")) {
			tabbedPane.setSelectedIndex(2);
			projectsTab.filterByExecutive(Integer.parseInt(command[1]));
		} else if(command[0].equals("PROJ_RW")) {
			tabbedPane.setSelectedIndex(3);
			researchWorkersTab.filterByProject(Integer.parseInt(command[1]));
		} else if(command[0].equals("Refresh")) {
			refresh(false);
		} else if(command[0].equals("Exit")) {
			System.exit(0);
		} else if(command[0].equals("Status")) {
			boolean dbConnValid = false;
			boolean sshTunnel = false;
			
			{
				if(Database.getSshTunnel() != null && Database.getSshTunnel().isConnected()) sshTunnel = true;
				
				try {
					dbConnValid = Database.verifyConnection();
				} catch(SQLException ex) {}
			}
			
			JOptionPane.showMessageDialog(this,
					"<html>" +
							"<h3>SSH Tunnel</h3>" +
							"Status: " + (sshTunnel ? "<span style=\"color: #639435\">Connected</span>" : "Not Connected") + "<br>" +
							"Address: " + (sshTunnel ? Database.getSshTunnel().getHost() + ":" + Database.getSshTunnel().getPort() : "-") + "<br>" +
							"<h3>Database</h3>" +
							"Status: " + (dbConnValid ? "<span style=\"color: #639435\">Connected</span>" : "<span style=\"color: #D82423\">Disconnected</span>") + "<br>" +
					"</html>",
					"Connection Status", JOptionPane.INFORMATION_MESSAGE);
		} else if(command[0].equals("Disconnect")) {
			if(Database.getSshTunnel() != null && Database.getSshTunnel().isConnected()) {
				try {
					Database.getSshTunnel().removeAllLocalPortForwarding();
				} catch(JSchException ex) {}
				
				Database.getSshTunnel().disconnect();
			}
			dispose();
			
			LoginFrame loginFrame = new LoginFrame();
			loginFrame.setVisible(true);
		} else if(command[0].equals("About")) {
			JOptionPane.showMessageDialog(this,
				"<html>" +
						ERP.getApplicationName() + "<br>" +
						"Enterprise Resource Planning System<br><br>" +
						"National Technical University of Athens<br>" +
						"School of Electrical & Computer Engineering<br><br>" +
						"This application is part of the semester project for the <a href=\"https://helios.ntua.gr/course/view.php?id=861\">Relational Databases</a> course.<br>" +
						"Project Team: 109<br><br>" +
						"Created for academic purposes only.<br>" +
						"May 2022" +
				"</html>",
				"About", JOptionPane.INFORMATION_MESSAGE);
		}
    }
	
}