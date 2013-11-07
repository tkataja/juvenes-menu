juvenes-menu
============

This is a ClojureScript web application to fetch the Juvenes TTY lunch menus.

# Building and running the application
ClojureScript can be compiled, for example, automatically when code changes
or once. There are three different build options which
uses different level of optimizations (minification, unused code removal)

##### Building and running
        lein cljsbuild [auto|once] [dbg|pre|prod]
        lein ring server

If the project was compiled using the *dbg* option, you can connect the
repl to the browser.

##### Browser REPL
        lein trampoline cljsbuild repl-listen        

