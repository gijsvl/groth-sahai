package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.entities.Witness;

public class WitnessImpl implements Witness {
    private Vector x;
    private Vector y;

    public WitnessImpl(final Vector x, final Vector y) {
        this.x = x;
        this.y = y;
    }

    public Vector getX() {
        return x;
    }

    public Vector getY() {
        return y;
    }
}
