package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.Pair;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

@Ignore
public class GrothSahaiPerformance {
    @Test
    public void testPerformance() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final Prover prover = NIZKFactory.createProver();
        final Verifier verifier = NIZKFactory.createVerifier();

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1);
        executePerformanceTest(prover, verifier, crs, statementWitnessPair);
    }

    private void executePerformanceTest(final Prover prover, final Verifier verifier, final CommonReferenceString crs, final Pair<Statement, Witness> statementWitnessPair) {
        long proofDuration = 0;
        long verificationDuration = 0;
        for (int i = 0;i < 10;i++) {
            final long startProof = System.nanoTime();
            final Proof proof = prover.proof(crs, Arrays.asList(statementWitnessPair.getLeft()), statementWitnessPair.getRight());
            proofDuration += System.nanoTime() - startProof;
            final long startVerify = System.nanoTime();
            verifier.verify(crs, Arrays.asList(statementWitnessPair.getLeft()), proof);
            verificationDuration += System.nanoTime() - startVerify;
        }

        System.out.println("Proof Duration: " + proofDuration / 10000000 + "ms");
        System.out.println("Verification Duration: " + verificationDuration / 10000000 + "ms");
    }

    @Test
    public void testPerformance_onlyA() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final Prover prover = NIZKFactory.createProver();
        final Verifier verifier = NIZKFactory.createVerifier();

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 0);
        executePerformanceTest(prover, verifier, crs, statementWitnessPair);
    }

    @Test
    public void testPerformance_onlyB() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final Prover prover = NIZKFactory.createProver();
        final Verifier verifier = NIZKFactory.createVerifier();

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing, 0, 1);
        executePerformanceTest(prover, verifier, crs, statementWitnessPair);
    }
}
