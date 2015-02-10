package com.ethanshea.arbor;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
	JFrame frame = new JFrame("Arbor Vitae");
	MainApplet applet= new MainApplet();
	applet.init();
	frame.add(applet);
	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(800,600);
	frame.setVisible(true);
    }
}
