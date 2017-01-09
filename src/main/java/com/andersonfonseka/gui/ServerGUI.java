package com.andersonfonseka.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.andersonfonseka.monitor.Message;
import com.andersonfonseka.monitor.ProgressMonitor;
import com.andersonfonseka.monitor.ProgressNotifier;
import com.andersonfonseka.parsers.WADLParser;
import com.andersonfonseka.service.Resource;
import com.andersonfonseka.util.RecursiveFiles;

public class ServerGUI extends BaseGUI implements ProgressNotifier {

	private static final long serialVersionUID = 1L;
	
	Server server;
	
	public static String GLOBAL_WADL_PATH = System.getProperty("user.dir");
	public static String GLOBAL_REAL_HOST = "";
	public static boolean GLOBAL_FORWARD_MODE = true;
	
	JTextField txJsonPath = new JTextField("", 60);
	JTextField txPortNumber = new JTextField("3000", 4);
	JTextField txRouterName = new JTextField("/", 60);
	JCheckBox chForwardMode = new JCheckBox();
	
	JTextField txRealHost = new JTextField("https://reqres.in/", 60); //http://li996/api/
	
	JPanel panelMonitor = new JPanel();
	JLabel requestGET = new JLabel("");
	JLabel requestPOST = new JLabel("");
	JLabel requestPUT = new JLabel("");
	JLabel requestDELETE = new JLabel("");
	
	JTextArea lbStatus = new JTextArea(25, 60);
	
	ProgressMonitor progressMonitor = new ProgressMonitor();
	
	private JButton btGenerate;
	private JButton btStop;
		
	public ServerGUI() throws Exception {
		super(propertiesUtil.getLabel("APP_NAME"), 800, 600);
		setResizable(false);
		
		getjPanelSelection().add(new JLabel(getPropertiesUtil().getLabel("PROJECT_NAME")));
		getjPanelSelection().add(txRouterName);

		final JButton btProjectSourceWeb = new JButton("...");
		
		btProjectSourceWeb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
							 fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							 fileChooser.showOpenDialog(null);
							 txRouterName.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});
		
		btProjectSourceWeb.setVisible(false);
		getjPanelSelection().add(btProjectSourceWeb);
		
		getjPanelSelection().add(new JLabel(getPropertiesUtil().getLabel("REVISION_NUMBER")));
		getjPanelSelection().add(txPortNumber);
		
		final JButton btProjectSourcePub = new JButton("...");
		
		btProjectSourcePub.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
							 fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							 fileChooser.showOpenDialog(null);
							 txPortNumber.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});
		
		btProjectSourcePub.setVisible(false);
		getjPanelSelection().add(btProjectSourcePub);
		
//		getjPanelSelection().add(new JLabel(getPropertiesUtil().getLabel("PROJECT_PATH")));
		txJsonPath.setEditable(false);
//		getjPanelSelection().add(txJsonPath);
		
		final JButton btProjectSource = new JButton("...");
		
		btProjectSource.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
							 fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
							 fileChooser.showOpenDialog(null);
							 txJsonPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
							 ServerGUI.GLOBAL_WADL_PATH = fileChooser.getSelectedFile().getAbsolutePath();
			}
		});
		
//		getjPanelSelection().add(btProjectSource);
		
		getjPanelSelection().add(new JLabel(getPropertiesUtil().getLabel("REAL_SERVER_HOST")));
		getjPanelSelection().add(txRealHost);
		chForwardMode.setSelected(true);
		chForwardMode.setText("Forward Mode");
		getjPanelSelection().add(chForwardMode);
		
		getjPanelSelection().add(new JLabel("Request monitor"));
		
		requestGET.setText(String.valueOf(0));
		requestGET.setPreferredSize(new Dimension(100, 20));
		requestGET.setOpaque(true);
		requestGET.setBackground(Color.GREEN);
		
		requestPOST.setText(String.valueOf(0));
		requestPOST.setPreferredSize(new Dimension(100, 20));
		requestPOST.setOpaque(true);
		requestPOST.setBackground(Color.ORANGE);
		
		requestPUT.setText(String.valueOf(0));
		requestPUT.setPreferredSize(new Dimension(100, 20));
		requestPUT.setOpaque(true);
		requestPUT.setBackground(Color.CYAN);
		
		requestDELETE.setText(String.valueOf(0));
		requestDELETE.setPreferredSize(new Dimension(100, 20));
		requestDELETE.setOpaque(true);
		requestDELETE.setBackground(Color.RED);
		
		panelMonitor.add(requestGET);
		panelMonitor.add(requestPOST);
		panelMonitor.add(requestPUT);
		panelMonitor.add(requestDELETE);
		
		getjPanelSelection().add(panelMonitor);

		getjPanelSelection().add(new JLabel());


		getjPanelSelection().add(new JLabel("Execution logs"));
		JScrollPane jScrollPane = new JScrollPane(lbStatus);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		jScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    }); 
		
		getjPanelSelection().add(jScrollPane);
		
		
		btGenerate = new JButton(getPropertiesUtil().getLabel("APPLY"));
		btStop = new JButton(getPropertiesUtil().getLabel("STOP"));
		
		getjPanelButtons().add(btGenerate);
		getjPanelButtons().add(btStop);
		
		btGenerate.addActionListener(new btnGenerateAction());
		btStop.addActionListener(new btnStopServer());
		btStop.setEnabled(false);
		
		getjPanelButtons().add(btnCancelar());
		
		getjPanel().add(getjPanelSelection());
		getjPanel().add(getjPanelButtons());
		add(getjPanel());
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		progressMonitor.addProgressNotifier(this);
		
	}
	
	class btnGenerateAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			Runnable runnable = new Runnable() {
				
				public void run() {

					Thread thread = new Thread(progressMonitor);

					if (!progressMonitor.isStarted()){
						thread.start();
						progressMonitor.setStarted(true);
					}
					
					btGenerate.setEnabled(false);
					progressMonitor.addMessage(new Message(Message.GENERIC,"Starting server..."));
					
					ServerGUI.GLOBAL_REAL_HOST = txRealHost.getText();
					ServerGUI.GLOBAL_FORWARD_MODE = chForwardMode.isSelected();

//					JsonPath.setFilePath(txJsonPath.getText());
//					
//					File file = new File(txJsonPath.getText());
//					RecursiveFiles recursiveFiles = new RecursiveFiles();
//					recursiveFiles.checkFiles(file, ".wadl");
//					List<String> result = recursiveFiles.getPaths();
//					
					Resource.setProgressMonitor(progressMonitor);
//					
//					for (String string : result) {
//						try {
//							Resource.setServices(new WADLParser(progressMonitor).parse(string));
//							progressMonitor.addMessage(new Message(Message.GENERIC, "checking..." + string));
//						} catch (Exception e1) {
//							progressMonitor.addMessage(new Message(Message.GENERIC, e1.getMessage()));
//						}
//					}

					int port = Integer.valueOf(txPortNumber.getText());
					server = new Server(port);
					
					ResourceConfig config = new ResourceConfig();
					config.packages("com.andersonfonseka.service");
					
					ServletHolder servlet = new ServletHolder(new ServletContainer(config));
					
					ServletContextHandler servletContextHandler = new ServletContextHandler(server,  txRouterName.getText() + "/*");
					servletContextHandler.addServlet(servlet, "/*");

					progressMonitor.addMessage(new Message(Message.GENERIC, "Server started on port " + port));
					btStop.setEnabled(true);
					
					try {
						server.start();
						server.join();
					} catch (Exception e) {
						progressMonitor.addMessage(new Message(Message.GENERIC, e.getMessage()));
					}
				}
			};

			Thread tMetrics = new Thread(runnable);
			tMetrics.start();
		}

	}
	
	class btnStopServer implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			try {
				server.stop();
				progressMonitor.addMessage(new Message(Message.GENERIC, "Server stopped now"));
				btGenerate.setEnabled(true);
				btStop.setEnabled(false);
			} catch (Exception e1) {
				progressMonitor.addMessage(new Message(Message.GENERIC, e1.getMessage()));
			}
		}
	}

	public JTextField getTxProjectSource() {
		return txJsonPath;
	}

	public JTextField getTxProjectSourcePublish() {
		return txPortNumber;
	}
	
	public JTextField getTxProjectSourceWeb() {
		return txRouterName;
	}

	public void updateDisplay(Message message) {
		
		if (message.getType().equals(Message.GET)){
			int value = Integer.valueOf(requestGET.getText());
			requestGET.setText(String.valueOf(++value));	
		}
		
		if (message.getType().equals(Message.POST)){
			int value = Integer.valueOf(requestPOST.getText());
			requestPOST.setText(String.valueOf(++value));	
		}

		if (message.getType().equals(Message.PUT)){
			int value = Integer.valueOf(requestPUT.getText());
			requestPUT.setText(String.valueOf(++value));	
		}

		if (message.getType().equals(Message.DELETE)){
			int value = Integer.valueOf(requestDELETE.getText());
			requestDELETE.setText(String.valueOf(++value));	
		}

		if (message.getType().equals(Message.GENERIC)){
			lbStatus.setText(lbStatus.getText() + "\n" + message.getContent()); 
			lbStatus.repaint();
		}
		
		getjPanelSelection().repaint();
		this.repaint();

	}
	
}
