
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.body.events
    (:require [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.plugin-handler.body.subs   :as body.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [plugins.plugin-handler.core.subs   :as core.subs]))
              ;[x.app-core.api                     :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ plugin-id body-props]]
  (assoc-in db [:plugins :plugin-handler/body-props plugin-id] body-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/body-props plugin-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ plugin-id body-props]]
  (update-in db [:plugins :plugin-handler/body-props plugin-id] merge body-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]])
  ; - Az update-item-id! függvény a body komponens React-fába csatolásakor alkalmazható,
  ;   hogy felülvizsgálja az aktuális elem azonosítóját.
  ;
  ; - Az update-item-id! függvény felülvizsgálja, ...
  ;   ... hogy az aktuális elem azonosítója eltárolásra került-e (az aktuális útvonalból),
  ;       vagy szükséges a body komponens {:item-id "..."} paramétereként átadott értéket beállítani.
  ;
  ; - Ha az update-item-id! függvény alkalmazásakor az aktuális elem azonosítója ...
  ;   A) ... már eltárolásra került, akkor NEM használja a body komponens {:item-id "..."}
  ;          paraméterének értékét.
  ;   B) ... még NEM került eltárolásra és a body komponens paraméterként megkapta az {:item-id "..."}
  ;          tulajdonságot, akkor azt eltárolja az aktuális elem azonosítójaként.
  ;          Az plugin body komponensének ...
  ;          ... {:item-id "..."} tulajdonsága is lehet az aktuális elem azonosítójának forrása,
  ;              így lehetséges az aktuális elem azonosítóját a body komponens paramétereként is átadni.
  ;(r core.events/set-item-id! db plugin-id (or (r core.subs/get-current-item-id db plugin-id)             ; A)
  ;                                             (r body.subs/get-body-prop       db plugin-id :item-id)]]) ; B)

(defn update-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]])
  ; - Az update-view-id! függvény a body komponens React-fába csatolásakor alkalmazható,
  ;   hogy felülvizsgálja az aktuális nézet azonosítóját.
  ;
  ; - Az update-view-id! függvény felülvizsgálja, ...
  ;   ... hogy az aktuális nézet azonosítója eltárolásra került-e (az aktuális útvonalból),
  ;       vagy szükséges a body komponens {:default-view-id ...} paramétereként átadott értéket beállítani.
  ;
  ; - Ha az update-view-id! függvény alkalmazásakor az aktuális nézet azonosítója ...
  ;   A1) ... már eltárolásra került, és a body komponens számára {:allowed-view-ids [...]}
  ;           paraméterként átadott vektor tartalmazza, akkor nem használja a body komponens
  ;           {:default-view-id ...} paraméterének értékét.
  ;   A2) ... már eltárolásra került, de a ... {:allowed-view-ids [...]} paraméterként átadott
  ;           vektor NEM tartalmazza, akkor a body komponens {:default-view-id ...} paraméterének
  ;           értékét eltárolja az aktuális nézet azonosítójaként.
  ;   B) ... még NEM került eltárolásra és a body komponens paraméterként megkapta
  ;          a {:default-view-id ...} tulajdonságot, akkor azt eltárolja az aktuális nézet azonosítójaként.
  ;
  ; - Ha az applikáció használata közbeni útvonal-váltáskor a body komponens React-fába csatolása
  ;   után történne meg az aktuális nézet azonosítójának származtatása az aktuálisan
  ;   böngészett útvonalból, ...
  ;   ... akkor az update-view-id! függvény alkalmazása már megtörtént a body komponens
  ;       React-fába csatolásakor.
  ;   ... az update-view-id! függvény már nem fogja felülvizsgálni az aktuális nézet azonosítóját.
  ;   Ez nem okoz problémát, mivel az applikáció használata közbeni útvonal-váltáskor nem szükséges
  ;   a felülvizsgálat, mert nem valószínű, hogy az applikáció valamely hivatkozása hibás lenne.
  ;   A felülvizsgálat az applikáció valamely útvonalon történő indításakor szükséges, mivel
  ;   a külső forrásból (link, könyvjelző) származó útvonal hibás vagy elavult lehet.
  ;(let [default-view-id (r body.subs/get-body-prop db plugin-id :default-view-id)]
  ;     (if-let [current-view-id (r core.subs/get-current-view-id db plugin-id)]
               ; A)
  ;             (let [allowed-view-ids (r body.subs/get-body-prop db plugin-id :allowed-view-ids)]
  ;                  (if (vector/contains-item? allowed-view-ids current-view-id)
                        ; A1)
  ;                      (return db)
                        ; A2)
  ;                      (r core.events/set-view-id! db plugin-id default-view-id)
               ; B)
  ;             (r core.events/set-view-id! db plugin-id default-view-id)]])
