package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;

import java.util.List;

public interface Prover {
    Proof proof(List<Statement> statements, Witness witness);
}
