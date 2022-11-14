
# Szerver-oldali és kliens-oldali útvonalak
  - Szerver-oldali útvonalak:
    Ha egy útvonal rendelkezik :get vagy :post tulajdonságokkal, akkor az útvonal-kezelő ...
    ... szerver-oldali útvonalnak tekinti.
    ... a szerver inicializálásakor átadja az útvonal tulajdonságait a Reitit számára,
        amely így létező (200) útvonalként értelmezi.
    ... eltárolja a [:x.router :route-handler/server-routes] Re-Frame adatbázis útvonalon
        (a kliens-oldalra jellemző tulajdonságok nélkül).

    Ha egy útvonal NEM rendelkezik :get vagy :post tulajdonságokkal, akkor az útvonal-kezelő ...
    ... NEM tekinti szerver-oldali útvonalnak.
    ... az útvonal használatakor az útvonal-kezelő NEM tér vissza 404 hibával, mivel lehetséges,
        hogy az útvonal hozzá van adva a rendszerhez kliens-oldali útvonalként és a kliens-oldali
        útvonal-kezelő fogja eldönteni, hogy létező útvonalnak tekinti-e az aktuális útvonalat!

  - Kliens-oldali útvonalak:
    Ha egy útvonal rendelkezik :client-event vagy :on-leave-event tulajdonságokkal,
    akkor az útvonal-kezelő ...
    ... kliens-oldali útvonalnak tekinti.
    ... a kliens inicializálásakor elküldi az útvonal tulajdonságait a kliens számára.
    ... eltárolja a [:x.router :route-handler/client-routes] Re-Frame adatbázis útvonalon
        (a szerver-oldalra jellemző tulajdonságok nélkül).

  + Az egyes útvonalak egyszerre lehetnek kliens- és szerver-oldali útvonalak is,
    ha rendelkeznek a szükséges tulajdonságokkal!



# Szerver-oldali útvonalak kezelése
# XXX#7706
  ...



# Kliens-oldali útvonalak kezelése
# XXX#7707
  A kliens-oldalra küldött útvonalak tulajdonságaiból a szerver-oldalra jellemző
  tulajdonságok biztonsági okokból minden esetben eltávolításra kerülnek!



# Útvonalak tulajdonságainak gyorsítótárazása  
# XXX#7708
  Az egyes útvonalak (és fájlok) kiszolgálásakor ...
  ... szükséges az útvonal tulajdonságait elérhetővé tenni a kiszolgáláskor
      használt függvények számára.
  ... a lekérés az aktuális útvonalat tartalmazza, miközben a szerver az útvonalak
      sablonjait tárolja (route-template)!
      Pl.: "/my-item"  (route)
           "/:item-id" (route-template)
  ... az aktuális útvonal alapján szükséges megkeresni, hogy melyik tárolt útvonal
      sablonja illeszkedik az aktuális útvonalhoz, hogy az ahhoz tartozó tulajdonságokat
      az útvonalkezelő elérhetővé tehesse a lekérés számára.
  ... a tárolt útvonalak sablonjai közötti kereséskor a sablonoknak olyan sorrendben
      kell legyenek, mint, ahogy az ordered-routes vektort visszaadja az útvonal-kezelő!




# Miért a kliens-oldali útvonalkezelő kezeli a nem létező útvonalakat?
  Ha a szerver-oldali útvonalkezelő kezelné a nem létező útvonalakat (404),
  akkor az egyes útvonalak hozzáadásakor szükséges lenne az útvonal :get tulajdonságát
  is meghatározni (át kellene adni az applikációt kiszolgáló html szerkezetet)!
  Viszont így, hogy a kliens-oldali ... kezeli a nem létező útvonalakat (is),

  1. Ha a szerver-oldali útvonalkezelő kezeli ...
     [:x.router/add-route! :my-route {:route-template "/my-route"
                                      :get #(fn [request] [:html [:head] [:body]])
                                      :client-event [:my-event]}]

  2. Ha a kliens-oldali útvonalkezelő kezeli ...
     [:x.router/add-route! :my-route {:route-template "/my-route"
                                      :client-event   [:my-event]}]
                                      :js-build       :app
