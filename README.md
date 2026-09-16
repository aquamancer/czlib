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
4. In `src/main/resources/fabric.mod.json` under `depends`, add czlib to prevent Fabric from launching if czlib is not in its `mods` folder:
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