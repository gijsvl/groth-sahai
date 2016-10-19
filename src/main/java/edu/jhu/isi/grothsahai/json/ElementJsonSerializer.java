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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.CustomQuadraticElement;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.lang.reflect.Type;

class ElementJsonSerializer<E extends Element> implements JsonSerializer<E>, JsonDeserializer<E> {

    private static final String VALUE = "value";
    private static final String TYPE = "type";
    private final CommonReferenceString crs;

    ElementJsonSerializer(final CommonReferenceString crs) {
        this.crs = crs;
    }

    public JsonElement serialize(final E e, final Type type, final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        final ElementType elementType;
        if (FieldEquals.equals(e.getField(), crs.getG1())) {
            elementType = ElementType.G1;
        } else if(FieldEquals.equals(e.getField(), crs.getG2())) {
            elementType = ElementType.G2;
        } else if(FieldEquals.equals(e.getField(), crs.getGT())) {
            elementType = ElementType.GT;
        } else if(FieldEquals.equals(e.getField(), crs.getB1())) {
            elementType = ElementType.B1;
        } else if(FieldEquals.equals(e.getField(), crs.getB2())) {
            elementType = ElementType.B2;
        } else if(FieldEquals.equals(e.getField(), crs.getBT())) {
            elementType = ElementType.BT;
        } else if(FieldEquals.equals(e.getField(), crs.getZr())) {
            elementType = ElementType.ZR;
        } else {
            throw new IllegalStateException("No such field");
        }
        jsonObject.addProperty(TYPE, elementType.toString());
        jsonObject.addProperty(VALUE, Hex.encodeHexString(e.toBytes()));
        return jsonObject;
    }

    public E deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        try {
            E e;
            final ElementType elementType = ElementType.valueOf(jsonObject.get(TYPE).getAsString());
            switch (elementType) {
                case ZR:
                    e = (E) crs.getZr().newElement();
                    break;
                case G1:
                    e = (E) crs.getG1().newElement();
                    break;
                case G2:
                    e = (E) crs.getG2().newElement();
                    break;
                case GT:
                    e = (E) crs.getGT().newElement();
                    break;
                case B1:
                    e = (E) crs.getB1().newElement();
                    break;
                case B2:
                    e = (E) crs.getB2().newElement();
                    break;
                case BT:
                    e = (E) crs.getBT().newElement();
                    break;
                default:
                    throw new IllegalStateException("No such type");
            }
            e.setFromBytes(Hex.decodeHex(jsonObject.get(VALUE).getAsString().toCharArray()));
            switch (elementType) {
                case B1:
                case B2:
                    e = (E) new CustomQuadraticElement<QuadraticElement>((QuadraticElement) e, crs.getPairing());
                    break;
            }
            return e;
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
