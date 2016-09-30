package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;

import java.util.List;

public interface Verifier {
    Boolean verify(List<Statement> statements, Proof proof);
}
