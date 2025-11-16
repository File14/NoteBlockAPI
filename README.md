# NoteBlockAPI

This is a modified version of the original [NoteBlockAPI](https://github.com/koca2000/NoteBlockAPI) plugin, designed to
let developers shade the API directly into
their plugin without requiring the separate NoteBlockAPI plugin to be installed on the server.

**Important:** According to the original authors’ license, your plugin must be open-source if you shade this
code into your plugin.

This project also aims to support newer Minecraft versions (1.20+) only, while cleaning up and modernizing the existing
codebase.

#### Maven Dependency

```xml

<repository>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repository>
<dependencies>
<dependency>
    <groupId>com.github.File14</groupId>
    <artifactId>NoteBlockAPI</artifactId>
    <version>1.6.5-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
</dependencies>
```

#### Gradle Dependency

```groovy
repositories {
    maven {
        url 'https://jitpack.io/'
    }
}
dependencies {
    compileOnly "com.github.File14:NoteBlockAPI:1.6.5-SNAPSHOT"
}
```

You can initialize the api using:

```java
NoteBlockAPI noteBlockAPI = NoteBlockAPI.initAPI(this);
```

On plugin disable:

```java
noteBlockAPI.shutdown();
```