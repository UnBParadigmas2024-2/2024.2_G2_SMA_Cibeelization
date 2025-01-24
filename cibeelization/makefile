PATH_PROJECT_JAR = target/cibeelization-1.0-SNAPSHOT.jar
PATH_LIBS    = src/libs
LIBS_JAR    = $(PATH_LIBS)/jade.jar:$(PATH_LIBS)/jadeExamples.jar:$(PATH_LIBS)/commons-codec-1.3.jar
PROJECT_CLASS_PATH = $(LIBS_JAR):$(PATH_PROJECT_JAR)
PROJECT_GROUP    = com.bee
JADE_AGENTS = QueenBee:$(PROJECT_GROUP).QueenBee;BeeLarva:$(PROJECT_GROUP).BeeLarva
JADE_FLAGS 		 = -gui -agents "$(JADE_AGENTS)"

.PHONY:
	clean
	build-and-run

build-and-run:
	@echo "Gerando a build e executando o projeto"
	make build run

build:
	@echo "Gerando a build do projeto"
	make build-libs build-maven

build-libs:
	@echo "Instalando JAR libs externas"
	make install-jade install-commons install-examples-jade

build-maven:
	@echo "Build do projeto"
	./mvnw clean install

run:
	@echo "Executando o projeto com a última build criada"
	java -cp $(PROJECT_CLASS_PATH) jade.Boot $(JADE_FLAGS)

clean:
	@echo "Removendo a build do projeto"
	./mvnw clean

build-and-run-win:
	@echo "Gerando a build e executando o projeto"
	make build-win run

build-win:
	@echo "Gerando a build do projeto"
	.\mvnw install

install-jade:
	@echo "Instalando Jade"
	./mvnw clean install:install-file -Dfile=$(PATH_LIBS)/jade.jar \
   -DgroupId=jade \
   -DartifactId=jade \
   -Dversion=4.6.0 \
   -Dpackaging=jar

install-commons:
	@echo "Instalando Commons"
	./mvnw clean install:install-file -Dfile=$(PATH_LIBS)/commons-codec-1.3.jar \
   -DgroupId=commons-codec \
   -DartifactId=commons-codec \
   -Dversion=1.3.0 \
   -Dpackaging=jar

install-examples-jade:
	@echo "Instalando exemplos jade"
	./mvnw clean install:install-file -Dfile=$(PATH_LIBS)/jadeExamples.jar \
   -DgroupId=jadeExamples \
   -DartifactId=jadeExamples \
   -Dversion=1.0.0 \
   -Dpackaging=jar