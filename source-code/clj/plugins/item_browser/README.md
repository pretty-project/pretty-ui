
# XXX#2045
# Miért szerver-oldali paraméter a {:handler-key ...} tulajdonság?



# plugins.item-browser.update.helpers (x4.6.8#1)
- Az item->path és item-id->path függvények használják a kliens-oldali body komponens
  :path-key paraméterének értékét, amit a kliens-oldali item-browser plugin minden lekéréskor
  elküld a szerver-számára, ezért az elérhető az összes resolver és mutation függvény számára.
- Az item->path és item-id->path függvények mutation függvények env térképét is fogadhatják,
  ezért a müködésükhöz szükséges, hogy a pathom/env->param függvény mutation függvények env térképől
  is képes legyen kiolvasni a paramétereket (ez nem gyári Pathom funkció).
- Az item->path és item-id->path függvények számára szükséges {:item-path ...} tulajdonság szerver-oldalra
  küldése miatt került bevezetésre a pluginok kliens-oldali {:default-query-params {...}} térképe (XXX#7061).
  Így az env térképből kiolvasható a {:path-key ...} tulajdonság értéke a mutation és resolver függvényekben
  miközben a {:path-key ...} megmaradhatott kliens-oldali paraméternek.
