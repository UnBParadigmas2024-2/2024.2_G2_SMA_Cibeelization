#!/bin/bash

# Compilar o projeto
mvn clean install

# Executar o projeto
java -cp target/school-simulation-1.0-SNAPSHOT.jar:src/libs/jade.jar:src/libs/commons-codec-1.3.jar com.school.AmbienteEscolar