
# XXX#5061
# A head elemben felsorolt CSS fájlok forrása ...
  1. ... a head.config/SYSTEM-CSS-PATHS vektorban felsorolt fájlok.
  2. ... az [:environment/add-css! ...] eseménnyel hozzáadott fájlok.
  3. ... az x.app-config.edn fájl {:css-paths [...]} tulajdonsága.



# XXX#5062
# A body elemben felsorolt JS fájlok forrása ...
  1. ... az aktuális útvonalhoz rendelt core-js fájl.
  2. ... az x.app-config.edn fájl {:plugin-js-paths [...]} tulajdonsága.
  3. ... a body komponens {:plugin-js-paths [...]} paramétere.
