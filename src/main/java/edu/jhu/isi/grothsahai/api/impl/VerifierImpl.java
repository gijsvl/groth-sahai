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

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.QuarticElement;
import edu.jhu.isi.grothsahai.entities.SingleProof;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import edu.jhu.isi.grothsahai.json.Serializer;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

public class VerifierImpl implements Verifier {

    private CommonReferenceString crs;

    public VerifierImpl(final String crs) {
        this.crs = Serializer.deserializeCRS(crs);
    }

    public VerifierImpl(final CommonReferenceString crs) {
        this.crs = crs;
    }

    public Boolean verify(final String statement, final String proof) {
        final List<Statement> statementObj = Serializer.deserializeStatement(statement, crs);
        final Proof proofObj = Serializer.deserializeProof(proof, crs);
        return verify(statementObj, proofObj);
    }

    public Boolean verify(final List<Statement> statements, final Proof proof) {
        if (proof.getProofs().size() != statements.size()) {
            return false;
        }

        for (int i = 0; i < proof.getProofs().size(); i++) {
            if (!verifyOneEquation(proof, statements.get(i), proof.getProofs().get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean verifyOneEquation(final Proof proof, final Statement statement, final SingleProof singleProof) {
        Element lhs;
        if (statement.getA().getLength() != 0) {
            lhs = crs.iota(1, statement.getA()).pairInB(proof.getD(), crs.getPairing());
        } else {
            lhs = new QuarticElement(crs.getBT(), crs.getGT().newZeroElement(), crs.getGT().newZeroElement(), crs.getGT().newZeroElement(), crs.getGT().newZeroElement());
        }
        if (statement.getB().getLength() != 0) {
            lhs = lhs.add(proof.getC().pairInB(crs.iota(2, statement.getB()), crs.getPairing()));
        }
        if (statement.getGamma() != null) {
            lhs = lhs.add(proof.getC().pairInB(statement.getGamma().multiply(proof.getD()), crs.getPairing()));
        }

        final Element rhs = crs.iotaT(ProblemType.PAIRING_PRODUCT, statement.getT())
                .add(crs.getU1().pairInB(singleProof.getPi(), crs.getPairing()))
                .add(singleProof.getTheta().pairInB(crs.getU2(), crs.getPairing()));
        return lhs.isEqual(rhs);
    }
}
