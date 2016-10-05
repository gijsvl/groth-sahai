package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Witness;

import java.util.List;

public interface Prover {
    Proof proof(final String statementAndWitness);

    Proof proof(final String statement, final String witness);

    Proof proof(final List<Statement> statements, final Witness witness);

    StatementAndWitness createDisjunctionStatements(final List<Statement> SatisfiedStatements, final List<Statement> UnsatisfiedStatements, final Witness witness);
}
