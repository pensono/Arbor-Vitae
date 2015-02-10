package com.ethanshea.arbor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.ethanshea.arbor.Paramiter.Type;

public class BranchedGenerator extends Generator {
    ParamiterManager params;
    float branchSpeed;

    public BranchedGenerator() {
	params = new ParamiterManager();
	params.addParamiter("Starting Size", Type.FLOAT, 20f, 0, 50);
	// params.addParamiter("Color", Type.COLOR, Color.white);
	params.addParamiter("Max Levels", Type.INT, 80, 1, 100);
	params.addParamiter("Branch Chance", Type.FLOAT, 10f, 0, 50);
	params.addParamiter("Branch Chance Decay", Type.FLOAT, 60f, 0, 100);
	params.addParamiter("Fork Chance", Type.FLOAT, 10f, 0, 50);
	params.addParamiter("Fork Chance Decay", Type.FLOAT, 60f, 0, 100);
	params.addParamiter("Split Angle", Type.FLOAT, 30f, 0, 90);
	params.addParamiter("Split Angle Stiffness", Type.FLOAT, .2f, 0, 2);
	params.addParamiter("Angle Variance", Type.FLOAT, 10f, 0, 45);
	params.addParamiter("Size Decay", Type.FLOAT, 65f, 0, 150);
	params.addParamiter("Size Variance", Type.FLOAT, 25f, 0, 50);
	params.addParamiter("Thickness", Type.FLOAT, 4f, 0, 10);
	params.addParamiter("Thickness Decay", Type.FLOAT, 95f, 90, 100);
	params.addParamiter("Branch Thickness", Type.FLOAT, 80f, 0, 100);
	params.addParamiter("Fork Thickness", Type.FLOAT, 60f, 0, 100);
    }

    public Collection<Paramiter> getParamiters() {
	return params.getParamiters();
    }

    public void redraw(Graphics g, long seed, int width, int height) {
	Graphics2D g2 = (Graphics2D) g;
	g.setColor(new Color(128, 70, 27));
	g.setColor(new Color(255, 255, 255));
	
	drawLimb(g2, new Random(seed), 0, width/2, height*19/20, 90, params.getFloat("Starting Size"), params.getFloat("Thickness"), params.getFloat("Branch Chance") / 100,
		params.getFloat("Fork Chance") / 100);
    }

    public void drawLimb(Graphics2D g, Random rad, int level, float x, float y, float angle, float size, float thickness, float branchChance, float forkChance) {
	if (level >= params.getInt("Max Levels")) {
	    return;
	}
	float endX = (float) (x + (Math.cos(angle * Math.PI / 180)) * size);
	float endY = (float) (y - (Math.sin(angle * Math.PI / 180)) * size);
	float angleOffset = 0;
	float newThickness = 1f;
	float rand = rad.nextFloat();
	if (rand < branchChance) {
	    // Do a branch
	    angleOffset = genRandomSignedPow(rad, params.getFloat("Split Angle Stiffness")) * params.getFloat("Split Angle");
	    drawLimb(g, rad, level + 1, endX, endY, (float) angle + angleOffset + (genRandomSigned(rad) * params.getFloat("Angle Variance")), (float) size
		    * ((params.getFloat("Size Decay") + (genRandomSigned(rad) * params.getFloat("Size Variance"))) / 100),
		    Math.max(0, (float) thickness * params.getFloat("Branch Thickness") * params.getFloat("Thickness Decay") / 10000),
		    branchChance * params.getFloat("Branch Chance Decay") / 100, forkChance);
	    angleOffset = 0;
	} else if (rand < branchChance + forkChance) {
	    // Fork
	    angleOffset = genRandomSignedPow(rad, params.getFloat("Split Angle Stiffness")) * params.getFloat("Split Angle");
	    newThickness = params.getFloat("Fork Thickness") / 100;
	    drawLimb(g, rad, level + 1, endX, endY, (float) angle - angleOffset + (genRandomSigned(rad) * params.getFloat("Angle Variance")), (float) size
		    * ((params.getFloat("Size Decay") + (genRandomSigned(rad) * params.getFloat("Size Variance"))) / 100),
		    Math.max(0, (float) thickness * newThickness * params.getFloat("Thickness Decay") / 100), branchChance,
		    params.getFloat("Fork Chance Decay") / 100 * forkChance);
	}
	drawLimb(g, rad, level + 1, endX, endY, (float) angleOffset + angle + (genRandomSigned(rad) * params.getFloat("Angle Variance")), (float) size
		* ((params.getFloat("Size Decay") + (genRandomSigned(rad) * params.getFloat("Size Variance"))) / 100),
		Math.max(0, (float) thickness * newThickness * params.getFloat("Thickness Decay") / 100), branchChance, forkChance);
	if (size > .5f) {
	    g.setStroke(new BasicStroke(thickness));
	    g.drawLine((int) x, (int) y, (int) endX, (int) endY);
	}
    }

    public String toString() {
	return "Branched";
    }

    public float genRandomSigned(Random rad) {
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

    public float getEstimatedDrawTime() {
	return 0;
    }

}
