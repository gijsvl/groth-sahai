package edu.jhu.isi.grothsahai.entities.impl;

public class Pair<E, F> {
    private E left;
    private F right;

    public Pair(final E left, final F right) {
        this.left = left;
        this.right = right;
    }

    public E getLeft() {
        return left;
    }

    public F getRight() {
        return right;
    }
}
