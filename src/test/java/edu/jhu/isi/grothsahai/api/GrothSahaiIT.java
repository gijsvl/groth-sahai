package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.enums.ImplementationType;
import edu.jhu.isi.grothsahai.enums.Role;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.commons.lang3.Validate.isTrue;

public class GrothSahaiIT {
    @Ignore
    @Test
    public void testValidProof() {
        final Generator generator = NIZKFactory.createGenerator(ImplementationType.GROTH_SAHAI, Role.PROVER);
        final Prover prover = NIZKFactory.createProver(ImplementationType.GROTH_SAHAI);
        final Verifier verifier = NIZKFactory.createVerifier(ImplementationType.GROTH_SAHAI);

        final CommonReferenceString crs = generator.generateCRS();
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(crs);
        final Proof proof = prover.proof(crs, statementWitnessPair.getLeft(), statementWitnessPair.getRight());
        isTrue(verifier.verify(crs, statementWitnessPair.getLeft(), proof));
    }
}