package edu.jhu.isi.grothsahai.entities.impl;

public class SingleProof {
    private Vector pi;
    private Vector theta;

    public SingleProof(final Vector pi, final Vector theta) {
        this.pi = pi;
        this.theta = theta;
    }

    public Vector getPi() {
        return pi;
    }

    public Vector getTheta() {
        return theta;
    }
}
