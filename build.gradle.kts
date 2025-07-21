plugins {
    id("java")
    application
    id("com.gradleup.shadow") version "9.0.0-rc1"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "br.edu.ifmg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("br.edu.ifmg.poo.Main")
}

javafx {
    version = "21.0.3"
    modules("javafx.controls", "javafx.fxml")
}
dependencies {
    implementation("org.openjfx:javafx-controls:21.0.3")
    implementation("org.openjfx:javafx-fxml:21.0.3")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}