package edu.jhu.isi.grothsahai.entities;

import java.util.ArrayList;
import java.util.List;

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

    public Vector getC() {
        return c;
    }

    public Vector getD() {
        return d;
    }

    public List<SingleProof> getProofs() {
        return new ArrayList<>(proofs);
    }
}
