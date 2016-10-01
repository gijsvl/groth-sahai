package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.SingleProof;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.json.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProverImpl implements Prover {
    private CommonReferenceString crs;

    public ProverImpl(final String crs) {
        this.crs = Serializer.deserializeCRS(crs);
    }

    public ProverImpl(final CommonReferenceString crs) {
        this.crs = crs;
    }

    public Proof proof(final String statementAndWitness) {
        final StatementAndWitness statementAndWitnessObj = StatementAndWitness.generateFromJson(statementAndWitness, crs);
        return proof(statementAndWitnessObj.getStatement(), statementAndWitnessObj.getWitness());
    }

    public Proof proof(final String statement, final String witness) {
        final List<Statement> statementsObj = Serializer.deserializeStatement(statement, crs);
        final Witness witnessObj = Serializer.deserializeWitness(witness, crs);
        return proof(statementsObj, witnessObj);
    }

    public Proof proof(final List<Statement> statements, final Witness witness) {
        final Matrix R = Matrix.random(crs.getZr(), witness.getX().getLength(), 2);
        final Matrix S = Matrix.random(crs.getZr(), witness.getY().getLength(), 2);
        final Vector c = R != null ? crs.iota(1, witness.getX()).add(R.multiply(crs.getU1())) : null;
        final Vector d = S != null ? crs.iota(2, witness.getY()).add(S.multiply(crs.getU2())) : null;

        final ArrayList<SingleProof> proofs = statements.stream().map(statement -> getSingleProof(statement, witness, R, S)).collect(Collectors.toCollection(ArrayList::new));
        return new Proof(c, d, proofs);
    }

    private SingleProof getSingleProof(final Statement statement, final Witness witness, final Matrix R, final Matrix S) {
        final Matrix T = Matrix.random(crs.getZr(), 2, 2);
        Vector pi;
        if (R != null) {
            pi = R.getTranspose().multiply(crs.iota(2, statement.getB()));
            if (statement.getGamma() != null) {
                pi = pi.add(R.getTranspose().multiply(statement.getGamma()).multiply(crs.iota(2, witness.getY())))
                        .add(R.getTranspose().multiply(statement.getGamma()).multiply(S).multiply(crs.getU2()));
            }
        } else {
            pi = Vector.getQuadraticNullVector(crs.getB2(), crs.getPairing(), 2);
        }
        pi = pi.sub(T.getTranspose().multiply(crs.getU2()));


        Vector theta;
        if (S != null) {
            theta = S.getTranspose().multiply(crs.iota(1, statement.getA()));
            if (statement.getGamma() != null) {
                theta = theta.add(S.getTranspose().multiply(statement.getGamma().getTranspose())
                        .multiply(crs.iota(1, witness.getX())));
            }
        } else {
            theta = Vector.getQuadraticNullVector(crs.getB1(), crs.getPairing(), 2);
        }
        theta = theta.add(T.multiply(crs.getU1()));

        return new SingleProof(pi, theta);
    }
}
