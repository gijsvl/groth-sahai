package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.springframework.util.Assert.notNull;

public class ProverImplTest extends BaseTest {
    @Test
    public void testProof() {
        final PairingParameters pairing = new GeneratorImpl(Role.PROVER).generatePairingParams();
        final CommonReferenceString crs = CommonReferenceString.generate(pairing);
        final Prover prover = new ProverImpl(crs);
        final StatementAndWitness statementWitnessPair = new GeneratorImpl(Role.PROVER).generateStatementAndWitness(crs.getPairing());
        final Witness witness = statementWitnessPair.getWitness();
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), witness);

        notNull(proof.getC());
        assertEquals(crs.getG1(), ((QuadraticElement) proof.getC().get(0)).getField().getTargetField());
        assertEquals(witness.getX().getLength(), proof.getC().getLength());
        notNull(proof.getD());
        assertEquals(crs.getG2(), ((QuadraticElement) proof.getD().get(0)).getField().getTargetField());
        assertEquals(witness.getY().getLength(), proof.getD().getLength());
        notNull(proof.getProofs().get(0).getPi());
        assertEquals(crs.getG2(), ((QuadraticElement) proof.getProofs().get(0).getPi().get(0)).getField().getTargetField());
        assertEquals(2, proof.getProofs().get(0).getPi().getLength());
        notNull(proof.getProofs().get(0).getTheta());
        assertEquals(crs.getG1(), ((QuadraticElement) proof.getProofs().get(0).getTheta().get(0)).getField().getTargetField());
        assertEquals(2, proof.getProofs().get(0).getTheta().getLength());
    }

    @Test
    public void testProofDisjunction() {
        final PairingParameters pairing = new GeneratorImpl(Role.PROVER).generatePairingParams();
        final CommonReferenceString crs = CommonReferenceString.generate(pairing);
        final Prover prover = new ProverImpl(crs);
        final StatementAndWitness statementWitnessPair1 = new GeneratorImpl(Role.PROVER).generateStatementAndWitness(crs.getPairing());
        final StatementAndWitness statementWitnessPair2 = new GeneratorImpl(Role.PROVER).generateStatementAndWitness(crs.getPairing());
        final StatementAndWitness statementWitnessPair = prover.createDisjunctionStatements(
                statementWitnessPair1.getStatement(),
                statementWitnessPair2.getStatement(),
                statementWitnessPair1.getWitness());
        final Witness witness = statementWitnessPair.getWitness();
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());

        notNull(proof.getC());
        assertEquals(crs.getG1(), ((QuadraticElement) proof.getC().get(0)).getField().getTargetField());
        assertEquals(witness.getX().getLength(), proof.getC().getLength());
        notNull(proof.getD());
        assertEquals(crs.getG2(), ((QuadraticElement) proof.getD().get(0)).getField().getTargetField());
        assertEquals(witness.getY().getLength(), proof.getD().getLength());
        notNull(proof.getProofs().get(0).getPi());
        assertEquals(crs.getG2(), ((QuadraticElement) proof.getProofs().get(0).getPi().get(0)).getField().getTargetField());
        assertEquals(2, proof.getProofs().get(0).getPi().getLength());
        notNull(proof.getProofs().get(0).getTheta());
        assertEquals(crs.getG1(), ((QuadraticElement) proof.getProofs().get(0).getTheta().get(0)).getField().getTargetField());
        assertEquals(2, proof.getProofs().get(0).getTheta().getLength());
    }
}
