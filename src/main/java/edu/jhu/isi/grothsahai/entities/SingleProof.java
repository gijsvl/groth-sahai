package edu.jhu.isi.grothsahai.entities;

import java.util.Objects;

public class SingleProof {
    private Vector pi;
    private Vector theta;

    public SingleProof(final Vector pi, final Vector theta) {
        this.pi = pi;
        this.theta = theta;
    }

    public SingleProof() {
    }

    public Vector getPi() {
        return pi;
    }

    public Vector getTheta() {
        return theta;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SingleProof that = (SingleProof) o;
        return Objects.equals(pi, that.pi) &&
                Objects.equals(theta, that.theta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pi, theta);
    }

    @Override
    public String toString() {
        return "SingleProof{" +
                "pi=" + pi +
                ", theta=" + theta +
                '}';
    }
}
