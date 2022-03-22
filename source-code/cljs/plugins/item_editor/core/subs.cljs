
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.vector                 :as vector]
              [plugins.item-editor.core.helpers  :as core.helpers]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-activities.api              :as activities]
              [x.app-components.api              :as components]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]
              [x.app-elements.api                :as elements]
              [x.app-router.api                  :as router]
              [x.app-sync.api                    :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-namespace item-key]]
  (get-in db [:plugins :item-editor/meta-items extension-id item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (r get-request-id db :my-extension :my-type)
  ;  =>
  ;  :my-handler/synchronize-editor!
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace]]
  ; XXX#8519
  ; - Az item-editor plugin a különböző lekérések elküldéséhez ugyanazt az azonosítót használja,
  ;   mert egy közös azonosítóval egyszerűbb megállapítani, hogy valamelyik lekérés folyamatban
  ;   van-e (az editor-synchronizing? függvénynek elegendő egy request-id azonosítót figyelnie).
  ; - Ha szükséges, akkor a különböző lekéréseket el lehet látni egyedi azonosítóval.
  (let [handler-key (r transfer.subs/get-transfer-item db extension-id item-namespace :handler-key)]
       (keyword (name handler-key) "synchronize-editor!")))

(defn editor-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; XXX#8519
  (let [request-id (r get-request-id db extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (keyword)
  [db [_ _ _]]
  (r router/get-current-route-path-param db :item-id))

(defn get-current-item-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-current-item-id db :my-extension :my-type)
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :item-id))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [item-path (r mount.subs/get-body-prop db extension-id item-namespace :item-path)]
       (get-in db item-path)))

(defn export-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace]]
  (let [current-item (r get-current-item db extension-id item-namespace)]
       (db/document->namespaced-document current-item item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (= (r get-current-item-id      db extension-id item-namespace)
     (r mount.subs/get-body-prop db extension-id item-namespace :new-item-id)))

(defn get-editor-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  ; - Az {:auto-title? ...} tulajdonság a body komponens paramétere, ezért annak React-fába történő
  ;   csatolása után lehetséges eldönteni, hogy szükséges-e az automatikus felirat beállítása.
  ; - A {:route-title ...} tulajdonság a szerver-oldali [:item-editor/init-editor! ...] esemény
  ;   paramétere, és a {:route-title ...} tulajdonságként átadott címke, akkor kerül beállításra,
  ;   ha a szerkesztő NEM {:auto-title? true} beállítással lett elindítva.
  ; - Ha a {:route-title ...} címke beállítása az [:item-editor/handle-route! ...] eseményben történne,
  ;   akkor szükséges lenne vizsgálni a body komponens React-fába történő csatolásának állapotát,
  ;   mert az elem duplikálása után a "Másolat szerkesztése" lehetőséget választva ...
  ;   ... az útvonal megváltozik és az [:item-editor/handle-route! ...] esemény megtörténik,
  ;       ami a {:route-title ...} paraméterként átadott címkét beállítaná az applikáció címkéjeként.
  ;   ... az [:item-editor/body-did-mount ...] esemény nem történne meg újra,
  ;       ami az esetlegesen beállított {:auto-title? true} beállítás szerint, lecserélné
  ;       a {:route-title ...} paraméterként átadott címkét az automatikus címkére.
  ;   Ezért, ha az [:item-editor/handle-route! ...] esemény megtörténésekor a body komponens,
  ;   már a React-fába lenne csatolva, nem volna szükséges beállítani a route-title címkét!
  (if-let [auto-title? (r mount.subs/get-body-prop db extension-id item-namespace :auto-title?)]
          (if-let [new-item? (r new-item? db extension-id item-namespace)]
                  (core.helpers/add-item-label  extension-id item-namespace)
                  (core.helpers/edit-item-label extension-id item-namespace))
          (if-let [route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
                  (return route-title))))

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [current-item (r get-current-item db extension-id item-namespace)]
       (if-let [modified-at (:modified-at current-item)]
               (let [actual-modified-at (r activities/get-actual-timestamp db modified-at)]
                    (components/content {:content :last-modified-at-n :replacements [actual-modified-at]})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-suggestions?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [suggestion-keys (r mount.subs/get-body-prop db extension-id item-namespace :suggestion-keys)]
       (vector/nonempty? suggestion-keys)))

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [new-item? (r new-item? db extension-id item-namespace)]
       (not new-item?)))

(defn download-data?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (or (r download-suggestions? db extension-id item-namespace)
      (r download-item?        db extension-id item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; XXX#3219
  ; Azért szükséges vizsgálni az {:data-received? ...} tulajdonság értékét, hogy a szerkesztő
  ; {:disabled? true} állapotban legyen, amíg NEM kezdődött még el az adatok letöltése!
  (boolean (if-let [download-data? (r download-data? db extension-id item-namespace)]
                   (let [data-received?        (r get-meta-item         db extension-id item-namespace :data-received?)
                         editor-synchronizing? (r editor-synchronizing? db extension-id item-namespace)]
                        (or editor-synchronizing? (not data-received?))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (keyword) item-key
;
; @usage
;  [:item-editor/get-meta-item :my-extension :my-type :my-item]
(a/reg-sub :item-editor/get-meta-item get-meta-item)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/get-current-item :my-extension :my-type]
(a/reg-sub :item-editor/get-current-item get-current-item)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/get-description :my-extension :my-type]
(a/reg-sub :item-editor/get-description get-description)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/new-item? :my-extension :my-type]
(a/reg-sub :item-editor/new-item? new-item?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/editor-disabled? :my-extension :my-type]
(a/reg-sub :item-editor/editor-disabled? editor-disabled?)
