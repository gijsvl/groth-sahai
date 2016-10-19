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
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
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

        final PairingParameters pairingParameters = generator.generatePairingParams();
        pairing = PairingFactory.getPairing(pairingParameters);
        crs = generator.generateCRS(pairingParameters);
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
    @Ignore("Cannot do disjunctions without T being zero in statements")
    public void testValidProof_disjunction() {
        final StatementAndWitness statementWitnessPair1 = generator.generateStatementAndWitness(pairing, 4, 3, 1);
        final StatementAndWitness statementWitnessPair2 = generator.generateStatementAndWitness(pairing, 6, 5, 2);
        final StatementAndWitness statementWitnessPair = prover.createDisjunctionStatements(
                statementWitnessPair1.getStatement(),
                statementWitnessPair2.getStatement(),
                statementWitnessPair1.getWitness());

        final Vector x = statementWitnessPair.getWitness().getX();
        final Vector y = statementWitnessPair.getWitness().getY();
        for (final Statement statement : statementWitnessPair.getStatement()) {
            final Vector a = statement.getA();
            final Vector b = statement.getB();
            final Matrix gamma = statement.getGamma();
            final Element t = statement.getT();
            assertEquals(a.pair(y, crs.getPairing())
                    .add(x.pair(b, crs.getPairing()))
                    .add(x.pair(gamma.multiply(y), crs.getPairing())).getImmutable(), t);
        }


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
        ReflectionTestUtils.setField(proof, "c", proof.getC().add(proof.getC()));
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
