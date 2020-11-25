all:
	@echo "Creating JAR file"
	mvn install -DskipTests -Dmaven.test.failure.ignore=true
	cp target/xboard-jar-with-dependencies.jar xboard.jar
	cp target/xstate-jar-with-dependencies.jar xstate.jar
	cp target/xtree-jar-with-dependencies.jar xtree.jar
	cp target/xstrategy-jar-with-dependencies.jar xstrategy.jar
	cp target/xref-jar-with-dependencies.jar xref.jar
	cp target/xgui-jar-with-dependencies.jar xgui.jar

clean:
	rm *.jar
