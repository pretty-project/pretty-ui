
# XXX#5061
# A head elemben megjelenített adatok forrása
  A head elemben megjelenített adatotok (pl. meta-title, og-preview-path, ...) ...
  ... elsődleges forrása a head komponensnek head-props paraméterként átadott térkép.
  ... másodlagos forrása az x.website-config.edn fájlban beállított értéket.



# XXX#5061
# A head elemben felsorolt CSS fájlok forrása
  1. A head.config/SYSTEM-CSS-PATHS vektorban felsorolt fájlok.
  2. Az x.app-config.edn fájl {:css-paths [...]} tulajdonsága.
  3. A head komponens {:css-paths [...]} paramétere.
  4. Az [:environment/add-css! ...] eseménnyel hozzáadott fájlok.
