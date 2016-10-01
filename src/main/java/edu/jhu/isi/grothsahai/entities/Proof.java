package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.json.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proof {
    private Vector c;
    private Vector d;
    private List<SingleProof> proofs = new ArrayList<>();

    public Proof(final Vector c, final Vector d, final List<SingleProof> proofs) {
        this.c = c;
        this.d = d;
        this.proofs = proofs;
    }

    public Proof() {
    }

    public String getAsJson(final CommonReferenceString crs) {
        return Serializer.serializeProof(this, crs);
    }

    public Vector getC() {
        return c;
    }

    public Vector getD() {
        return d;
    }

    public List<SingleProof> getProofs() {
        return new ArrayList<>(proofs);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Proof proof = (Proof) o;
        return Objects.equals(c, proof.c) &&
                Objects.equals(d, proof.d) &&
                Objects.equals(proofs, proof.proofs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, d, proofs);
    }

    @Override
    public String toString() {
        return "Proof{" +
                "c=" + c +
                ", d=" + d +
                ", proofs=" + proofs +
                '}';
    }
}
