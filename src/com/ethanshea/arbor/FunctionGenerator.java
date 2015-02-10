package com.ethanshea.arbor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;

import com.ethanshea.arbor.Paramiter.Type;

public class FunctionGenerator extends Generator {
    private ParamiterManager params;
    private float lastXScale=50f,lastYScale=50f;

    public FunctionGenerator() {
	params = new ParamiterManager();
	params.addParamiter("x scale", Type.FLOAT, 10f, 1, 50);
	params.addParamiter("y scale", Type.FLOAT, 10f, 1, 50);
	params.addParamiter("x^0", Type.FLOAT, 0f, -10, 10);
	params.addParamiter("x^1", Type.FLOAT, 0f, -10, 10);
	params.addParamiter("x^2", Type.FLOAT, 0f, -10, 10);
	params.addParamiter("x^3", Type.FLOAT, 0f, -10, 10);
	params.addParamiter("x^4", Type.FLOAT, 0f, -10, 10);
	params.addParamiter("x^5", Type.FLOAT, 0f, -10, 10);
    }

    public Collection<Paramiter> getParamiters() {
	return params.getParamiters();
    }

    public void redraw(Graphics g, long seed, int width, int height) {
	// Draw axis lines
	g.setColor(Color.RED);
	int midHeight = height / 2;
	int midWidth = width / 2;
	g.drawLine(0, midHeight, width, midHeight);
	g.drawLine(midWidth, 0, midWidth, height);

	float unitPerPixelX = params.getFloat("x scale") / midWidth;
	float unitPerPixelY = midHeight/-params.getFloat("y scale");
	
	//Draw tick marks
	//X axis
	int xMultiplier = 1;
	if (params.getFloat("x scale")>50){
	    xMultiplier = 10;
	}
	for (float x=0;x<midWidth;x+=xMultiplier*midWidth/params.getFloat("x scale")){
	    g.drawLine(midWidth+(int)x, midHeight-1, midWidth+(int)x, midHeight+1);
	    g.drawLine(midWidth+(int)-x, midHeight-1, midWidth+(int)-x, midHeight+1);
	}
	g.drawString(""+(int)params.getFloat("x scale"), width-25, midHeight+2);
	
	//Y axis
	int yMultiplier = 1;
	if (params.getFloat("y scale")>50){
	    yMultiplier = 10;
	}
	for (float y=0;y<midHeight;y+=yMultiplier*250/params.getFloat("y scale")){
	    g.drawLine(midWidth-1,midHeight+(int)y, midWidth+1,midHeight+(int)y);
	    g.drawLine(midWidth-1,midHeight+(int)-y, midWidth+1, midHeight+(int)-y);
	}
	g.drawString(""+(int)params.getFloat("y scale"), midWidth+2, 10);

	// Draw the line
	g.setColor(Color.BLUE);
	int prevX = -midWidth -1;
	double prevY=0;
	for (int px = -midWidth; px < midWidth; px++) {
	    float gx = px * unitPerPixelX;
	    double gy = Math.pow(gx,5) * params.getFloat("x^5") + Math.pow(gx,4) * params.getFloat("x^4") + Math.pow(gx,3) * params.getFloat("x^3") + Math.pow(gx,2) * params.getFloat("x^2") + gx
		    * params.getFloat("x^1") + params.getFloat("x^0");
	    g.drawLine(px + midWidth, (int)(unitPerPixelY*gy)+midHeight, prevX + midWidth, (int)(unitPerPixelY*prevY)+midHeight);
	    prevX = px;
	    prevY = gy;
	}
    }

    public String toString() {
	return "Function";
    }

    public float getEstimatedDrawTime() {
	return 0;
    }

}
