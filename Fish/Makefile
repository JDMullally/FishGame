all:
	@echo "Creating JAR file"
	@eval $$( \
	cd Common && mvn install \
	&& cd target \
	&& mv Fish-1.0-SNAPSHOT.jar ../xfish.jar \
	&& mv Fish-1.0-SNAPSHOT-tests.jar ../xtest.jar \
	&& cd .. && rm -r -f target)