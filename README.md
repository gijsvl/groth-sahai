# README #
An implementation of the Groth-Sahai NIZK protocol

## ⚠ Warning ⚠ ##
This is an experimental library and is only at its first version. The API could still be updated in the near future.
I cannot guarantee the security of this library, and one should not use this in production.

### Setup ###

* Install PBC on your machine (Hopefully we can solve this in time)

https://crypto.stanford.edu/pbc/

* Setup a shared library

http://gas.dia.unisa.it/projects/jpbc/docs/pbcwrapper.html

### Run tests ###
* mvn clean test

### Implementation details ###
Generation:
```
#!java

final Generator generator = NIZKFactory.createGenerator(Role.PROVER);

final PairingParameters pairingParameters = generator.generatePairingParams();
final CommonReferenceString crs = generator.generateCRS(pairingParameters);
final Prover prover = NIZKFactory.createProver(crs);
final Verifier verifier = NIZKFactory.createVerifier(crs);
final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(crs.getPairing());
```

Proof:
```
#!java

final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());
```

Verifying:
```
#!java

verifier.verify(statementWitnessPair.getStatement(), proof)
```

### References ###
Groth, Jens and Sahai, Amit, **Efficient Non-interactive Proof Systems for Bilinear Groups**, *Advances in Cryptology -- EUROCRYPT 2008: 27th Annual International Conference on the Theory and Applications of Cryptographic Techniques, Istanbul, Turkey, April 13-17, 2008. Proceedings (2008), Springer Berlin Heidelberg*

Ghadafi, Essam, Smart, Nigel. P. and Warinschi, Bogdan, **Groth--Sahai Proofs Revisited**, *Public Key Cryptography -- PKC 2010: 13th International Conference on Practice and Theory in Public Key Cryptography, Paris, France, May 26-28, 2010. Proceedings (2010), Springer Berlin Heidelberg*

Groth, Jens, **Simulation-Sound NIZK Proofs for a Practical Language and Constant Size Group Signatures**, *Advances in Cryptology -- ASIACRYPT 2006: 12th International Conference on the Theory and Application of Cryptology and Information Security, Shanghai, China, December 3-7, 2006. Proceedings (2006), Springer Berlin Heidelberg*

Melissa Chase, Markulf Kohlweiss, Anna Lysyanskaya and Sarah Meiklejohn, **Malleable Proof Systems and Applications**, *Cryptology ePrint Archive, Report 2012/012 (2012)*

### License ###
Copyright (c) 2016 Gijs Van Laer

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.