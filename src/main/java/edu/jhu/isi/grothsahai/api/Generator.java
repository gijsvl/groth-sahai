package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import org.apache.commons.lang3.tuple.Pair;

public interface Generator {
    CommonReferenceString generateCRS();

    Pair<Statement, Witness> generateStatementAndWitness(CommonReferenceString crs);
}
