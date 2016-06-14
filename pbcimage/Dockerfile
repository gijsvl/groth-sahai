FROM atlassian/default-image

RUN wget https://crypto.stanford.edu/pbc/files/pbc-0.5.14.tar.gz
RUN tar -zxvf pbc-0.5.14.tar.gz
RUN apt-get update
RUN apt-get -y install flex bison libgmp3-dev cmake
RUN cd pbc-0.5.14 && ./configure && make && make install
RUN wget http://downloads.sourceforge.net/project/jpbc/jpbc_2_0_0/jpbc-2.0.0.tar.gz
RUN tar -zxvf jpbc-2.0.0.tar.gz
RUN cd jpbc-pbc && cmake . && make

CMD /bin/bash
