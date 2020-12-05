all:
	@echo "Creating JAR file"
	mvn install -DskipTests -Dmaven.test.failure.ignore=true
	cp target/xgui-jar-with-dependencies.jar xgui.jar
	cp target/xserver-jar-with-dependencies.jar xserver.jar
	cp target/xclient-jar-with-dependencies.jar xclient.jar

clean:
	rm *.jar
