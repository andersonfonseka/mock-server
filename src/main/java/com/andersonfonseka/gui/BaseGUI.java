package com.andersonfonseka.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.andersonfonseka.util.ImageUtil;
import com.andersonfonseka.util.PropertiesUtil;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class BaseGUI extends JFrame {
	
	static ImageUtil imageUtil = ImageUtil.getInstance();
	static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
	
	private JPanel jPanel = new JPanel();
	private JPanel jPanelSelection = new JPanel();
	private JPanel jPanelButtons = new JPanel();

	public BaseGUI(String title, int width, int height) {
		super(title);
		
		setSize(new Dimension(width, height));
		
		int top = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight())/4);
		int left = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth())/2);
		setLocation(left, top);
		
		MigLayout layout = new MigLayout("wrap 1");
		MigLayout layoutPanelSelection = new MigLayout("wrap 3");
		MigLayout layoutButtons = new MigLayout();
		
		jPanel.setLayout(layout);
		jPanelSelection.setLayout(layoutPanelSelection);
		jPanelButtons.setLayout(layoutButtons);
		
	}
	
	public static PropertiesUtil getPropertiesUtil() {
		return propertiesUtil;
	}

	public JPanel getjPanel() {
		return jPanel;
	}

	public JPanel getjPanelSelection() {
		return jPanelSelection;
	}

	public JPanel getjPanelButtons() {
		return jPanelButtons;
	}

	public JButton btnCancelar(){
		
		JButton btnCancelar = new JButton(getPropertiesUtil().getLabel("CANCEL"));
		btnCancelar.setName("btnCancelar");
		
		btnCancelar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		return btnCancelar; 
		
	}
	
}
