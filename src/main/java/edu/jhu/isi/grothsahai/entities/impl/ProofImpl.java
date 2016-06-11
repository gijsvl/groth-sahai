package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.entities.Proof;

public class ProofImpl implements Proof {
    private Vector c;
    private Vector d;
    private Vector pi;
    private Vector theta;

    public ProofImpl(final Vector c, final Vector d, final Vector pi, final Vector theta) {
        this.c = c;
        this.d = d;
        this.pi = pi;
        this.theta = theta;
    }

    public Vector getC() {
        return c;
    }

    public Vector getD() {
        return d;
    }

    public Vector getPi() {
        return pi;
    }

    public Vector getTheta() {
        return theta;
    }
}
