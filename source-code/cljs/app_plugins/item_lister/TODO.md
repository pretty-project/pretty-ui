
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



# XXX#3286



# x
- Az item-lister nem fogadja az elemek on-click és on-right-click eventjét az item-lister plugint
  megvalósító eszköztől, ezért nem tudja eldönteni, hogy az on-right-click eseményt szükséges-e
  meghívni, ezért minden esetben megtörténik a [:my-extension.my-type-lister/item-right-clicked ...]
  esemény, ami esetlegesen a konzolra írja ha ez az esemény nem létezik, mert nincs használatban
