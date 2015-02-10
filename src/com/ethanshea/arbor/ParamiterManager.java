package com.ethanshea.arbor;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.ethanshea.arbor.Paramiter.Type;
import com.ethanshea.arbor.Paramiter;

/**
 * Data structure class designed specifically for holding parameters.
 * @author Ethan Shea
 *
 */
public class ParamiterManager {
    private LinkedHashMap<String,Paramiter> params;
    
    public ParamiterManager(){
	params = new LinkedHashMap<String,Paramiter>();
    }
    
    public void addParamiter(String name, Type type,Object def, int min, int max){
	params.put(name, new Paramiter(type, name, def, min, max));
    }
    
    public void addParamiter(String name, Type type,Object def){
	params.put(name, new Paramiter(type, name, def));
    }
    
    public Collection<Paramiter> getParamiters(){
	return params.values();
    }
    
    public float getFloat(String key){
	return ((Float)params.get(key).getValue()).floatValue();
    }
    
    public int getInt(String key){
	return ((Integer)params.get(key).getValue()).intValue();
    }
    
    public String getString(String key){
	return (String)params.get(key).getValue();
    }
    
    public boolean getBoolean(String key){
	return ((Boolean)params.get(key).getValue()).booleanValue();
    }
    
    public Object getObject(String key){
	return params.get(key).getValue();
    }

    public Paramiter getParamiter(String key) {
	return params.get(key);
    }

}
