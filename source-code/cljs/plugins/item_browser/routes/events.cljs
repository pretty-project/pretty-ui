
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.events
    (:require [plugins.item-browser.body.subs   :as body.subs]
              [plugins.item-browser.core.events :as core.events]
              [plugins.item-browser.routes.subs :as routes.subs]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; A) Ha az aktuális útvonalból származtatható az :item-id útvonal-paraméter, ...
  ;    ... akkor a browse-item! függvény item-id paraméterként megkapja a származtatott értéket.
  ;
  ; B) Ha az aktuális útvonalból NEM származtatható az :item-id útvonal-paraméter,
  ;    de a body komponens már a React-fába van csatolva és a Re-Frame adatbázisban már elérhető
  ;    a body komponens {:root-item-id "..."} paramétere, ...
  ;    ... akkor a browse-item! függvény item-id paraméterként megkapja a body komponens
  ;        {:root-item-id "..."} paraméterét.
  ;    Pl.: Ha az item-browser böngésző használata közben a felhasználó visszatér a base-route útvonalra,
  ;         ami NEM tartalmazza az :item-id útvonal-paramétert (pl. a böngésző "Vissza" gombjának használatával).
  ;         Ilyenkor az aktuális útvonalból nem származtatható az :item-id útvonal-paraméter és a React-fába
  ;         csatolt body komponens :component-did-mount életciklusa sem fog megtörténni, ami felhasználná
  ;         a {:root-item-id "..."} paramétert. Ezért a szükséges a browse-item! függénynek átadni
  ;         a {:root-item-id "..."} paraméter értékét, hogy eltárolja az aktuálisan böngészett
  ;         elem azonosítójaként.
  ;
  ; C) Ha az aktuális útvonalból NEM származtatható az :item-id útvonal paraméter,
  ;    és a body komponens NINCS a React-fába csatolva, ...
  ;    ... akkor a body-did-mount függvény által alkalmazott update-item-id! függvény fogja eltárolni
  ;        a body komponens {:root-item-id "..."} paramétereként átadott azonosítót az aktuálisan
  ;        böngészett elem azonosítójaként.
  ;    Pl.: Ha az item-browser plugin base-route útvonalának használata indítja el a plugint,
  ;         ami az útvonal használata előtt nem volt elindítva, ezért a body komponens nincs még
  ;         a React-fába csatolva és a base-route útvonalból nem származtatható az :item-id
  ;         útvonal-paraméter.
  (r core.events/browse-item! db browser-id (or (r routes.subs/get-derived-item-id db browser-id)                  ; A)
                                                (r body.subs/get-body-prop         db browser-id :root-item-id)))) ; B)
