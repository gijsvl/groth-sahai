package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.SingleProof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.json.Serializer;
import it.unisa.dia.gas.jpbc.Element;

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

    public StatementAndWitness createDisjunctionStatements(final List<Statement> satisfiedStatements, final List<Statement> unsatisfiedStatements, final Witness witness) {
        satisfiedStatements.forEach(statement -> {
            if (!statement.getT().isZero()) {
                throw new IllegalStateException("T should be 0 when doing disjunctions");
            }
        });
        unsatisfiedStatements.forEach(statement -> {
            if (!statement.getT().isZero()) {
                throw new IllegalStateException("T should be 0 when doing disjunctions");
            }
        });
        final int satXLength = witness.getX().getLength();
        final int satYLength = witness.getY().getLength();
        final int unXLength = unsatisfiedStatements.get(0).getB().getLength();
        final int unYLength = unsatisfiedStatements.get(0).getA().getLength();
        final int xLength = 2 * unXLength + 2 * satXLength;
        final int yLength = 2 + satYLength + unYLength;

        final List<Statement> newStatement = createOrStatements(satisfiedStatements, unsatisfiedStatements, satXLength, satYLength, unXLength, unYLength, xLength, yLength);
        final Witness newWitness = createOrWitness(witness, unXLength, unYLength);

        return new StatementAndWitness(newStatement, newWitness);
    }

    private List<Statement> createOrStatements(final List<Statement> satisfiedStatements, final List<Statement> unsatisfiedStatements, final int satXLength, final int satYLength, final int unXLength, final int unYLength, final int xLength, final int yLength) {
        final List<Statement> newStatement = new ArrayList<>();
        newStatement.add(getVStatement(xLength, yLength));
        newStatement.addAll(createCheckX0Statement(satXLength, xLength, yLength));
        newStatement.addAll(createCheckX1Statement(satXLength, xLength, yLength, unXLength));
        newStatement.addAll(updateSatisfiedStatements(satisfiedStatements, satXLength, satYLength, unXLength, unYLength));
        newStatement.addAll(updateUnsatisfiedStatements(unsatisfiedStatements, satXLength, satYLength, unXLength, unYLength));
        return newStatement;
    }

    private List<Statement> updateSatisfiedStatements(final List<Statement> statements, final int satXLength, final int satYLength, final int unXLength, final int unYLength) {
        final List<Statement> newStatements = new ArrayList<>();
        for (final Statement statement : statements) {
            final Vector a = new Vector(Vector.getZeroVector(2, crs.getG1()),
                    statement.getA(),
                    Vector.getZeroVector(unYLength, crs.getG1()));
            final Vector b = new Vector(Vector.getZeroVector(satXLength, crs.getG2()),
                    statement.getB(),
                    Vector.getZeroVector(2 * unXLength, crs.getG2()));
            final Matrix gamma = Matrix.zero(crs.getZr(), 2 * unXLength + 2 * satXLength, 2 + satYLength + unYLength);
            for (int i = 0; i < satXLength; i++) {
                for (int j = 0; j < satYLength; j++) {
                    gamma.set(satXLength + i, 2 + j, statement.getGamma().get(i, j));
                }
            }
            final Element t = statement.getT();
            newStatements.add(new Statement(a, b, gamma, t));
        }
        return newStatements;
    }

    private List<Statement> updateUnsatisfiedStatements(final List<Statement> statements, final int satXLength, final int satYLength, final int unXLength, final int unYLength) {
        final List<Statement> newStatements = new ArrayList<>();
        for (final Statement statement : statements) {
            final Vector a = new Vector(Vector.getZeroVector(2 + satYLength, crs.getG1()),
                    statement.getA());
            final Vector b = new Vector(Vector.getZeroVector(2 * satXLength + unXLength, crs.getG2()),
                    statement.getB());
            final Matrix gamma = Matrix.zero(crs.getZr(), 2 * unXLength + 2 * satXLength, 2 + satYLength + unYLength);
            for (int i = 0; i < unXLength; i++) {
                for (int j = 0; j < unYLength; j++) {
                    gamma.set(2 * satXLength + unXLength + i, 2 + satYLength + j, statement.getGamma().get(i, j));
                }
            }
            final Element t = crs.getGT().newZeroElement().getImmutable();
            newStatements.add(new Statement(a, b, gamma, t));
        }
        return newStatements;
    }

    private List<Statement> createCheckX0Statement(final int satXLength, final int xLength, final int yLength) {
        final List<Statement> checkX0Statement = new ArrayList<>();
        for (int i = 0; i < satXLength; i++) {
            checkX0Statement.add(createCheckVariableStatement(xLength, yLength,
                    i, i + satXLength, 0));
        }
        return checkX0Statement;
    }

    private List<Statement> createCheckX1Statement(final int satXLength, final int xLength, final int yLength, final int unXLength) {
        final List<Statement> checkX0Statement = new ArrayList<>();
        for (int i = 0; i < unXLength; i++) {
            checkX0Statement.add(createCheckVariableStatement(xLength, yLength,
                    i + 2 * satXLength, i + unXLength + 2 * satXLength, 1));
        }
        return checkX0Statement;
    }

    private Statement createCheckVariableStatement(final int xLength, final int yLength, final int row1, final int row2, final int colIndex) {
        final Vector a = Vector.getZeroVector(yLength, crs.getG1());
        final Vector b = Vector.getZeroVector(xLength, crs.getG2());
        final Matrix gamma = Matrix.zero(crs.getZr(), xLength, yLength);
        gamma.set(row1, colIndex, crs.getZr().newOneElement().getImmutable());
        gamma.set(row2, colIndex, crs.getZr().newOneElement().getImmutable().negate());
        return new Statement(a, b, gamma, crs.getGT().newZeroElement().getImmutable());
    }

    private Statement getVStatement(final int xLength, final int yLength) {
        final Vector a = Vector.getZeroVector(yLength, crs.getG1());
        a.set(0, crs.getG1().newOneElement().getImmutable());
        a.set(1, crs.getG1().newOneElement().getImmutable());
        return new Statement(a,
                Vector.getZeroVector(xLength, crs.getG2()),
                Matrix.zero(crs.getZr(), xLength, yLength), crs.getGT().newOneElement().getImmutable());
    }

    private Witness createOrWitness(final Witness witness, final int xLength, final int yLength) {
        final Vector x = new Vector(witness.getX(), witness.getX(),
                Vector.getZeroVector(2 * xLength, crs.getG1()));
        final Element[] v = new Element[2];
        v[0] = crs.getG2().newOneElement().getImmutable();
        v[1] = crs.getG2().newZeroElement().getImmutable();
        final Vector y = new Vector(new Vector(v), witness.getY(),
                Vector.getZeroVector(yLength, crs.getG2()));
        return new Witness(x, y);
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
            pi = Vector.getQuadraticZeroVector(crs.getB2(), crs.getPairing(), 2);
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
            theta = Vector.getQuadraticZeroVector(crs.getB1(), crs.getPairing(), 2);
        }
        theta = theta.add(T.multiply(crs.getU1()));

        return new SingleProof(pi, theta);
    }
}
