package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;

import java.util.List;

public interface Verifier {
    Boolean verify(final String statement, final String proof);

    Boolean verify(final List<Statement> statements, final Proof proof);
}
