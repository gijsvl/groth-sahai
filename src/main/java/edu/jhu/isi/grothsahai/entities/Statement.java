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

import it.unisa.dia.gas.jpbc.Element;

import java.util.Objects;

public class Statement {
    private Vector a;
    private Vector b;
    private Matrix gamma;
    private Element t;

    public Statement(final Vector a, final Vector b, final Matrix gamma, final Element t) {
        this.a = a;
        this.b = b;
        this.gamma = gamma;
        this.t = t;
    }

    public Statement() {
    }

    public Vector getA() {
        return a;
    }

    public Vector getB() {
        return b;
    }

    public Matrix getGamma() {
        return gamma;
    }

    public Element getT() {
        return t;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Statement statement = (Statement) o;
        return Objects.equals(a, statement.a) &&
                Objects.equals(b, statement.b) &&
                Objects.equals(gamma, statement.gamma) &&
                Objects.equals(t, statement.t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, gamma, t);
    }

    @Override
    public String toString() {
        return "StatementImpl{" +
                "a=" + a +
                ", b=" + b +
                ", gamma=" + gamma +
                ", t=" + t +
                '}';
    }
}
