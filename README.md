# Vaadin Flow -  Simple Java Object Generator

# Getting Started
My small (maybe)useful project.

An web application that create an content of Object Class depend on user's options, with Vaadin Flow Framework.

![](https://raw.githubusercontent.com/qvanphong/pojo-generator/master/screenshot.png)

# Prerequisites
- JDK 8.0
- IDE With integrated Maven
- Nodejs & npm 

# Installing
Clone an import my project to your Java IDE, then execute these commands:
```bash
$ mvn install    # Build package
$ npm install    # Install required nodejs packages
$ mvn jetty:run  # Start Jetty Server
```
You can deploy it to Apache Tomcat instead Jetty.

If you want to run your app locally in the production mode, run `mvn jetty:run -Pproduction`.

## To-dos

- Improve UI
- Complete toString() option
- Annotation
- Import package