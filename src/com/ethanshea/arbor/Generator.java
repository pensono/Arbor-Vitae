package com.ethanshea.arbor;

import java.awt.Graphics;
import java.util.Collection;
import java.util.Random;

public abstract class Generator {
    public abstract Collection<Paramiter> getParamiters();
    public abstract void redraw(Graphics g, long seed, int width, int height);
    public abstract String toString();
    public abstract float getEstimatedDrawTime();
}
