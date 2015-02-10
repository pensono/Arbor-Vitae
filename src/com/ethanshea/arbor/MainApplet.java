package com.ethanshea.arbor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainApplet extends JApplet {
    private ControlPanel control;
    private DrawPanel draw;
    private JPanel main;

    private void createGUI() {
	resize(700,500);
	main =new JPanel();
	main.setLayout(new BorderLayout());
	
	draw = new DrawPanel();
	draw.setBackground(Color.BLACK);
	draw.setPreferredSize(new Dimension(360,360));
	main.add(draw, BorderLayout.CENTER);	

	control = new ControlPanel(draw, new FractalGenerator(), new BranchedGenerator(), new FunctionGenerator());
	main.add(control, BorderLayout.EAST);
	
	add(main);
    }
    
    public void init() {
        //Execute a job on the event-dispatching thread:
        //creating this applet's GUI.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) { 
            System.err.println("createGUI didn't successfully complete");
        }
    }
}
