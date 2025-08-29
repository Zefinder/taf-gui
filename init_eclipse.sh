#!/bin/bash

# Constants
CURRENT_DIR=`pwd`

CLASSPATH_FILE='.classpath'
SETTINGS_DIR='.settings'

CUSTOM_ERRORS_WARNINGS='errors_warnings'
ECLIPSE_ERRORS_WARNINGS='.settings/org.eclipse.jdt.core.prefs'

CUSTOM_JAUTODOC='jautodoc'
ECLIPSE_JAUTODOC='.settings/net.sf.jautodoc.prefs'

ECLIPSE_ENCODING='.settings/org.eclipse.core.resources.prefs'

ARCHIVE_NAME="processor.jar"

APT='.settings/org.eclipse.jdt.apt.core.prefs'

FACTORY_PATH='.factorypath'

CLASSPATH_CONTENT='<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="src" output="testbin" path="tests">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="src" output="processorbin" path="processor"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.junit.JUNIT_CONTAINER/5"/>
	<classpathentry kind="src" path=".apt_generated">
		<attributes>
			<attribute name="optional" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="src" output="testbin" path=".apt_tests">
		<attributes>
			<attribute name="optional" value="true"/>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="output" path="bin"/>
</classpath>
'

ENCODING_CONTENT='eclipse.preferences.version=1
encoding/<project>=UTF-8
'

APT_CONTENT='eclipse.preferences.version=1
org.eclipse.jdt.apt.aptEnabled=true
org.eclipse.jdt.apt.genSrcDir=.apt_generated
org.eclipse.jdt.apt.genTestSrcDir=.apt_tests
org.eclipse.jdt.apt.reconcileEnabled=true
'

FACTORY_PATH_CONTENT='<factorypath>
    <factorypathentry kind="EXTJAR" id="'$CURRENT_DIR'/'$ARCHIVE_NAME'" enabled="true" runInBatchMode="false"/>
</factorypath>
'

# Check if classpath exists, if so modify it!
printf "%s" "$CLASSPATH_CONTENT" > "$CLASSPATH_FILE"
echo "Added modification to classpath!"

# Check if .settings exists
if [[ ! -d "$SETTINGS_DIR" ]]
then
    mkdir "$SETTINGS_DIR"
fi

# Set custom errors and warnings
mv "$CUSTOM_ERRORS_WARNINGS" "$ECLIPSE_ERRORS_WARNINGS"
echo "Set custom errors and warnings"

# Set JAutoDoc options
mv "$CUSTOM_JAUTODOC" "$ECLIPSE_JAUTODOC"
echo "Set JAutoDoc options (even if plugin is not installed)"

# Set the project encoding set
printf "%s" "$ENCODING_CONTENT" > "$ECLIPSE_ENCODING"

# Compile and generate jar of processor annotation
javac -d ./processorbin/ ./processor/com/*.java ./src/com/taf/annotation/FactoryObject.java
cd ./processorbin
jar cvf ../$ARCHIVE_NAME *
cd ..

# Create apt file
printf "%s" "$APT_CONTENT" > "$APT"

# Create factory path file
printf "%s" "$FACTORY_PATH_CONTENT" > "$FACTORY_PATH"

echo "Init finished, you can now rebuild the project (or restart Eclipse) and install JUnit 5 (in whatever order)"