
# Mi az a plugin-handler?
- A plugin-handler a különböző pluginok egymással megegyező függvényeit/eseményeit szabványosítja,
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
