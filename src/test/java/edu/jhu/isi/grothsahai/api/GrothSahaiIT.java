package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.Pair;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.Assert.isTrue;

public class GrothSahaiIT {

    private Generator generator;
    private Prover prover;
    private Verifier verifier;
    private Pairing pairing;

    @Before
    public void setUp() throws Exception {
        generator = NIZKFactory.createGenerator(Role.PROVER);
        prover = NIZKFactory.createProver();
        verifier = NIZKFactory.createVerifier();

        pairing = generator.generatePairing();
    }

    @Test
    public void testValidProof() {
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<List<Statement>, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing);
        final Proof proof = prover.proof(crs, statementWitnessPair.getLeft(), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, statementWitnessPair.getLeft(), proof));
    }

    @Test
    public void testValidProof_multipleStatements() {
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<List<Statement>, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 2);
        final Proof proof = prover.proof(crs, statementWitnessPair.getLeft(), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, statementWitnessPair.getLeft(), proof));
    }

    @Test
    public void testInvalidProof_multipleStatements() {
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<List<Statement>, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 2);
        final Proof proof = prover.proof(crs, statementWitnessPair.getLeft(), statementWitnessPair.getRight());
        isTrue(!verifier.verify(crs, Collections.singletonList(statementWitnessPair.getLeft().get(0)), proof));
    }

    @Test
    public void testInvalidProof() {
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final CommonReferenceString crs2 = generator.generateCRS(pairing);
        final Pair<List<Statement>, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing);
        final Proof proof = prover.proof(crs, statementWitnessPair.getLeft(), statementWitnessPair.getRight());
        isTrue(!verifier.verify(crs2, statementWitnessPair.getLeft(), proof));
    }

    @Test
    public void testValidProof_onlyA() {
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<List<Statement>, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 0, 1);
        final Proof proof = prover.proof(crs, statementWitnessPair.getLeft(), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, statementWitnessPair.getLeft(), proof));
    }

    @Test
    public void testValidProof_onlyB() {
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<List<Statement>, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 0, 1, 1);
        final Proof proof = prover.proof(crs, statementWitnessPair.getLeft(), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, statementWitnessPair.getLeft(), proof));
    }
}
