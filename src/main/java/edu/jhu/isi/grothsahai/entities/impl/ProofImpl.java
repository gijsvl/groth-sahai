package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.entities.Proof;

import java.util.ArrayList;
import java.util.List;

public class ProofImpl implements Proof {
    private Vector c;
    private Vector d;
    private List<SingleProof> proofs = new ArrayList<>();

    public ProofImpl(final Vector c, final Vector d, final List<SingleProof> proofs) {
        this.c = c;
        this.d = d;
        this.proofs = proofs;
    }

    public ProofImpl() {
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
