# Assignment 2 Makefile
# Keagan Chasenski
# 20 September 2020

JAVAC = /usr/bin/javac
.SUFFIXES: .java .class

BUILDDIR  = ../..
PACKAGE   = javax.swing

SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=Terrain.class Water.class FlowThread.class FlowPanel.class Flow.class

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

run:
	java -cp $(BINDIR) Flow

docs:
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java

clean:
	rm $(BINDIR)/*.class
