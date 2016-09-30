package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.entities.Statement;
import it.unisa.dia.gas.jpbc.Element;

import java.util.Objects;

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

    public StatementImpl() {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StatementImpl statement = (StatementImpl) o;
        return Objects.equals(a, statement.a) &&
                Objects.equals(b, statement.b) &&
                Objects.equals(gamma, statement.gamma) &&
                Objects.equals(t, statement.t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, gamma, t);
    }

    @Override
    public String toString() {
        return "StatementImpl{" +
                "a=" + a +
                ", b=" + b +
                ", gamma=" + gamma +
                ", t=" + t +
                '}';
    }
}
