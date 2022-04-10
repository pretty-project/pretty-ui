
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.subs
    (:require [plugins.item-browser.mount.subs    :as mount.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-router.api                   :as router]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def get-extended-route routes.subs/get-extended-route)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (string)
  [db [_ browser-id]]
  ; A get-derived-item-id függvény visszatérési értéke:
  ;
  ; A) az aktuális útvonalból származtatott :item-id útvonal-paraméter értéke, ...
  ;    ... ha az aktuális útvonal tartalmazza az :item-id útvonal-paramétert
  ;        (= az aktuális útvonal nem a base-route útvonal).
  ;
  ; B) a body komponens {:root-item-id "..."} paraméterének értéke, ...
  ;    ... ha az aktuális útvonal NEM tartalmazza az :item-id útvonal-paramétért.
  ;    ... a body komponens a React-fába van csatolva és megkapta a {:root-item-id "..."} paramétert.
  ;    Pl.: Ha az item-browser böngésző használata közben a felhasználó visszatér a base-route útvonalra,
  ;         ami NEM tartalmazza az :item-id útvonal-paramétert (pl. a böngésző "Vissza" gombjának használatával).
  ;         Ilyenkor az aktuális útvonalból nem származtatható az :item-id útvonal-paraméter és a React-fába
  ;         csatolt body komponens :component-did-mount életciklusa sem fog megtörténni, ami felhasználná
  ;         a {:root-item-id "..."} paramétert.
  ;
  ; C) NIL, ha a get-derived-item-id függvény használatakor ...
  ;    ... az aktuális útvonal NEM tartalmazza az :item-id útvonal-paramétert.
  ;    ... a body komponens NINCS még a React-fába csatolva vagy NEM kapta meg a {:root-item-id "..."} paramétert.
  ;    Pl.: Ha az item-browser plugin base-route útvonalának használata indítja el a plugint,
  ;         ami az útvonal használata előtt nem volt elindítva, ezért a body komponens nincs még a React-fába csatolva.
  (or (r router/get-current-route-path-param db :item-id)                   ; A)
      (r mount.subs/get-body-prop            db browser-id :root-item-id))) ; B)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-route
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @example
  ;  (r item-browser/get-item-route db :my-browser "my-item")
  ;  =>
  ;  "/@app-home/my-browser/my-item"
  ;
  ; @return (string)
  [db [_ browser-id item-id]]
  (r get-extended-route db browser-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-browser/get-item-route :my-browser "my-item"]
(a/reg-sub :item-browser/get-item-route get-item-route)
