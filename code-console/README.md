# Code Console

This module provides an implementation of the `ICodeConsole` interface
from the `code-console-api` module as `UniversalConsole`. It's an
universal console able to support any language declared throw the
implementation of `ILanguagePack` interface.

### Provided languages

Several languages are already implemented into language pack.

##### Groovy

`GroovyLanguagePack` fully support the Groovy langue (up to version
2.5.4) except some functionalities :

 - Dependency management with `Grapes`. Due to the OSGI structure of
 the application, the dependency management can be tricky. There is
 two cases :
   - Dependency used only within the script : if the dependency is only
   used inside the script and doesn't need acces to the system
   ClassLoader, the `@Grab`, `@Grapes`, `GrabResolver` ... annotations
    can be used.
   - Dependency using the system CLassLoader : if the dependency need
   to access to the system ClassLoader (with the annotation
   `@GrabConfig`) like the case of the SQL Driver
   (postgresql, mysql, H2 ...), the classes from the module
   `bundle-manager` should be use instead.
