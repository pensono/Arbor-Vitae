package com.ethanshea.arbor;

import java.awt.Color;
import java.util.Random;

public class Paramiter {
    private Type type;
    private String name;
    private float max;
    private float min;
    private Object value;
    // Default
    private Object def;

    public Paramiter() {
	this.type = Type.FLOAT;
	this.name = "Paramiter";
	this.min = 0;
	this.max = 10;
	this.value = 0;
    }

    public Paramiter(Type type, String name, Object def) {
	this(type, name, def, -1, -1);
    }

    public Paramiter(Type type, String name, Object def, float min, float max) {
	this.type = type;
	this.name = name;
	this.min = min;
	this.max = max;
	value = def;
	this.def = def;
    }

    public Type getType() {
	return type;
    }

    public String getName() {
	return name;
    }

    public float getMax() {
	return max;
    }

    public float getMin() {
	return min;
    }

    public Object getValue() {
	return value;
    }

    public Object getDefaultValue() {
	return def;
    }

    public void setValue(Object value) {
	this.value = value;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setMax(int max) {
	this.max = max;
    }

    public void setMin(int min) {
	this.min = min;
    }

    public enum Type {
	INT(true), FLOAT(true), BOOLEAN(false), STRING(true), COLOR(false);
	
	private boolean hasLabel;
	
	Type(boolean label){
	    hasLabel=label;
	}

	public boolean hasLabel() {
	    return hasLabel;
	}
    }

    public void randomize() {
	switch (type) {
	case INT:
	    value = (int) (new Random().nextInt((int)(max - min)) + min);
	    break;
	case FLOAT:
	    value = new Random().nextFloat()*(max-min) + min;
	    break;
	case BOOLEAN:
	    value = new Random().nextBoolean();
	    break;
	case COLOR:
	    value = new Color(new Random().nextInt(16777216));
	}
    }

}
