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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proof {
    private Vector c;
    private Vector d;
    private List<SingleProof> proofs = new ArrayList<>();

    public Proof(final Vector c, final Vector d, final List<SingleProof> proofs) {
        this.c = c;
        this.d = d;
        this.proofs = proofs;
    }

    public Proof() {
    }

    public String getAsJson(final CommonReferenceString crs) {
        return Serializer.serializeProof(this, crs);
    }

    public Vector getC() {
        return c;
    }

    public Vector getD() {
        return d;
    }

    public List<SingleProof> getProofs() {
        return new ArrayList<>(proofs);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Proof proof = (Proof) o;
        return Objects.equals(c, proof.c) &&
                Objects.equals(d, proof.d) &&
                Objects.equals(proofs, proof.proofs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, d, proofs);
    }

    @Override
    public String toString() {
        return "Proof{" +
                "c=" + c +
                ", d=" + d +
                ", proofs=" + proofs +
                '}';
    }
}
