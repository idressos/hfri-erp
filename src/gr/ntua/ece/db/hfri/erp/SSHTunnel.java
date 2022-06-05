package gr.ntua.ece.db.hfri.erp;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.InvalidPathException;

import java.nio.charset.StandardCharsets;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.JSchException;

public class SSHTunnel {
	
	private JSch jsch;
	private Session session;
	
	private String knownHostsFile;
	private List<Integer> forwardedLocalPorts = new ArrayList<Integer>();
	
	public SSHTunnel(String host, int port, String user, String knownHostsFile, boolean strictHostKeyChecking) throws JSchException {
		jsch = new JSch();
		jsch.setKnownHosts(this.knownHostsFile = knownHostsFile);
		
		session = jsch.getSession(user, host, port);
		if(!strictHostKeyChecking) session.setConfig("StrictHostKeyChecking", "no");
	}
	
	public void addIdentity(String keyFile, String passphrase) throws JSchException {
		if(passphrase == null) jsch.addIdentity(keyFile);
		else jsch.addIdentity(keyFile, passphrase);
	}
	
	public void setPassword(String password) {
		session.setPassword(password);
	}
	
	public String getHost() {
		return session.getHost();
	}
	
	public int getPort() {
		return session.getPort();
	}
	
	public void connect() throws JSchException {
		session.connect();
	}
	
	public void disconnect() {
		session.disconnect();
	}
	
	public boolean isConnected() {
		return session.isConnected();
	}
	
	public HostKey getHostKey() {
		return session.getHostKey();
	}
	
	public boolean knownHostsContains(HostKey hostKey) {
		try {
			String knownHosts = Files.readString(Paths.get(knownHostsFile), StandardCharsets.UTF_8);
			String hostKeyRegex = (hostKey.getHost() + " +" + hostKey.getType() + " +" + hostKey.getKey())
					 			.replaceAll("\\/", "\\\\\\/")
					 			.replaceAll("\\[", "\\\\\\[")
					 			.replaceAll("\\]", "\\\\\\]")
					 			.replaceAll("\\.", "\\\\\\.");
			
			return knownHosts.matches(hostKeyRegex);
		} catch(IOException | InvalidPathException ex) {
			return false;
		}
	}
	
	public void addToKnownHosts(HostKey hostKey) throws IOException {
		String line = hostKey.getHost() + " " + hostKey.getType() + " " + hostKey.getKey() + "\n";
		
		Files.writeString(Paths.get(knownHostsFile), line, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
	}
	
	public int forwardRandomLocalPort(String host, int remotePort) throws JSchException {
		int forwardedLocalPort = session.setPortForwardingL(0, host, remotePort);
		forwardedLocalPorts.add(forwardedLocalPort);
		
		return forwardedLocalPort;
	}
	
	public void removeLocalPortForwarding(int localPort) throws JSchException {
		session.delPortForwardingL(localPort);
	}
	
	public void removeAllLocalPortForwarding() throws JSchException {
		for(int forwardedLocalPort : forwardedLocalPorts) removeLocalPortForwarding(forwardedLocalPort);
	}
	
}