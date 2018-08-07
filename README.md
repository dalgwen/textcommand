
# textcommand
Java framework/library designed to run arbitrary code, triggered by sending text command in natural langage. The text can come from various interface (http, dbus, and more soon).

Simple use case :
Start by coding your functions with the proper annotation :

~~~~
@Command("echo $what")
public String echo(String what) {
    return what;
}
~~~~
Register it whitin the application

~~~~
CommandFinder.registerClass(EchoCommands.class);
~~~~

Then, from one of the interface provided (dbus, http post),  type :

> echo hello world

The input string `echo hello world` will match your method, thanks to the `@Command` annotation.
Your code will then be executed with the parameter(s) correctly valued. Depending on the invocation interface, you can get the response (http response, dbus response, etc.). In the above example, you would of course get "hello world".

The `Command` annotation supports some syntaxic sugar for designing methods which can respond to different calls, with several spelling or different vocabulary :

~~~~
@Command(priority = 10, value = "(greet|say hello to) $people1 (and|or) $people2")
~~~~

The method annoted with the above `@Command` would be called by the following text :

> say hello to Luke and Obiwan

And also with the following text :

> greet Yoda and Quigon

This make you free from the burden of handling regular expression.
For example (this is my main use case), you could design your home automation system, which will turn the light or other system by using the proper API, and make it responsive to several command in natural language. Then make it accessible by several ways with the proper interface already included.

Plan for the future :
- adding several other interface (SMS, xmpp, email, voice control)
