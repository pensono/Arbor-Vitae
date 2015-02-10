package com.ethanshea.arbor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Random;

import com.ethanshea.arbor.Paramiter.Type;

public class FractalGenerator extends Generator {
    private ParamiterManager params;
    private double branchSpeed;

    public FractalGenerator() {
	params = new ParamiterManager();
	params.addParamiter("Background Color", Type.COLOR, Color.black);
	params.addParamiter("Start Color", Type.COLOR, Color.white);
	params.addParamiter("End Color", Type.COLOR, Color.DARK_GRAY);
	params.addParamiter("Size", Type.FLOAT, 100f, 0, 200);
	params.addParamiter("Levels", Type.INT, 5, 2, 12);
	params.addParamiter("Branches", Type.INT, 10, 1, 15);
	params.addParamiter("Angle Variance", Type.FLOAT, 50f, 0, 135);
	params.addParamiter("Stiffness", Type.FLOAT, 1f, 0, 10);
	params.addParamiter("Size Decay", Type.FLOAT, 65f, 0, 150);
	params.addParamiter("Size Decay Variance", Type.FLOAT, 25f, 0, 50);
	params.addParamiter("Thickness", Type.FLOAT, 4f, 0, 10);
	params.addParamiter("Thickness Decay", Type.FLOAT, 50f, 0, 150);
    }

    public Collection<Paramiter> getParamiters() {
	return params.getParamiters();
    }

    public void redraw(Graphics g, long seed, int width, int height) {
	Graphics2D g2 = (Graphics2D) g;
	// Build Color table
	Color[] colors = new Color[params.getInt("Levels")];
	for (int i = 0; i < params.getInt("Levels"); i++) {
	    colors[i] = lerpColor((Color) params.getObject("Start Color"), (Color) params.getObject("End Color"), i / (params.getInt("Levels") - 1f));
	}
	long startTime = System.nanoTime();
	g2.setColor((Color)params.getObject("Background Color"));
	g2.setBackground(null);
	g2.fillRect(0, 0, width, height);
	g2.setColor(colors[0]);
	g2.setStroke(new BasicStroke(params.getFloat("Thickness")));
    	drawLimb(g2, new Random(seed), colors, 0, width/2, height*19/20, 90, params.getFloat("Size"), params.getFloat("Thickness"));
    	long endTime = System.nanoTime();
    	branchSpeed = (endTime-startTime)/getNumBranches(params.getInt("Levels"),params.getInt("Branches"))*0.000000001;
    }

    public void drawLimb(Graphics2D g, Random rad, Color[] colors, int level, double x, double y, float angle, float size, float thickness) {
	double endX = x + (Math.cos(angle * Math.PI / 180)) * size;
	double endY = y - (Math.sin(angle * Math.PI / 180)) * size;
	g.drawLine((int) x, (int) y, (int) endX, (int) endY);

	if (level < params.getInt("Levels") - 1) {
	    float newThickness = (float) Math.max(0, thickness * params.getFloat("Thickness Decay") / 100);
	    for (int i = 0; i < params.getInt("Branches"); i++) {
		// If its the last level, don't re-set the values to what they already are.
		if (level != params.getInt("Levels")) {
		    g.setColor(colors[level + 1]);
		    g.setStroke(new BasicStroke(newThickness));
		}
		drawLimb(g, rad, colors, level + 1, endX, endY,
			(float) angle + (genRandomSignedPow(rad, params.getFloat("Stiffness")) * params.getFloat("Angle Variance")),
			(float) size * ((params.getFloat("Size Decay") + (genRandomSigned(rad) * params.getFloat("Size Decay Variance"))) / 100),
			(float) Math.max(0, thickness * params.getFloat("Thickness Decay") / 100));
	    }
	}
    }

    public String toString() {
	return "Fractal";
    }

    private float genRandomSigned(Random rad) {
	if (rad.nextBoolean()) {
	    return -rad.nextFloat();
	}
	return rad.nextFloat();
    }

    private float genRandomSignedPow(Random rad, float pow) {
	if (rad.nextBoolean()) {
	    return (float) -Math.pow(rad.nextFloat(), pow);
	}
	return (float) Math.pow(rad.nextFloat(), pow);
    }

    private Color lerpColor(Color a, Color b, float precent) {
	return new Color((int) (a.getRed() + ((b.getRed() - a.getRed()) * precent)), (int) (a.getGreen() + ((b.getGreen() - a.getGreen()) * precent)),
		(int) (a.getBlue() + ((b.getBlue() - a.getBlue()) * precent)));
    }

    public float getEstimatedDrawTime() {
	return (float) (branchSpeed*getNumBranches(params.getInt("Levels"),params.getInt("Branches")));
    }

    private int getNumBranches(int levels, int branches) {
	int answer = 0;
	for (int i=0;i<levels;i++){
	    answer+=Math.pow(branches,i);
	}
	return answer;
    }
}
