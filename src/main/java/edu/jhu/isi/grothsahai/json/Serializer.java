/*
 * Copyright (c) 2016 Gijs Van Laer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.jhu.isi.grothsahai.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;

public class Serializer {
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public static String serializeCRS(final CommonReferenceString crs) {
        final Gson gson = gsonBuilder
                .registerTypeAdapter(CommonReferenceString.class, new CRSJsonSerializer())
                .registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.toJson(crs);
    }

    public static CommonReferenceString deserializeCRS(final String crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(CommonReferenceString.class, new CRSJsonSerializer())
                .create();
        return gson.fromJson(crs, CommonReferenceString.class);
    }

    public static String serializeStatementAndWitness(final StatementAndWitness statementWitnessPair, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.toJson(statementWitnessPair);
    }

    public static StatementAndWitness deserializeStatementAndWitness(final String statementWitness, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.fromJson(statementWitness, StatementAndWitness.class);
    }

    public static String serializeStatement(final List<Statement> statements, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        final Statements statementsObj = new Statements(statements);
        return gson.toJson(statementsObj);
    }

    public static List<Statement> deserializeStatement(final String statement, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.fromJson(statement, Statements.class).getStatements();
    }

    private static class Statements {
        private List<Statement> statements = new ArrayList<>();

        @SuppressWarnings("unused")
        public Statements() {
            // Empty constructor for GSON serialization
        }

        Statements(final List<Statement> statements) {
            this.statements = statements;
        }

        List<Statement> getStatements() {
            return statements;
        }
    }

    public static String serializeWitness(final Witness witness, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.toJson(witness);
    }

    public static Witness deserializeWitness(final String witness, final CommonReferenceString crs) {
        return gsonBuilder
                .registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .fromJson(witness, Witness.class);
    }

    public static String serializeProof(final Proof proof, final CommonReferenceString crs) {
        return gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .toJson(proof);
    }

    public static Proof deserializeProof(final String proof, final CommonReferenceString crs) {
        return gsonBuilder
                .registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .fromJson(proof, Proof.class);
    }
}
