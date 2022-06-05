package gr.ntua.ece.db.hfri;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JOptionPane;

import java.io.IOException;

import java.text.SimpleDateFormat;

import org.json.JSONException;

import com.jcraft.jsch.JSchException;

import gr.ntua.ece.db.hfri.erp.Settings;
import gr.ntua.ece.db.hfri.erp.Database;

import gr.ntua.ece.db.hfri.erp.frames.LoginFrame;

public class ERP {
	
	private static Runtime runtime = Runtime.getRuntime();
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	private static String applicationName = "Hellenic Foundation for Research & Innovation";
	private static Image applicationIcon = toolkit.getImage(ERP.class.getResource("/img/icon.png"));
	
	private static Dimension screenSize = toolkit.getScreenSize();
	
	private static SimpleDateFormat spinnerDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public static void main(String[] args) {
		if(Desktop.isDesktopSupported()) {
			try {
				if(!Settings.exists()) Settings.exportDefault();
				
				if(Settings.isReadable()) Settings.load();
				else {
					JOptionPane.showMessageDialog(null, "Cannot read settings.json file! Please check file/folder permissions.", getApplicationName(), JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
			} catch(JSONException ex) {
				JOptionPane.showMessageDialog(null, "Cannot parse settings.json file! Please repair/delete it and restart the program.", getApplicationName(), JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} catch(IOException ex) {
				JOptionPane.showMessageDialog(null, "Cannot open settings.json file! Please check file/folder permissions.", getApplicationName(), JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			
			runtime.addShutdownHook(new Thread() {
				public void run() {
					if(Database.getSshTunnel() != null && Database.getSshTunnel().isConnected()) {
						try {
							Database.getSshTunnel().removeAllLocalPortForwarding();
						} catch(JSchException ex) {}
						
						Database.getSshTunnel().disconnect();
					}
				}
			});
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					LoginFrame loginFrame = new LoginFrame();
					
					loginFrame.setVisible(true);
				}
			});
		} else {
			System.err.println("Error: Desktop is not supported");
			System.exit(1);
		}
	}
	
	public static String getApplicationName() {
		return applicationName;
	}
	
	public static Image getApplicationIcon() {
		return applicationIcon;
	}
	
	public static Dimension getScreenSize() {
		return screenSize;
	}
	
	public static SimpleDateFormat getSpinnerDateFormat() {
		return spinnerDateFormat;
	}
	
	public static void beep() {
		toolkit.beep();
	}
	
}