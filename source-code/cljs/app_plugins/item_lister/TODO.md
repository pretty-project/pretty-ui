
# app-plugins.item-lister.views
- Ha az [:item-lister/reload-lister! ...] esemény megtörténése után megváltozik az elemek száma,
  akkor a újrarenderelődnek az egyes elemek (pl fájlkezelőben kikapcsolt cache mellett villannak
  a bélyegképek)



# XXX#3907
- Ha az item-lister plugin nem végez adatműveletet a letöltött dokumentumokon, akkor
  nincs szükség a dokumentumokból a névteret eltávolítani.
  A névtér eltávolítása a {:handle-favorites? ...} és {:handle-archived? ...} tulajdonságok
  kivezetése előtt volt szükséges.

- Az {:item-actions ...} tulajdonság nincs befejezve
