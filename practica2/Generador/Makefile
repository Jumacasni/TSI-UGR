AR=ar
CXX=g++
CXXFLAGS=-I$(INC) -Wall -g
LDFLAGS=-Llib/
LDLIBS=-lformas
INC=include/
SRC=src/
OBJ=obj/
LIB=lib/
BIN=bin/

all: generaproblema

generaproblema: predicados.o
	$(CXX) -o generaproblema predicados.o generaproblema.cpp

predicados.o: predicados.h predicados.cpp
	$(CXX) -c -g -std=c++11 -o predicados.o predicados.cpp


clean:
	rm -f generaproblema
	rm -f *.o