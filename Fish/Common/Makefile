all:
	@echo "Creating JAR file"
	@eval $$( \
	mvn install -Dmaven.test.failure.ignore=true \
	&& cd target \
	&& cp Fish-1.0-SNAPSHOT.jar ../xfish.jar \
	&& cp Fish-1.0-SNAPSHOT-jar-with-dependencies.jar ../xtree.jar)