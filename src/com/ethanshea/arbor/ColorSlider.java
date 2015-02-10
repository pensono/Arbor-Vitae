package com.ethanshea.arbor;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JSlider;

public class ColorSlider extends JPanel {
    
    public ColorSlider(){
	JSlider hue = new JSlider();
    }
    
    private class HueSlider extends JSlider{
	public HueSlider(){ 
	    super();
	    setMaximum(255);
	}
	
	protected void paintComponent(Graphics g){
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setPaint(new GradientPaint(0,0, Color.RED, 200, 16, Color.GREEN));
	}
    }

}
