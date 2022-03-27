
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.vector                 :as vector]
              [plugins.item-editor.core.helpers  :as core.helpers]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [plugins.plugin-handler.core.subs  :as core.subs]
              [x.app-activities.api              :as activities]
              [x.app-components.api              :as components]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]
              [x.app-elements.api                :as elements]
              [x.app-router.api                  :as router]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def get-request-id        core.subs/get-request-id)
(def plugin-synchronizing? core.subs/plugin-synchronizing?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (r plugin-synchronizing? db editor-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (keyword)
  [db [_ _]]
  (r router/get-current-route-path-param db :item-id))

(defn get-current-item-id
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  (r item-editor/get-current-item-id db :my-editor)
  ;
  ; @return (string)
  [db [_ editor-id]]
  (r get-meta-item db editor-id :item-id))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [item-path (r mount.subs/get-body-prop db editor-id :item-path)]
       (get-in db item-path)))

(defn export-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (namespaced map)
  [db [_ editor-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)
        current-item   (r get-current-item                db editor-id)]
       (db/document->namespaced-document current-item item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (= (r get-current-item-id      db editor-id)
     (r mount.subs/get-body-prop db editor-id :new-item-id)))

(defn get-editor-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (metamorphic-content)
  [db [_ editor-id]]
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
  (if-let [auto-title? (r mount.subs/get-body-prop db editor-id :auto-title?)]
          (if-let [new-item? (r new-item? db editor-id)]
                  (let [item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)]
                       (core.helpers/add-item-label  editor-id item-namespace))
                  (let [item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)]
                       (core.helpers/edit-item-label editor-id item-namespace)))
          (if-let [route-title (r transfer.subs/get-transfer-item db editor-id :route-title)]
                  (return route-title))))

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [db [_ editor-id]]
  (let [current-item (r get-current-item db editor-id)]
       (if-let [modified-at (:modified-at current-item)]
               (let [actual-modified-at (r activities/get-actual-timestamp db modified-at)]
                    (components/content {:content :last-modified-at-n :replacements [actual-modified-at]})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-suggestions?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (let [suggestion-keys (r mount.subs/get-body-prop db editor-id :suggestion-keys)]
       (vector/nonempty? suggestion-keys)))

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (let [new-item? (r new-item? db editor-id)]
       (not new-item?)))

(defn download-data?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (or (r download-suggestions? db editor-id)
      (r download-item?        db editor-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; XXX#3219
  ; Azért szükséges vizsgálni az {:data-received? ...} tulajdonság értékét, hogy a szerkesztő
  ; {:disabled? true} állapotban legyen, amíg NEM kezdődött még el az adatok letöltése!
  (boolean (if-let [download-data? (r download-data? db editor-id)]
                   (let [data-received?        (r get-meta-item         db editor-id :data-received?)
                         editor-synchronizing? (r editor-synchronizing? db editor-id)]
                        (or editor-synchronizing? (not data-received?))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
; @param (keyword) item-key
;
; @usage
;  [:item-editor/get-meta-item :my-editor :my-item]
(a/reg-sub :item-editor/get-meta-item get-meta-item)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-item :my-editor]
(a/reg-sub :item-editor/get-current-item get-current-item)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-description :my-editor]
(a/reg-sub :item-editor/get-description get-description)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/new-item? :my-editor]
(a/reg-sub :item-editor/new-item? new-item?)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/editor-disabled? :my-editor]
(a/reg-sub :item-editor/editor-disabled? editor-disabled?)
