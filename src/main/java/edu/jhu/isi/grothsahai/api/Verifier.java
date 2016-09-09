package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;

import java.util.List;

public interface Verifier {
    Boolean verify(CommonReferenceString crs, List<Statement> statements, Proof proof);
}
