package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.entities.Statement;
import it.unisa.dia.gas.jpbc.Element;

public class StatementImpl implements Statement {
    private Vector a;
    private Vector b;
    private Matrix gamma;
    private Element t;

    public StatementImpl(final Vector a, final Vector b, final Matrix gamma, final Element t) {
        this.a = a;
        this.b = b;
        this.gamma = gamma;
        this.t = t;
    }

    public Vector getA() {
        return a;
    }

    public Vector getB() {
        return b;
    }

    public Matrix getGamma() {
        return gamma;
    }

    public Element getT() {
        return t;
    }
}
