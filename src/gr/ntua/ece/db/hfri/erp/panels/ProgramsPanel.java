package gr.ntua.ece.db.hfri.erp.panels;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import javax.swing.event.DocumentEvent;
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

import java.io.IOException;

import java.sql.SQLException;

import java.util.regex.Pattern;

import gr.ntua.ece.db.hfri.ERP;

import gr.ntua.ece.db.hfri.erp.Utils;
import gr.ntua.ece.db.hfri.erp.Database;

import gr.ntua.ece.db.hfri.types.Program;

import gr.ntua.ece.db.hfri.erp.frames.Dashboard;

public class ProgramsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Dashboard dashboard;
	
	private JList<Program> list;
	private JScrollPane scrollPane;
	
	private JTextField searchField;
	
	public ProgramsPanel(Dashboard dashboard) {
		this.dashboard = dashboard;
		
		setLayout(new BorderLayout(0, 0));
		
		list = new JList<>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new ProgramCellRenderer());
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				int index  = list.locationToIndex(event.getPoint());
				
				if(index > -1) {
					Program program = list.getModel().getElementAt(index);
					
					if(event.getButton() == 1 && event.getClickCount() == 1) {
						dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "PROG " + program.getId()));
					} else if(event.getButton() == 3 && event.getClickCount() == 1) {
						JPopupMenu popupMenu = new JPopupMenu();
						
						JMenuItem popupMenuLabel = new JMenuItem("<html><center>" + program.getName() + "</center></html>");
						popupMenuLabel.setEnabled(false);
						popupMenu.add(popupMenuLabel);
						popupMenu.addSeparator();
						JMenuItem viewAddressMenuButton = new JMenuItem("View Address");
						viewAddressMenuButton.setActionCommand("ADDR " + program.getAddressId());
						viewAddressMenuButton.addActionListener(dashboard);
						popupMenu.add(viewAddressMenuButton);
						
						popupMenu.show(list, event.getX(), event.getY());
					}
				}
			}
		});
		list.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					Program program = list.getSelectedValue();
					
					if(program != null) dashboard.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_LAST, "PROG " + program.getId()));
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
		searchField.setToolTipText("Search programs by name");
		searchField.getDocument().addDocumentListener(searchFieldChangeListener);
		filtersBar.add(searchField);
		
		filtersBar.addSeparator(new Dimension(ERP.getScreenSize().width / 2, 28));
	}
	
	public synchronized void refresh() throws IOException, SQLException {
		dashboard.setRefreshing(true);
		
		list.removeAll();
		scrollPane.validate();
		
		DefaultListModel<Program> programListModel = new DefaultListModel<>();
		
		for(Program program : Database.getPrograms()) {
			programListModel.addElement(program);
		}
		
		list.setModel(programListModel);
		
		dashboard.setRefreshing(false);
		
		filter();
	}
	
	private void filter() {
		DefaultListModel<Program> newListModel = new DefaultListModel<>();
		
		for(int i = 0; i < list.getModel().getSize(); i++) {
			Program program = list.getModel().getElementAt(i);
			
			if(!isProgramFilteredOut(program)) newListModel.addElement(program);
		}
		
		list.setModel(newListModel);
		scrollPane.validate();
		
		dashboard.setResultCount(newListModel.getSize());
	}
	
	private boolean isProgramFilteredOut(Program program) {
		if(!searchField.getText().isBlank()) {
			Pattern pattern = Pattern.compile(Utils.removeDiacriticalMarks(searchField.getText()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			
			if(!pattern.matcher(Utils.removeDiacriticalMarks(program.getName())).find()) return true;
		}
		
		return false;
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
	
	private static class ProgramCellRenderer extends JLabel implements ListCellRenderer<Program> {
		private static final long serialVersionUID = 1L;
		
		public ProgramCellRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Program> list, Program program, int index, boolean isSelected, boolean cellHasFocus) {
			setText("<html><pre>" + program.getName() + "</pre></html>");
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
	
}