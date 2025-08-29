# TAF-GUI: Testing Automation Framework - Graphical User Interface

Welcome to the official graphical interface repository of the TAF software. Because writing an XML file to describe a model and writing constraints was not user friendly, the goal was to make TAF accessible for everyone. This includes:
- Creating the model
- Modifying a model and including all mandatory parameters for every entity
- Creating all mandatory files to run TAF and simplifying the run settings
- Displaying the python line of code to access a certain parameter of the model for the `Export.py`

This repository is made in **Java 21** and tries to respect all TAF specifications. It is better to use Eclipse to run this project since the installation is made for Eclipse (but if you want to use another IDE, feel free to do so)

## How to configure the workspace? 
I made the choice to not use Maven to limit the needs of external libraries. There are three source folders: `src` containing the source code of the GUI, `tests` containing all unit tests, and `processor` containing the code of the annotation processor. The unit tests use **JUnit Jupiter** (JUnit 5). 

Custom annotations are used for **null static analysis**:
- **Nullable** to specify that a value can be **null**
- **NotNull** to specify that a value **cannot be null**

The annotation **NotEmpty** is here for documentation purpose and specifies that an array, list or String value is not empty.

Custom annotations, coupled with java annotation processors, are used to verify the code structure (and sometimes generate code) (WIP):
- **Manager** is used for managers and will verify that the manager class is a singleton
- **EventMethod** is used for event listeners and will verify that it is compliant with the event method description
- **FactoryObject** is used to tell that objects are created with a factory and generate factories (WIP)

### How to configure with Eclipse?
If you are on Linux, you can run the script `init_eclipse.sh` **after adding the project into Eclipse** and it will install everything for you. If you don't know how to import a project into Eclipse, here are the steps (after cloning the repo):
- `File -> New -> Java Project`
- Enter a project name (`tafgui` is a good name)
- In the project location, put the location of the clone (`/home/myuser/work/taf-gui` for example)

You can also clone the git repository in Eclipse:
- Open the Git perspective (`Window -> Perspective -> Open Perspective -> Other... -> Git`)
- Clone a Git repository and choose `GitHub`
- In the search bar, type `Zefinder/taf-gui` and `Next`, `Next`
- Choose where to clone the repository (you can leave the default location, it is as you like) and `Finish`
- Right click on the git repository and `Import Projects...` and `Finish`

You will still need to install JUnit 5. Go to `Project -> Properties` and select `Java Build Path`. In the `Libraries` tab, click on `Classpath`, then `Add Library` and `JUnit`. Select `JUnit 5` and `Finish`.

If you are not on Linux, then you cannot execute the script. Here are the steps that you will need to do:

To show the "dot files", click on the three vertical dots in the package explorer, `Filters...` and deselect the `.* resources`.

To add `tests` as a test source folder, add the following lines in `.classpath` (you might need to allow different output folders):
```xml
<classpathentry kind="src" output="testbin" path="tests">
	<attributes>
		<attribute name="test" value="true"/>
	</attributes>
</classpathentry>
<classpathentry kind="src" output="processorbin" path="processor"/>
```

For the warning and errors, create the `.settings` folder if it does not exist and create (or replace the content of) `.settings/org.eclipse.jdt.code.prefs` by the content of `errors_warnings`. Once you did this you can delete the `errors_warnings` file.

Set the encoding set to UTF-8 (in the `Problems` tab, right click on the problem and `Quick Fix`.

For the custom annotations, create a new file in `.settings` called `org.eclipse.jdt.core.perfs` and fill it with:
```
eclipse.preferences.version=1
org.eclipse.jdt.apt.aptEnabled=true
org.eclipse.jdt.apt.genSrcDir=.apt_generated
org.eclipse.jdt.apt.genTestSrcDir=.apt_tests
org.eclipse.jdt.apt.reconcileEnabled=true
```

After everything done, restart Eclipse, click on the project, refresh (`F5` or right click and `refresh`), and rebuild (`Project -> Clean...`). 

### JAutoDoc

If you want to use the JAutoDoc plugin, install it and create (or replace the content of) `.settings/net.sf.jautodoc.prefs` by the content of `jautodoc`. Once you did this, you can delete the `jautodoc` file.

## What is TAF?
TAF is an academic software developed in LAAS-CNRS by researchers in the Trust team. It is a generator that takes a data model described in a XML file and outputs random test cases (according to a given template). For more information, and for the TAF documentation, please go to the TAF website: [https://wp.laas.fr/taf/](https://wp.laas.fr/taf/). This repository also contains documentation that can be useful to understand what can be put in the XML file if you want to modify it by hand. 

## How to install TAF
TAF can be installed by cloning its git repository: `https://redmine.laas.fr/laas/taf.git` and following the instructions from there. To use TAF from the graphical interface, you need to set the path using the `Settings -> Path settings` menu in the menu bar of any menu (even the run menu).