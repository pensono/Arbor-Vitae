package com.ethanshea.arbor;

import java.awt.Graphics;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
    private Generator gen;
    private long seed=1;
    
    public void paintComponent(Graphics g){
	super.paintComponent(g);
	gen.redraw(g,seed, getWidth(), getHeight());
    }

    public Generator getGenerator() {
	return gen;
    }

    public void setGenerator(Generator gen) {
	this.gen = gen;
    }
    
    public void setSeed(long seed){
	this.seed = seed;
    }
}
