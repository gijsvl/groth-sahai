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
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        PairingParameters pairingParams = generator.generatePairingParams();
        final Pairing pairing = PairingFactory.getPairing(pairingParams);
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        Prover prover = NIZKFactory.createProver(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());

        final String serializedCRS = Serializer.serializeCRS(crs);
        final String serializedStatement = Serializer.serializeStatement(statementWitnessPair.getStatement(), crs);
        final String serializedProof = Serializer.serializeProof(proof, crs);


        final CommonReferenceString deserializedCRS = Serializer.deserializeCRS(serializedCRS);
        final List<Statement> deserializedStatement = Serializer.deserializeStatement(serializedStatement, deserializedCRS);
        final Proof deserializedProof = Serializer.deserializeProof(serializedProof, deserializedCRS);
        Verifier verifier = NIZKFactory.createVerifier(deserializedCRS);

        isTrue(verifier.verify(deserializedStatement, deserializedProof));

    }
}
