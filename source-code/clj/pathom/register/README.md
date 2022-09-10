
# ...
- A Pathom handlereket lehetséges handler függvényenként illetve handler
  függvénycsoportonként regisztrálni a reg-handler! es reg-handlers! függvényekkel.
  Így az esetlegesen használaton kívüli modulok es névterek handler függvényei
  nem regisztrálódnak a Pathom környezetbe.

- Az egyes handler függvények és handler függvénycsoportok egyedi azonosítóval
  kerülnek regisztrálásra. Így biztosítható, hogy a módosított forrásfájlok
  wrap-reload eszköz általi újratöltésekor ne regisztrálják többször ugyanazt
  a handler függvényt vagy függvénycsoportot a Pathom környezetbe.
