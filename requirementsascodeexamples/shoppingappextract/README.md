# shoppingappextract
Example application that extracts the use case model of the [shoppingappjavafx](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/shoppingappjavafx) example
and transforms it to a HTML page. 

As a result of running shoppingappextract, it will print the path to the HTML page 
to the console. 

## Getting started
In order to use shoppingappextract, you need the following libraries on the classpath:
* freemarker-2.3.26-incubating.jar (FreeMarker)
* commons-lang3-3.5.jar (Apache Commons)
* The current shoppingappjavafx jar
* The current shoppingappextract jar
* The current requirementsascodeextract jar
* The current requirementsascodecore jar

If you don't want to download them manually, and you don't use a build system like Gradle,
you can download the shoppingappextract distribution zip from the Releases tab. 

You execute shoppingappextract by running a shell script in the bin subfolder of the distribution.
Make sure that you run it from a console, not by double clicking on the script, so that you receive
the path to the HTML file.
