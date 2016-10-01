# README #

An implementation of the Groth-Sahai NIZK protocol

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