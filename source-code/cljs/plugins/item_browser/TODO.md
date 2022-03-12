
# XXX#3286
- Az item-browser plugin jelenlegi indítása az [:item-lister/load-lister! ...] esemény meghívásával
  kezdődik, ami meghívja a [:my-extension.my-type-browser/load-browser! ...] eseményt, ami kirendereli
  az item-browser komponenst.
  - Ez egy egyszerű és egyértelmű mód, ahol először megtörténnek a state
    változásai, az item-browser felkészül a kirenderelésre (pl. elkezdi letölteni az elemeket),
    majd egy esemény kirendereli az item-browser komponenst.
  - Egy másik megközelítés szerint az item-browser komponenst kellene először a React-fába csatolni,
    majd a {:component-did-mount ...} életciklus indítaná el az [:item-browser/load-browser! ...]
    eseményt. Mindkettőnek vannak előnyei és hátrányai is ...



# BUG#5140
- Ha az item-browser-ben kitörölsz egy elemet, akkor az item-lister/reload-items! esemény fut le
  utána, nem pedig az item-browser/reload-items! ami miatt az aktuálisan böngészett elem adatai nem
  töltődnek újra le, mert nem fut le az item-browser/request-item! amit az item-browser/reload-items!
  hívna meg
