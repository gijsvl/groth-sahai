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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

class CRSJsonSerializer implements JsonSerializer<CommonReferenceString>, JsonDeserializer<CommonReferenceString> {

    private static final String PAIRING_PARAMS = "pairingParams";
    private static final String U = "u";
    private static final String ELEMENTS = "elements";

    @Override
    public CommonReferenceString deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        try {
            final PairingParameters pairingParams = getPairingParameters(jsonElement);
            final CommonReferenceString crs = CommonReferenceString.generate(pairingParams);
            final JsonArray u = jsonElement.getAsJsonObject().get(U).getAsJsonArray();
            final JsonElement elements1 = u.get(0).getAsJsonObject().get(ELEMENTS);
            final JsonElement elements2 = u.get(1).getAsJsonObject().get(ELEMENTS);
            final Element u11 = context.deserialize(elements1.getAsJsonArray().get(0), Element.class);
            final Element u12 = context.deserialize(elements1.getAsJsonArray().get(1), Element.class);
            final Element u21 = context.deserialize(elements2.getAsJsonArray().get(0), Element.class);
            final Element u22 = context.deserialize(elements2.getAsJsonArray().get(1), Element.class);
            crs.setU((QuadraticElement) u11, (QuadraticElement) u12, (QuadraticElement) u21, (QuadraticElement) u22);
            return crs;
        } catch (IOException e) {
            throw new JsonParseException("Could not read pairing parameters", e);
        }
    }

    private PairingParameters getPairingParameters(final JsonElement jsonElement) throws IOException {
        final File temp = File.createTempFile("params", ".tmp");
        temp.deleteOnExit();
        final BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(jsonElement.getAsJsonObject().get(PAIRING_PARAMS).getAsString());
        out.close();
        return PairingFactory.getPairingParameters(temp.getPath());
    }

    @Override
    public JsonElement serialize(final CommonReferenceString crs, final Type type, final JsonSerializationContext context) {
        final JsonElement u1 = context.serialize(crs.getU1());
        final JsonElement u2 = context.serialize(crs.getU2());
        final JsonArray u = new JsonArray();
        u.add(u1);
        u.add(u2);

        final JsonObject jsonObject = new JsonObject();
        jsonObject.add(U, u);
        jsonObject.addProperty(PAIRING_PARAMS, crs.getPairingParams().toString());
        return jsonObject;
    }
}
