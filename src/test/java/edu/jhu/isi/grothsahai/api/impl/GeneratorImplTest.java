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
package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.springframework.util.Assert.notNull;

public class GeneratorImplTest extends BaseTest {
    @Test
    public void canCreateGenerator() {
        final Generator generator = new GeneratorImpl(Role.PROVER);
        notNull(generator);
    }

    @Test
    public void testGeneratePairing() {
        final Generator generator = new GeneratorImpl(Role.PROVER);

        final PairingParameters pairing = generator.generatePairingParams();
        notNull(pairing);
    }

    @Test
    public void testGenerateCRS() {
        final Generator generator = new GeneratorImpl(Role.PROVER);

        final CommonReferenceString crs = generator.generateCRS(generator.generatePairingParams());
        notNull(crs);
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateStatementAndWitness_asVerifier() {
        final GeneratorImpl generator = new GeneratorImpl(Role.VERIFIER);

        final PairingParameters pairingParams = generator.generatePairingParams();
        generator.generateStatementAndWitness(PairingFactory.getPairing(pairingParams));
    }

    @Test
    public void testGenerateStatementAndWitness_asProver() {
        final GeneratorImpl generator = new GeneratorImpl(Role.PROVER);

        final PairingParameters pairing = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing());

        final Statement statement = statementWitnessPair.getStatement().get(0);
        final Vector a = statement.getA();
        final Vector b = statement.getB();
        final Matrix gamma = statement.getGamma();
        final Element t = statement.getT();
        final Vector x = statementWitnessPair.getWitness().getX();
        final Vector y = statementWitnessPair.getWitness().getY();

        notNull(statementWitnessPair.getStatement());
        notNull(a);
        notNull(b);
        notNull(gamma);
        notNull(t);
        notNull(statementWitnessPair.getWitness());
        notNull(x);
        notNull(y);

        assertEquals(a.pair(y, crs.getPairing())
                .add(x.pair(b, crs.getPairing()))
                .add(x.pair(gamma.multiply(y), crs.getPairing())).getImmutable(), t);
        assertEquals(crs.getPairing().getGT(), t.getField());
    }
}
