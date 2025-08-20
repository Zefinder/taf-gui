#!/bin/bash

# Constants
CLASSPATH_FILE='.classpath'
SETTINGS_DIR='.settings'

CUSTOM_ERRORS_WARNINGS='errors_warnings'
ECLIPSE_ERRORS_WARNINGS='.settings/org.eclipse.jdt.code.prefs'

CUSTOM_JAUTODOC='jautodoc'
ECLIPSE_JAUTODOC='.settings/net.sf.jautodoc.prefs'

ECLIPSE_ENCODING='.settings/org.eclipse.core.resources.prefs'

CLASSPATH_CONTENT='<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="src" output="testbin" path="tests">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.junit.JUNIT_CONTAINER/5"/>
	<classpathentry kind="output" path="bin"/>
</classpath>
'

ENCODING_CONTENT='eclipse.preferences.version=1
encoding/<project>=UTF-8
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

echo "Init finished, you can now rebuild the project (or restart Eclipse) and install JUnit 5 (in whatever order)"