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
package edu.jhu.isi.grothsahai.json;

import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.api.NIZKFactory;
import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

import java.util.List;

import static org.springframework.util.Assert.isTrue;

public class SerializationIT {
    @Test
    public void testProofDeserialization() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final PairingParameters pairingParams = generator.generatePairingParams();
        final Pairing pairing = PairingFactory.getPairing(pairingParams);
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final Prover prover = NIZKFactory.createProver(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());

        final String serializedCRS = Serializer.serializeCRS(crs);
        final String serializedStatement = Serializer.serializeStatement(statementWitnessPair.getStatement(), crs);
        final String serializedProof = Serializer.serializeProof(proof, crs);


        final CommonReferenceString deserializedCRS = Serializer.deserializeCRS(serializedCRS);
        final List<Statement> deserializedStatement = Serializer.deserializeStatement(serializedStatement, deserializedCRS);
        final Proof deserializedProof = Serializer.deserializeProof(serializedProof, deserializedCRS);
        final Verifier verifier = NIZKFactory.createVerifier(deserializedCRS);

        isTrue(verifier.verify(deserializedStatement, deserializedProof));
    }

    @Test
    public void testStringInterface() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        final PairingParameters pairingParams = generator.generatePairingParams();
        final Pairing pairing = PairingFactory.getPairing(pairingParams);
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final String crsString = crs.getAsJson();
        final Prover prover = NIZKFactory.createProver(crsString);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing);
        final String statementWitnessPairString = statementWitnessPair.getAsJson(crs);
        final Proof proof = prover.proof(statementWitnessPairString);
        final String serializedProof = proof.getAsJson(crs);

        final Proof proof2 = prover.proof(statementWitnessPair.getStatementAsJson(crs),
                statementWitnessPair.getWitnessAsJson(crs));
        final String serializedProof2 = proof2.getAsJson(crs);

        final Verifier verifier = NIZKFactory.createVerifier(crsString);

        isTrue(verifier.verify(statementWitnessPair.getStatementAsJson(crs), serializedProof));
        isTrue(verifier.verify(statementWitnessPair.getStatementAsJson(crs), serializedProof2));
    }
}
