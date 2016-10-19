/*
 * Copyright (c) 2016 Gijs Van Laer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class GrothSahaiPerformance {
    @Test
    public void testPerformance() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final Prover prover = NIZKFactory.createProver(crs);
        final Verifier verifier = NIZKFactory.createVerifier(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 1, 1, 1);
        executePerformanceTest(prover, verifier, statementWitnessPair);
    }

    private void executePerformanceTest(final Prover prover, final Verifier verifier, final StatementAndWitness statementWitnessPair) {
        long proofDuration = 0;
        long verificationDuration = 0;
        for (int i = 0;i < 10;i++) {
            final long startProof = System.nanoTime();
            final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
            proofDuration += System.nanoTime() - startProof;
            final long startVerify = System.nanoTime();
            verifier.verify(statementWitnessPair.getStatement(), proof);
            verificationDuration += System.nanoTime() - startVerify;
        }

        System.out.println("Proof Duration: " + proofDuration / 10000000 + "ms");
        System.out.println("Verification Duration: " + verificationDuration / 10000000 + "ms");
    }

    @Test
    public void testPerformance_onlyA() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final Prover prover = NIZKFactory.createProver(crs);
        final Verifier verifier = NIZKFactory.createVerifier(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 1, 0, 1);
        executePerformanceTest(prover, verifier, statementWitnessPair);
    }

    @Test
    public void testPerformance_onlyB() {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final Prover prover = NIZKFactory.createProver(crs);
        final Verifier verifier = NIZKFactory.createVerifier(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 0, 1, 1);
        executePerformanceTest(prover, verifier, statementWitnessPair);
    }
}
