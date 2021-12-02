TeachYourselfLsp4j
====== 

The code for the [Getting Started with Language Server Protocol with LSP4J](https://linuxtut.com/en/c53e4dddb0709492d362/) tutorial - which disappeared from BitBucket, but was for some weird reason rescued by https://bitbucket-archive.softwareheritage.org/projects/mi.html - so I could restore it and put it here.

Client
--------
This is the source code for a VS Code plugin that will run the LSP.
It is currently not working because of path issues, but that may be easy to fix.
The dependencies are quite out of date, but it runs at the time of writing.

Server
-------
This is the source code for the language server from the tutorial.
It has been updated to current Java standards.

`gradlew assemble`

will make a runnable jar that talks LSP over stdin and stdout.

`java -jar build/libs/TeachYourselfLsp4j-1.0-SNAPSHOT.jar`

will run it.
