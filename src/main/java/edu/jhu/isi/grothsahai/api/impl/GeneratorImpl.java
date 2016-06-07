package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.enums.Role;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

public class GeneratorImpl implements Generator {
    private Role role;

    public GeneratorImpl(final Role role) {
        this.role = role;
    }

    public CommonReferenceString generateCRS() {
        throw new NotImplementedException("Not implemented yet");
    }

    public Pair<Statement, Witness> generateStatementAndWitness() {
        if (role == Role.VERIFIER) {
            throw new IllegalStateException("The verifier should not create a statement witness pair, " +
                    "but should receive the statement from the prover at generation time.");
        }
        throw new NotImplementedException("Not implemented yet");
    }
}
