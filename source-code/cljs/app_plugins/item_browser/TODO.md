
#

 - [:my-extension.my-type-LISTER/->item-clicked ...] az esemény neve az item-browser plugin
   használatakor

- Az {:item-actions ...} tulajdonság nincs befejezve



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
