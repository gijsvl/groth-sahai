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
