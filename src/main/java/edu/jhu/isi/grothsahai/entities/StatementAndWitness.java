package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.json.Serializer;

import java.util.List;
import java.util.Objects;

public class StatementAndWitness {
    private List<Statement> statement;
    private Witness witness;

    public StatementAndWitness(final List<Statement> statement, final Witness witness) {
        this.statement = statement;
        this.witness = witness;
    }

    public static StatementAndWitness generateFromJson(final String statementAndWitness, final CommonReferenceString crs) {
        return Serializer.deserializeStatementAndWitness(statementAndWitness, crs);
    }

    public String getAsJson(final CommonReferenceString crs) {
        return Serializer.serializeStatementAndWitness(this, crs);
    }

    public String getStatementAsJson(final CommonReferenceString crs) {
        return Serializer.serializeStatement(getStatement(), crs);
    }

    public String getWitnessAsJson(final CommonReferenceString crs) {
        return Serializer.serializeWitness(getWitness(), crs);
    }

    public List<Statement> getStatement() {
        return statement;
    }

    public Witness getWitness() {
        return witness;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StatementAndWitness that = (StatementAndWitness) o;
        return Objects.equals(statement, that.statement) &&
                Objects.equals(witness, that.witness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statement, witness);
    }

    @Override
    public String toString() {
        return "StatementAndWitness{" +
                "statement=" + statement +
                ", witness=" + witness +
                '}';
    }
}
