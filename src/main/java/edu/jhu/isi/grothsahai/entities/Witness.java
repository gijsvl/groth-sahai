package edu.jhu.isi.grothsahai.entities;

import java.util.Objects;

public class Witness {
    private Vector x;
    private Vector y;

    public Witness(final Vector x, final Vector y) {
        this.x = x;
        this.y = y;
    }

    public Vector getX() {
        return x;
    }

    public Vector getY() {
        return y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Witness witness = (Witness) o;
        return Objects.equals(x, witness.x) &&
                Objects.equals(y, witness.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "WitnessImpl{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
