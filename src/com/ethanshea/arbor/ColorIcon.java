package com.ethanshea.arbor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class ColorIcon implements Icon {
    private Color color; 
    
    public ColorIcon(){
	color=Color.WHITE;
    }

    public ColorIcon(Color value) {
	this();
	color = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getIconHeight() {
	return 16;
    }

    public int getIconWidth() {
	return 16;
    }

    public void paintIcon(Component comp, Graphics g, int x, int y) {
	g.setColor(color);
	g.fillRect(x, y, 16,16);
    }

}
