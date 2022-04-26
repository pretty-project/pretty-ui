
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.vector                 :as vector]
              [plugins.item-editor.body.subs     :as body.subs]
              [plugins.item-editor.core.helpers  :as core.helpers]
              [plugins.item-editor.download.subs :as download.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [plugins.plugin-handler.core.subs  :as core.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item                core.subs/get-meta-item)
(def plugin-synchronizing?        core.subs/plugin-synchronizing?)
(def get-current-item-id          core.subs/get-current-item-id)
(def get-current-view-id          core.subs/get-current-view-id)
(def get-current-item             core.subs/get-current-item)
(def export-current-item          core.subs/export-current-item)
(def get-current-item-modified-at core.subs/get-current-item-modified-at)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (r core.subs/get-request-id db editor-id :editor))

(defn editor-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (r plugin-synchronizing? db editor-id :editor))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editing-item?
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-editor/editing-item? db :my-editor "my-item")
  ;
  ; @return (boolean)
  [db [_ editor-id item-id]]
  ; Az editing-item? függvény visszatérési értéke akkor TRUE, ...
  ; ... ha az item-editor plugin body komponense a React-fába van csatolva.
  ; ... ha az item-id paraméterként átadott azonosítójú elem van megnyitva szerkesztésre.
  (r core.subs/current-item? db editor-id item-id))

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (= "create" (r get-current-item-id db editor-id)))

(defn get-editor-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (metamorphic-content)
  [db [_ editor-id]]
  ; - Az {:auto-title? ...} tulajdonság a body komponens paramétere, ezért annak React-fába
  ;   csatolása után lehetséges eldönteni, hogy szükséges-e az automatikus felirat beállítása.
  ;
  ; - A {:route-title ...} tulajdonság a szerver-oldali [:item-editor/init-editor! ...] esemény
  ;   paramétere, és a {:route-title ...} tulajdonságként átadott címke, akkor kerül beállításra,
  ;   ha a szerkesztő NEM {:auto-title? true} beállítással lett elindítva.
  ;
  ; - Ha a {:route-title ...} címke beállítása az [:item-editor/handle-route! ...] eseményben történne,
  ;   akkor szükséges lenne vizsgálni a body komponens React-fába csatolásának állapotát,
  ;   mert az elem duplikálása után a "Másolat szerkesztése" lehetőséget választva ...
  ;   ... az útvonal megváltozik és az [:item-editor/handle-route! ...] esemény megtörténik,
  ;       ami a {:route-title ...} paraméterként átadott címkét beállítaná az applikáció címkéjeként.
  ;   ... az [:item-editor/body-did-mount ...] esemény nem történne meg újra,
  ;       ami az esetlegesen beállított {:auto-title? true} beállítás szerint, lecserélné
  ;       a {:route-title ...} paraméterként átadott címkét az automatikus címkére.
  ;   Ezért, ha az [:item-editor/handle-route! ...] esemény megtörténésekor a body komponens,
  ;   már a React-fába lenne csatolva, nem volna szükséges beállítani a route-title címkét!
  (if-let [auto-title? (r body.subs/get-body-prop db editor-id :auto-title?)]
          (if-let [new-item? (r new-item? db editor-id)]
                  (let [item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)]
                       (core.helpers/add-item-label  editor-id item-namespace))
                  (let [item-namespace (r transfer.subs/get-transfer-item db editor-id :item-namespace)]
                       (core.helpers/edit-item-label editor-id item-namespace)))
          (if-let [route-title (r transfer.subs/get-transfer-item db editor-id :route-title)]
                  (return route-title))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-suggestions?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  (let [suggestion-keys (r body.subs/get-body-prop db editor-id :suggestion-keys)]
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
  (if-let [download-data? (r download-data? db editor-id)]
          (let [data-received?        (r download.subs/data-received? db editor-id)
                editor-synchronizing? (r editor-synchronizing?        db editor-id)]
               (or editor-synchronizing? (not data-received?)))))



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
;  [:item-editor/get-current-item-id :my-editor]
(a/reg-sub :item-editor/get-current-item-id get-current-item-id)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-item :my-editor]
(a/reg-sub :item-editor/get-current-item get-current-item)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-item-modified-at :my-editor]
(a/reg-sub :item-editor/get-current-item-modified-at get-current-item-modified-at)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-view-id :my-editor]
(a/reg-sub :item-editor/get-current-view-id get-current-view-id)

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
