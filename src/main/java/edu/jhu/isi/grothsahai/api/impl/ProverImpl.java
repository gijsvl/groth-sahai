package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.SingleProof;
import edu.jhu.isi.grothsahai.entities.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProverImpl implements Prover {
    private CommonReferenceString crsImpl;

    public ProverImpl(final CommonReferenceString crs) {
        this.crsImpl = crs;
    }

    public Proof proof(final List<Statement> statements, final Witness witness) {
        final Matrix R = Matrix.random(crsImpl.getZr(), witness.getX().getLength(), 2);
        final Matrix S = Matrix.random(crsImpl.getZr(), witness.getY().getLength(), 2);
        final Vector c = R != null ? crsImpl.iota(1, witness.getX()).add(R.multiply(crsImpl.getU1())) : null;
        final Vector d = S != null ? crsImpl.iota(2, witness.getY()).add(S.multiply(crsImpl.getU2())) : null;

        final ArrayList<SingleProof> proofs = statements.stream().map(statement -> getSingleProof(statement, witness, R, S)).collect(Collectors.toCollection(ArrayList::new));
        return new Proof(c, d, proofs);
    }

    private SingleProof getSingleProof(final Statement statement, final Witness witness, final Matrix R, final Matrix S) {
        final Matrix T = Matrix.random(crsImpl.getZr(), 2, 2);
        Vector pi;
        if (R != null) {
            pi = R.getTranspose().multiply(crsImpl.iota(2, statement.getB()));
            if (statement.getGamma() != null) {
                pi = pi.add(R.getTranspose().multiply(statement.getGamma()).multiply(crsImpl.iota(2, witness.getY())))
                        .add(R.getTranspose().multiply(statement.getGamma()).multiply(S).multiply(crsImpl.getU2()));
            }
        } else {
            pi = Vector.getQuadraticNullVector(crsImpl.getB2(), crsImpl.getPairing(), 2);
        }
        pi = pi.sub(T.getTranspose().multiply(crsImpl.getU2()));


        Vector theta;
        if (S != null) {
            theta = S.getTranspose().multiply(crsImpl.iota(1, statement.getA()));
            if (statement.getGamma() != null) {
                theta = theta.add(S.getTranspose().multiply(statement.getGamma().getTranspose())
                        .multiply(crsImpl.iota(1, witness.getX())));
            }
        } else {
            theta = Vector.getQuadraticNullVector(crsImpl.getB1(), crsImpl.getPairing(), 2);
        }
        theta = theta.add(T.multiply(crsImpl.getU1()));

        return new SingleProof(pi, theta);
    }
}
