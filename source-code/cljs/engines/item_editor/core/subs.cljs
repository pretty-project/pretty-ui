
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.core.subs
    (:require [engines.engine-handler.core.subs  :as core.subs]
              [engines.item-editor.body.subs     :as body.subs]
              [engines.item-editor.download.subs :as download.subs]
              [logical.api                       :refer [nor]]
              [mid-fruits.vector                 :as vector]
              [re-frame.api                      :as r :refer [r]]
              [x.router.api                      :as x.router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-meta-item                core.subs/get-meta-item)
(def engine-synchronizing?        core.subs/engine-synchronizing?)
(def get-current-item-id          core.subs/get-current-item-id)
(def get-current-item             core.subs/get-current-item)
(def export-current-item          core.subs/export-current-item)
(def get-current-item-label       core.subs/get-current-item-label)
(def get-current-item-modified-at core.subs/get-current-item-modified-at)
(def get-auto-title               core.subs/get-auto-title)
(def use-query-prop               core.subs/use-query-prop)
(def use-query-params             core.subs/use-query-params)



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
  (r engine-synchronizing? db editor-id :editor))



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
  ; ... ha az item-id paraméterként átadott azonosítójú elem van megnyitva szerkesztésre.
  ; ... ha az item-editor engine body komponense a React-fába van csatolva.
  (r core.subs/current-item? db editor-id item-id))

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; Mivel az item-viewer engine használja a "/my-route/:item-id" formátumú útvonalat,
  ; ezért az item-editor számára szükséges külön beállítani a "/my-route/create" útvonalat,
  ; hogy annak használatakor ne az item-viewer engine induljon el.
  ; Ezért a "/my-route/create" formátumú útvonalak használatakor az :item-id útvonal-paraméter
  ; nem elérhető, ami miatt az "Új elem hozzáadása" mód megállapítása az útvonal azonosítója
  ; alapján történik.
  (let [current-route-id (r x.router/get-current-route-id db)]
       (= "creator-route" (name current-route-id))))



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
  (let [new-item?      (r new-item?     db editor-id)
        recovery-mode? (r get-meta-item db editor-id :recovery-mode?)]
       (nor new-item? recovery-mode?)))



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
  (let [data-received?        (r download.subs/data-received? db editor-id)
        editor-synchronizing? (r editor-synchronizing?        db editor-id)]
       (or editor-synchronizing? (not data-received?))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
; @param (keyword) item-key
;
; @usage
;  [:item-editor/get-meta-item :my-editor :my-item]
(r/reg-sub :item-editor/get-meta-item get-meta-item)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-item-id :my-editor]
(r/reg-sub :item-editor/get-current-item-id get-current-item-id)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-item :my-editor]
(r/reg-sub :item-editor/get-current-item get-current-item)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/export-current-item :my-editor]
(r/reg-sub :item-editor/export-current-item export-current-item)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-item-label :my-editor]
(r/reg-sub :item-editor/get-current-item-label get-current-item-label)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/get-current-item-modified-at :my-editor]
(r/reg-sub :item-editor/get-current-item-modified-at get-current-item-modified-at)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/new-item? :my-editor]
(r/reg-sub :item-editor/new-item? new-item?)

; @param (keyword) editor-id
;
; @usage
;  [:item-editor/editor-disabled? :my-editor]
(r/reg-sub :item-editor/editor-disabled? editor-disabled?)
