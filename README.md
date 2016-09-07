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

final Generator generator = NIZKFactory.createGenerator(ImplementationType.GROTH_SAHAI, Role.PROVER);
        final Prover prover = NIZKFactory.createProver(ImplementationType.GROTH_SAHAI);
        final Verifier verifier = NIZKFactory.createVerifier(ImplementationType.GROTH_SAHAI);

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(pairing);
```

Proof:
```
#!java

final Proof proof = prover.proof(crs, Arrays.asList(statementWitnessPair.getLeft()), statementWitnessPair.getRight());
```

Verifying:
```
#!java

verifier.verify(crs, statementWitnessPair.getLeft(), proof)
```

### References ###
Groth, Jens and Sahai, Amit, **Efficient Non-interactive Proof Systems for Bilinear Groups**, *Advances in Cryptology -- EUROCRYPT 2008: 27th Annual International Conference on the Theory and Applications of Cryptographic Techniques, Istanbul, Turkey, April 13-17, 2008. Proceedings (2008), Springer Berlin Heidelberg*

Ghadafi, Essam, Smart, Nigel. P. and Warinschi, Bogdan, **Groth--Sahai Proofs Revisited**, *Public Key Cryptography -- PKC 2010: 13th International Conference on Practice and Theory in Public Key Cryptography, Paris, France, May 26-28, 2010. Proceedings (2010), Springer Berlin Heidelberg*