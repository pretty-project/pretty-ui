
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.vector                  :as vector]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.events    :as core.events]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.plugin-handler.body.events :as body.events]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.events
(def store-body-props!  body.events/store-body-props!)
(def remove-body-props! body.events/remove-body-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; - Az update-item-id! függvény a body komponens React-fába csatolásakor felülvizsgálja
  ;   az aktuálisan szerkesztett elem azonosítóját.
  ;
  ; - Ha az [:item-editor/body-did-mount ...] esemény megtörténtekor az aktuálisan szerkesztett
  ;   elem azonosítója ...
  ;
  ;   A) ... már eltárolásra került, akkor NEM használja a body komponens {:item-id "..."}
  ;          paraméterének értékét.
  ;
  ;   B) ... még NEM került eltárolásra és a body komponens paraméterként megkapta az {:item-id "..."}
  ;          tulajdonságot, akkor azt eltárolja az aktuálisan szerkesztett elem azonosítójaként.
  ;          Az item-editor plugin body komponensének ...
  ;          ... {:item-id "..."} tulajdonsága is lehet a szerkesztett elem azonosítójának forrása,
  ;              így lehetséges a szerkesztett dokumentum azonosítóját a body komponens paramétereként is átadni.
  (r core.events/set-item-id! db editor-id (or (r core.subs/get-current-item-id db editor-id)             ; A)
                                               (r body.subs/get-body-prop       db editor-id :item-id)))) ; B)

(defn update-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; - Az update-view-id! függvény a body komponens React-fába csatolásakor felülvizsgálja
  ;   az aktuálisan beállított nézet azonosítóját.
  ;
  ; - Ha az [:item-editor/body-did-mount ...] esemény megtörténtekor az aktuálisan beállított
  ;   nézet azonosítója ...
  ;
  ;   A1) ... már eltárolásra került, és az allowed-view-ids vektor tartalmazza, akkor nem
  ;           használja a body komponens {:default-view-id ...}  paraméterének értékét.
  ;
  ;   A2) ... már eltárolásra került, de az allowed-view-ids vektor NEM tartalmazza, akkor
  ;           a body komponens {:default-view-id ...} paraméterének értékét eltárolja az aktuálisan
  ;           beállított nézet azonosítójaként.
  ;
  ;   B) ... még NEM került eltárolásra és a body komponens paraméterként megkapta a {:default-view-id ...}
  ;          tulajdonságot, akkor azt eltárolja az aktuálisan beállított nézet azonosítójaként.
  ;
  ; - Ha a body komponens React-fába csatolása után történne meg az [:item-editor/handle-route! ...]
  ;   esemény, akkor az update-view-id! függvény már nem fog megtörténni, hogy ellenőrizze
  ;   aktuális útvonalból származtatott nézet azonosítóját, mivel nem valószínű, hogy az applikáció
  ;   valamely hivatkozása hibás lenne és az útvonalban szereplő :view-id útvonal-paraméter
  ;   nem szerepelne az allowed-view-ids vektorban.
  ;   Az útvonalból származtatott :view-id útvonal-paramétert azért szükséges ellenőrizni, hogy
  ;   szerepel-e az allowed-view-ids vektorban, mert az applikáción kívülről megnyitott
  ;   útvonal esetleg hibás, vagy elavult lehet.
  (let [default-view-id (r body.subs/get-body-prop db editor-id :default-view-id)]
       (if-let [current-view-id (r core.subs/get-current-view-id db editor-id)]
               ; A)
               (let [allowed-view-ids (r body.subs/get-body-prop db editor-id :allowed-view-ids)]
                    (if (vector/contains-item? allowed-view-ids current-view-id)
                        ; A1)
                        (return db)
                        ; A2)
                        (r core.events/set-view-id! db editor-id default-view-id)))
               ; B)
               (r core.events/set-view-id! db editor-id default-view-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:initial-item (map)(opt)
  ;   :item-path (vector)}
  ;
  ; @return (map)
  [db [_ editor-id {:keys [initial-item item-path] :as body-props}]]
  ; Az item-editor plugin body komponensének ...
  ; ... {:initial-item {...}} tulajdonságával megadható a szerkesztett dokumentum kezdeti állapota ...
  ;     ... így elkerülhető az input mezők {:initial-value ...} tulajdonságának használata, ami miatt
  ;         a plugin felhasználói változtatás nélküli elhagyásakor az tévesen felajánlaná
  ;         a "Nem mentett változtatások visszaállítása" lehetőséget.
  ;     ... így beállíthatók a dokumentum felhasználó által nem szerkeszthető tulajdonságai.
  (cond-> db :store-body-props! (as-> % (r store-body-props! % editor-id body-props))
             :update-view-id!   (as-> % (r update-view-id!   % editor-id))
             :update-item-id!   (as-> % (r update-item-id!   % editor-id))
             initial-item       (assoc-in item-path initial-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (as-> db % (r core.events/remove-meta-items! % editor-id)
             (r core.events/reset-downloads!   % editor-id)
             (r remove-body-props!             % editor-id)))
