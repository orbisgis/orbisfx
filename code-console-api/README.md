# Code Console API

This module is dedicated to the definition of the interfaces for the
creation of a code console use to write, execute, export script in
differents languages.

### Code Console

The Code Console is a dockable UI element used to write script in a
specific language. It may also have other functionality like the text
coloration, menu action (execute, open, save ...).

The language support is given throught the method
`addLanguagePack(ILanguagePack languagePack)`. For more information,
see the section `Language Pack`.

### Language Pack

The language pack provides the support of a language in the code console.
For that, for each language, at least the interface `ILanguagePack`
should be implemented.

To provide additional functionality, more interface can be implemented :

##### Language Coloration

The interface `ILanguageColoration` provides methods used to add the text
coloration if the code console. The implementation of
`generateColoration(CodeArea codeArea, ExecutorService executorService)`
should register in the CodeArea object the tasks managed by the
ExecutorService which apply the coloration. The methods also return the
URI of a generated CSS file which then should be registered in the
java FX Scene

##### Language Action

The interface `ILanguageAction` provides a methods `getMenuActions()`
which returns the list of java FX `Menu`. Those nodes will be add to the
code console menur bar.

The interface contains also different `get...EventHandler()` methods.
Those methods are not necessarily defined and can just return a `null`
value. They are defined if the interface to provides some basic action
which should be preferably supported.