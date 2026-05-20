$ErrorActionPreference = "Stop"

$localJavaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
$localMavenHome = "$env:USERPROFILE\Documents\Code\Tools\apache-maven-3.9.9"

if (Test-Path "$localJavaHome\bin\java.exe") {
    $env:JAVA_HOME = $localJavaHome
    $env:Path = "$env:JAVA_HOME\bin;$env:Path"
}

if (Test-Path "$localMavenHome\bin\mvn.cmd") {
    $env:MAVEN_HOME = $localMavenHome
    $env:Path = "$env:MAVEN_HOME\bin;$env:Path"
}

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    throw "Java nao encontrado. Instale o JDK 21 ou confira o JAVA_HOME."
}

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    throw "Maven nao encontrado. Instale o Maven ou confira a pasta Documents\Code\Tools."
}

mvn clean install
mvn exec:java
