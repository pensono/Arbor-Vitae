package com.ethanshea.arbor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel {
    private static final long serialVersionUID = -986222644453098218L;
    private static float FLOAT_PRECISION = 100f;
    private JComboBox generator;
    private JSpinner seed;
    private JPanel paramiters;
    private JButton redraw; 
    private DrawPanel draw;
    private Generator selected;
    private HashMap<Paramiter, JComponent> componentMapping;    
    private boolean blockRedraw;

    public ControlPanel(final DrawPanel draw, Generator... generators) {
	blockRedraw=false;
	this.draw = draw;
	componentMapping = new HashMap<Paramiter, JComponent>();
	selected = generators[0];
	setLayout(new BorderLayout());
	setPreferredSize(new Dimension(200, 500));

	JPanel north = new JPanel();
	north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));

	JPanel seedPanel = new JPanel(new BorderLayout());
	seed = new JSpinner();
	seed.setValue(1);

	seed.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		draw.setSeed((Integer) seed.getValue());
		sendRedraw();
	    }
	});

	seedPanel.add(seed);

	JButton newSeed = new JButton("New");
	newSeed.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		Random rad = new Random();
		int numSeed = (int) rad.nextInt();
		seed.setValue(numSeed);
		draw.setSeed(numSeed);
		sendRedraw();
	    }
	});
	seedPanel.add(newSeed, BorderLayout.EAST);

	JLabel seedLabel = new JLabel("Seed: ");
	seedLabel.setLabelFor(seed);
	seedPanel.add(seedLabel, BorderLayout.WEST);

	north.add(seedPanel);

	generator = new JComboBox(generators);
	generator.setEditable(false);
	draw.setGenerator(selected);
	generator.addItemListener(new ItemListener() {

	    public void itemStateChanged(ItemEvent ie) {
		selected = (Generator) ie.getItem();
		resetParamiterList();
		draw.setGenerator(selected);
		sendRedraw();
	    }

	});

	north.add(generator);
	add(north, BorderLayout.NORTH);
	
	paramiters = new JPanel();
	paramiters.setLayout(new BoxLayout(paramiters, BoxLayout.Y_AXIS));
	JScrollPane para = new JScrollPane(paramiters);
	para.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	para.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	add(para);
	resetParamiterList();
	
	JPanel south = new JPanel();
	//south.setLayout(new BoxLayout(south, BoxLayout.X_AXIS));
	south.setLayout(new GridLayout(2,1));
	
	redraw = new JButton("Redraw");
	redraw.setEnabled(false);
	redraw.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		draw.repaint();
	    }
	});
	south.add(redraw);

	JButton randomize = new JButton("Randomize");
	randomize.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		System.out.println("Randomize!");
		blockRedraw=true;
		for (Paramiter p:componentMapping.keySet()){
		    p.randomize();
		    switch (p.getType()){
		    case INT:
			((JSlider)componentMapping.get(p)).setValue((Integer) p.getValue());
			break;
		    case FLOAT:
			((JSlider)componentMapping.get(p)).setValue((int) (((Float)p.getValue())*FLOAT_PRECISION));
			break;
		    case BOOLEAN:
			((JCheckBox)componentMapping.get(p)).setSelected((Boolean) p.getValue());
			break;
		    case COLOR:
			((JButton)componentMapping.get(p)).setIcon(new ColorIcon((Color) p.getValue()));
			break;
		    }
		}
		blockRedraw=false;
		sendRedraw();
	    }
	});
	south.add(randomize);
	
	add(south,BorderLayout.SOUTH);
    }

    public void resetParamiterList() {
	paramiters.removeAll();
	componentMapping.clear();
	for (final Paramiter p : selected.getParamiters()) {
	    if (p.getType().hasLabel()) {
		final JLabel label = new JLabel(p.getName());
		label.setName(p.getName());
		label.setAlignmentX(0);
		paramiters.add(label);
		switch (p.getType()) {
		case INT:
		    final JSlider slider = new JSlider(((Float) p.getMin()).intValue(), ((Float) p.getMax()).intValue(), (Integer) p.getValue());
		    slider.setSnapToTicks(true);
		    slider.setPreferredSize(new Dimension(50,16));
		    label.setText(label.getText() + ": " + p.getValue());
		    slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			    label.setText(label.getName() + ": " + slider.getValue());
			    p.setValue(slider.getValue());
			    sendRedraw();
			}
		    });
		    paramiters.add(slider);
		    componentMapping.put(p,slider);
		    break;
		case FLOAT:
		    final JSlider floatSlider = new JSlider((int) (p.getMin() * FLOAT_PRECISION), (int) (p.getMax() * FLOAT_PRECISION),
			    (int) (((Float) p.getValue()).floatValue() * FLOAT_PRECISION));
		    label.setText(label.getText() + ": " + p.getValue());
		    floatSlider.setPreferredSize(new Dimension(50,16));
		    floatSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			    label.setText(label.getName() + ": " + floatSlider.getValue() / FLOAT_PRECISION);
			    p.setValue(new Float(floatSlider.getValue() / FLOAT_PRECISION));
			    sendRedraw();
			}
		    });
		    paramiters.add(floatSlider);
		    componentMapping.put(p,floatSlider);
		    break;
		case STRING:
		    label.setText(label.getText() + ":");
		    JTextField text = new JTextField((String) p.getValue());
		    paramiters.add(text);
		    break;
		}
	    } else {
		switch (p.getType()) {
		case BOOLEAN:
		    final JCheckBox box = new JCheckBox(p.getName(), (Boolean) p.getValue());
		    box.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			    p.setValue(box.isSelected());
			    sendRedraw();
			}
		    });
		    paramiters.add(box);
		    componentMapping.put(p,box);
		    break;
		case COLOR:
		    final ColorIcon icon = new ColorIcon();
		    icon.setColor((Color) p.getValue());
		    final JButton color = new JButton(p.getName(), icon);
		    color.setHorizontalTextPosition(SwingConstants.LEADING);
		    color.setIconTextGap(10);
		    color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    Color newColor = JColorChooser.showDialog(null, p.getName(), icon.getColor());
			    if (newColor!=null){
				color.setIcon(new ColorIcon(newColor));
				p.setValue(newColor);
				sendRedraw();
			    }
			}
		    });
		    paramiters.add(color);
		    componentMapping.put(p,color);
		    break;
		}
	    }
	}
    }

    public Generator getSelected() {
	return selected;
    }

    public void sendRedraw() {	
	if (blockRedraw) return;
	if (selected.getEstimatedDrawTime()<.2f){
	    draw.repaint();
	    redraw.setEnabled(false);
	    redraw.setText("Redraw");
	}else{
	    redraw.setEnabled(true);
	    redraw.setText(String.format("Redraw: (%.2fs)",selected.getEstimatedDrawTime()));
	}
    }
}
