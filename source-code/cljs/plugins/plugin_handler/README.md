
# XXX#7061
# A {:query-params {...}} térkép ...
  A {:query-params {...}} térkép minden resolver és mutation lekérés paraméter térképének
  alapja, így annak tartalma elérhető minden meghívott szerver-oldali mutation és resolver függvény
  paraméterei között.

  A body komponens {:path-key ...} és {:items-key ...} paramétereinek értékei esetlegesen szükségesek
  a szerver-oldali mutation és resolver függvényekben, ezért a body komponens React-fába csatolásakor
  a két paraméter értéke eltárolásra kerül a {:query-params {...}} térképben.

  A {:query-params {...}} térkép bevezetése megoldotta, hogy az item-browser plugin az aktuálisan
  böngészett elem azonosítóját az item-lister plugin által küldött :my-handler/get-items resolver függvény
  paraméterei közé írhatja.
  Az item-browser plugin alkalmazza a core.events/set-query-param! függvényt, amivel az aktuálisan böngészett
  elem azonosítóját eltárolja a plugin query-params térképében és az item-browser plugin alapját képező
  item-lister plugin az elemek letöltésekor alkalmazza a core.subs/use-query-params függvényt, amivel kiolvassa
  a plugin query-params térképéből az adatokat és hozzáfűzi a szerver számára küldött többi adathoz.



# Mi az a plugin-handler?
  A plugin-handler a különböző pluginok egymással megegyező függvényeit/eseményeit szabványosítja,
  így elkerülhetők az ismétlődések.
  Pl. Nem szükséges minden pluginban megírni a get-meta-item subscription függvényt ...



# Current-item
  ...



# Single item
  ...



# Multiple items
  ...



# XXX#3907
  A pluginok a letöltött dokumentumokat/tartalmat névtér nélkül tárolják,
  így az egyes Re-Frame feliratkozásokban az egyes értékek olvasása kevesebb
  erőforrást igényel, mivel nem szükséges az értékek kulcsaihoz az aktuális
  névteret hozzáfűzni/eltávolítani.
