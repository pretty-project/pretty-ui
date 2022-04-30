
# A {:query-params {...}} térkép
- XXX#7061
- A {:query-params {...}} térkép minden resolver és mutation lekérés paraméter térképének
  alapja, így annak tartalma elérhető minden meghívott szerver-oldali mutation és resolver függvény
  paraméterei között.
- A body komponens {:path-key ...} és {:items-key ...} paramétereinek értékei esetlegesen szükségesek
  a szerver-oldali mutation és resolver függvényekben, ezért a body komponens React-fába csatolásakor
  a két paraméter értéke eltárolásra kerül a {:query-params {...}} térképben.
- A {:query-params {...}} térkép bevezetése megoldotta, hogy az item-browser plugin az aktuálisan
  böngészett elem azonosítóját az item-lister plugin által küldött :my-handler/get-items resolver függvény
  paraméterei közé írhatja.



# XXX#6177
- Az item-browser plugin az item-lister plugin alapjaira épül, ezért ...
  ...
  ... mindkét plugin módosításait úgy kell elvégezni, hogy azok működése egymással összehangolt maradjon!
