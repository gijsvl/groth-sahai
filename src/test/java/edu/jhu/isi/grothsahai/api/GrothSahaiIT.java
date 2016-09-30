package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.springframework.util.Assert.isTrue;

public class GrothSahaiIT {

    private Generator generator;
    private Prover prover;
    private Verifier verifier;
    private Pairing pairing;
    private CommonReferenceString crs;

    @Before
    public void setUp() throws Exception {
        generator = NIZKFactory.createGenerator(Role.PROVER);

        pairing = generator.generatePairing();
        crs = generator.generateCRS(pairing);
        prover = NIZKFactory.createProver(crs);
        verifier = NIZKFactory.createVerifier(crs);
    }

    @Test
    public void testValidProof() {
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
        isTrue(verifier.verify(statementWitnessPair.getStatement(), proof));
    }

    @Test
    public void testValidProof_multipleStatements() {
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 2);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
        isTrue(verifier.verify(statementWitnessPair.getStatement(), proof));
    }

    @Test
    public void testInvalidProof_multipleStatements() {
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 2);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
        isTrue(!verifier.verify(Collections.singletonList(statementWitnessPair.getStatement().get(0)), proof));
    }

    @Test
    public void testInvalidProof() throws Exception {
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
        final Proof proofImpl = (Proof) proof;
        ReflectionTestUtils.setField(proof, "c", proofImpl.getC().add(proofImpl.getC()));
        isTrue(!verifier.verify(statementWitnessPair.getStatement(), proof));
    }

    @Test
    public void testValidProof_onlyA() {
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 0, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
        isTrue(verifier.verify(statementWitnessPair.getStatement(), proof));
    }

    @Test
    public void testValidProof_onlyB() {
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 0, 1, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
        isTrue(verifier.verify(statementWitnessPair.getStatement(), proof));
    }
}
