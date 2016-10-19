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

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.api.NIZKFactory;
import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SerializationTest extends BaseTest {
    @Test
    public void measureStatementSerialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);

        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 2, 2, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testStatement.json");
        final String statement = Serializer.serializeStatement(statementWitnessPair.getStatement(), crs);
        fileWriter.write(statement.getBytes());
        fileWriter.close();

        System.out.println("Size of Statement: " + Files.size(Paths.get("testStatement.json")) + " Bytes");
        Files.delete(Paths.get("testStatement.json"));
    }

    @Test
    public void measureProofSerialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        Prover prover = NIZKFactory.createProver(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 1, 1, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());

        final FileOutputStream fileWriter = new FileOutputStream("testProof.json");
        fileWriter.write(Serializer.serializeProof(proof, crs).getBytes());
        fileWriter.close();

        System.out.println("Size of proof: " + Files.size(Paths.get("testProof.json")) + " Bytes");
        Files.delete(Paths.get("testProof.json"));
    }

    @Test
    public void testStatementDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);

        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 2, 2, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testStatement.json");
        final String statement = Serializer.serializeStatement(statementWitnessPair.getStatement(), crs);
        fileWriter.write(statement.getBytes());
        fileWriter.close();


        final FileInputStream fileReader = new FileInputStream("testStatement.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final List<Statement> statements = Serializer.deserializeStatement(new String(bytes), crs);

        Files.delete(Paths.get("testStatement.json"));
        for (int i = 0; i < statements.size(); i++) {
            assertEquals(statements.get(i), statementWitnessPair.getStatement().get(i));
        }
    }

    @Test
    public void testProofDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        Prover prover = NIZKFactory.createProver(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 1, 1, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());

        final FileOutputStream fileWriter = new FileOutputStream("testProof.json");
        fileWriter.write(Serializer.serializeProof(proof, crs).getBytes());
        fileWriter.close();

        final FileInputStream fileReader = new FileInputStream("testProof.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final Proof deserializedProof = Serializer.deserializeProof(new String(bytes), crs);
        Files.delete(Paths.get("testProof.json"));

        assertEquals(proof, deserializedProof);

    }

    @Test
    public void testWitnessDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        PairingParameters pairingparams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingparams);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 1, 1, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testWitness.json");
        fileWriter.write(Serializer.serializeWitness(statementWitnessPair.getWitness(), crs).getBytes());
        fileWriter.close();

        final FileInputStream fileReader = new FileInputStream("testWitness.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final Witness witness = Serializer.deserializeWitness(new String(bytes), crs);
        Files.delete(Paths.get("testWitness.json"));

        assertEquals(statementWitnessPair.getWitness(), witness);
    }

    @Test
    public void testStatementWitnessDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing(), 1, 1, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testStatementWitness.json");
        fileWriter.write(Serializer.serializeStatementAndWitness(statementWitnessPair, crs).getBytes());
        fileWriter.close();

        final FileInputStream fileReader = new FileInputStream("testStatementWitness.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final StatementAndWitness statementAndWitness = Serializer.deserializeStatementAndWitness(new String(bytes), crs);
        Files.delete(Paths.get("testStatementWitness.json"));

        assertEquals(statementWitnessPair, statementAndWitness);
    }

    @Test
    public void testCRSDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);

        final FileOutputStream fileWriter = new FileOutputStream("testCRS.json");
        fileWriter.write(Serializer.serializeCRS(crs).getBytes());
        fileWriter.close();

        final FileInputStream fileReader = new FileInputStream("testCRS.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final CommonReferenceString deserializedCRS = Serializer.deserializeCRS(new String(bytes));
        Files.delete(Paths.get("testCRS.json"));

        assertEquals(crs, deserializedCRS);
    }
}
