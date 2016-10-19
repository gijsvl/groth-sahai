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

import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeFCurveGenerator;

import java.util.ArrayList;
import java.util.List;

public class GeneratorImpl implements Generator {
    private Role role;

    public GeneratorImpl(final Role role) {
        this.role = role;
    }

    public PairingParameters generatePairingParams() {
        final int rBits = 256;
        final PairingParametersGenerator parametersGenerator = new PBCTypeFCurveGenerator(rBits);
        return parametersGenerator.generate();
    }

    public CommonReferenceString generateCRS(final PairingParameters pairingParams) {
        return CommonReferenceString.generate(pairingParams);
    }

    public StatementAndWitness generateStatementAndWitness(final Pairing pairing) {
        if (role == Role.VERIFIER) {
            throw new IllegalStateException("The verifier should not create a statement witness pair, " +
                    "but should receive the statement from the prover at generation time.");
        }
        final int bLength = (int) Math.ceil(Math.random() * 10);
        final int aLength = (int) Math.ceil(Math.random() * 10);
        return generateStatementAndWitness(pairing, aLength, bLength, 1);
    }

    public StatementAndWitness generateStatementAndWitness(final Pairing pairing, final int aLength, final int bLength, final int nrOfStatements) {
        Element[] yElements = new Element[aLength];
        Element[] xElements = new Element[bLength];
        for (int i = 0; i < aLength; i++) {
            yElements[i] = pairing.getG2().newRandomElement().getImmutable();
        }
        for (int i = 0; i < bLength; i++) {
            xElements[i] = pairing.getG1().newRandomElement().getImmutable();
        }
        final Vector x = new Vector(xElements);
        final Vector y = new Vector(yElements);

        final List<Statement> statements = new ArrayList<>();
        for (int i = 0; i < nrOfStatements; i++) {
            statements.add(createStatement(pairing, aLength, bLength, x, y));
        }
        final Witness witness = new Witness(x, y);

        return new StatementAndWitness(statements, witness);
    }

    private Statement createStatement(final Pairing pairing, final int aLength, final int bLength, final Vector x, final Vector y) {
        Element[] aElements = new Element[aLength];
        Element[] bElements = new Element[bLength];
        for (int i = 0; i < aLength; i++) {
            aElements[i] = pairing.getG1().newRandomElement().getImmutable();
        }
        for (int i = 0; i < bLength; i++) {
            bElements[i] = pairing.getG2().newRandomElement().getImmutable();
        }
        final Matrix gamma = Matrix.random(pairing.getZr(), bLength, aLength);

        final Vector a = new Vector(aElements);
        final Vector b = new Vector(bElements);

        Element t = a.pair(y, pairing).add(x.pair(b, pairing));
        if (gamma != null) {
            t = t.add(x.pair(gamma.multiply(y), pairing));
        }
        return new Statement(a, b, gamma, t);
    }
}
