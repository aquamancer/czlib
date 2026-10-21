# czlib
An API that provides party information and events during runs of the Celestial Zenith in Monumenta MMO

**You must have a Depths Trinket in your inventory for czlib to update**

**For Fabric 1.20.4 only**


### Provided Information
* All info presented in the Depths Trinket (teammates and their abilities and grave timer)
* Party members' equipped charms
* Current room type and number

### Events
Refer to the [events](src/main/java/com/aquamancer/czlib/api/event) package


## Installation
Move the czlib JAR file into your Minecraft profile's `mods` folder


## Building

Windows:
`gradlew.bat build`

Linux: `./gradlew build`

Output .jar is located at `build/libs/`

## Developers
### Setting up czlib as a dependency
The following example uses **Gradle**, whose mod template was generated [here](https://fabricmc.net/develop/template)
1. Build czlib
2. Copy `czlib-X.X.X.jar` (and optionally `czlib-X.X.X-sources.jar` for IDE support) to a stable directory, e.g. `yourmod/libs/`
3. In `build.gradle`, under `dependencies`, add czlib as a compile-only dependency:
   ```groovy
   dependencies {
      ...
      modCompileOnly files("libs/czlib-X.X.X.jar")
      ...
   }
4. (Optional) In `src/main/resources/fabric.mod.json` under `depends`, you may add czlib to prevent Fabric from launching if czlib is not present:
   ```json
   "depends": {
      ...
      "czlib": "X.X.X",
      ...
   }
5. Run `./gradlew --refresh-dependencies` (Linux/Mac) or `gradlew.bat --refresh-dependencies` (Windows)
   1. You may have to press "Sync Gradle Changes" in IntelliJ
6. (Optional) If using IntelliJ, you can get czlib's comments/documentation:
   1. Copy `czlib-X.X.X-sources.jar` to a stable directory if you haven't already
   2. File > Project Structure > Find `remapped.unspecified:czlib-<...>` > Add > Select `czlib-X.X.X-sources.jar`

### Using the API
1. Get the state: `ZenithApi.getInstance()`
2. Register events, e.g.: `ZenithApiStateEvents.ROOM_SPAWNED.register((room, isWildcard) -> {})`. All changes to the state of the API have an associated event.