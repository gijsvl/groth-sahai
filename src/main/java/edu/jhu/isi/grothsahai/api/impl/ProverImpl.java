package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.Matrix;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.SingleProof;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.Vector;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;

import java.util.ArrayList;
import java.util.List;

public class ProverImpl implements Prover {
    private CommonReferenceStringImpl crsImpl;

    public ProverImpl(final CommonReferenceString crs) {
        this.crsImpl = (CommonReferenceStringImpl) crs;
    }

    public Proof proof(final List<Statement> statements, final Witness witness) {
        final WitnessImpl witnessImpl = (WitnessImpl) witness;

        final Matrix R = Matrix.random(crsImpl.getZr(), witnessImpl.getX().getLength(), 2);
        final Matrix S = Matrix.random(crsImpl.getZr(), witnessImpl.getY().getLength(), 2);
        final Vector c = R != null ? crsImpl.iota(1, witnessImpl.getX()).add(R.multiply(crsImpl.getU1())) : null;
        final Vector d = S != null ? crsImpl.iota(2, witnessImpl.getY()).add(S.multiply(crsImpl.getU2())) : null;

        final ArrayList<SingleProof> proofs = new ArrayList<>();
        for (final Statement statement : statements) {
            proofs.add(getSingleProof((StatementImpl) statement, witnessImpl, R, S));
        }
        return new ProofImpl(c, d, proofs);
    }

    private SingleProof getSingleProof(final StatementImpl statementImpl, final WitnessImpl witnessImpl, final Matrix R, final Matrix S) {
        final Matrix T = Matrix.random(crsImpl.getZr(), 2, 2);
        Vector pi;
        if (R != null) {
            pi = R.getTranspose().multiply(crsImpl.iota(2, statementImpl.getB()));
            if (statementImpl.getGamma() != null) {
                pi = pi.add(R.getTranspose().multiply(statementImpl.getGamma()).multiply(crsImpl.iota(2, witnessImpl.getY())))
                        .add(R.getTranspose().multiply(statementImpl.getGamma()).multiply(S).multiply(crsImpl.getU2()));
            }
        } else {
            pi = Vector.getQuadraticNullVector(crsImpl.getB2(), crsImpl.getPairing(), 2);
        }
        pi = pi.sub(T.getTranspose().multiply(crsImpl.getU2()));


        Vector theta;
        if (S != null) {
            theta = S.getTranspose().multiply(crsImpl.iota(1, statementImpl.getA()));
            if (statementImpl.getGamma() != null) {
                theta = theta.add(S.getTranspose().multiply(statementImpl.getGamma().getTranspose())
                        .multiply(crsImpl.iota(1, witnessImpl.getX())));
            }
        } else {
            theta = Vector.getQuadraticNullVector(crsImpl.getB1(), crsImpl.getPairing(), 2);
        }
        theta = theta.add(T.multiply(crsImpl.getU1()));

        return new SingleProof(pi, theta);
    }
}
