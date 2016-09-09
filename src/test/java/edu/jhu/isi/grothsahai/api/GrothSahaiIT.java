package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.Pair;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Test;

import java.util.Arrays;

import static org.springframework.util.Assert.isTrue;

public class GrothSahaiIT {
    @Test
    public void testValidProof() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final Prover prover = NIZKFactory.createProver();
        final Verifier verifier = NIZKFactory.createVerifier();

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing);
        final Proof proof = prover.proof(crs, Arrays.asList(statementWitnessPair.getLeft()), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, Arrays.asList(statementWitnessPair.getLeft()), proof));
    }

    @Test
    public void testInvalidProof() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final Prover prover = NIZKFactory.createProver();
        final Verifier verifier = NIZKFactory.createVerifier();

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final CommonReferenceString crs2 = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing);
        final Proof proof = prover.proof(crs, Arrays.asList(statementWitnessPair.getLeft()), statementWitnessPair.getRight());

        isTrue(!verifier.verify(crs2, Arrays.asList(statementWitnessPair.getLeft()), proof));
    }

    @Test
    public void testValidProof_onlyA() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final Prover prover = NIZKFactory.createProver();
        final Verifier verifier = NIZKFactory.createVerifier();

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 0);
        final Proof proof = prover.proof(crs, Arrays.asList(statementWitnessPair.getLeft()), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, Arrays.asList(statementWitnessPair.getLeft()), proof));
    }

    @Test
    public void testValidProof_onlyB() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final Prover prover = NIZKFactory.createProver();
        final Verifier verifier = NIZKFactory.createVerifier();

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 0, 1);
        final Proof proof = prover.proof(crs, Arrays.asList(statementWitnessPair.getLeft()), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, Arrays.asList(statementWitnessPair.getLeft()), proof));
    }
}
