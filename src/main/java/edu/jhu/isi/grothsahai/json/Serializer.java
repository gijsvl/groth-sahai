package edu.jhu.isi.grothsahai.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Serializer {
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public static String serializeCRS(final CommonReferenceStringImpl crs) {
        return null;
    }

    public static CommonReferenceString deserializeCRS(final String crs) {
        return null;
    }

    public static String serializeStatementAndWitness(final StatementAndWitness statementWitnessPair, final CommonReferenceStringImpl crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.toJson(statementWitnessPair);
    }

    public static StatementAndWitness deserializeStatementAndWitness(final String statementWitness, final CommonReferenceStringImpl crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.fromJson(statementWitness, StatementAndWitness.class);
    }

    public static String serializeStatement(final List<Statement> statements, final CommonReferenceStringImpl crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        final Statements statementsObj = new Statements(statements);
        return gson.toJson(statementsObj);
    }

    public static List<StatementImpl> deserializeStatement(final String statement, final CommonReferenceStringImpl crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.fromJson(statement, Statements.class).getStatements();
    }

    private static class Statements {
        private List<StatementImpl> statements = new ArrayList<>();

        public Statements() {
        }

        Statements(final List<Statement> statements) {
            this.statements = statements.stream()
                    .map(statement -> (StatementImpl) statement)
                    .collect(Collectors.toList());
        }

        List<StatementImpl> getStatements() {
            return statements;
        }
    }

    public static String serializeWitness(final WitnessImpl witness, final CommonReferenceStringImpl crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.toJson(witness);
    }

    public static Witness deserializeWitness(final String witness, final CommonReferenceStringImpl crs) {
        return gsonBuilder
                .registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .fromJson(witness, WitnessImpl.class);
    }

    public static String serializeProof(final ProofImpl proof, final CommonReferenceStringImpl crs) {
        return gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .toJson(proof);
    }

    public static Proof deserializeProof(final String proof, final CommonReferenceStringImpl crs) {
        return gsonBuilder
                .registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .fromJson(proof, ProofImpl.class);
    }
}
