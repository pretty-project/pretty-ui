
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.core.subs
    (:require [engines.engine-handler.core.subs   :as core.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.download.subs :as download.subs]
              [mid-fruits.vector                  :as vector]
              [re-frame.api                       :as r :refer [r]]))



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
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (r core.subs/get-request-id db handler-id :handler))

(defn handler-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (r engine-synchronizing? db handler-id :handler))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handling-item?
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r handling-item? db :my-handler "my-item")
  ;
  ; @return (boolean)
  [db [_ handler-id item-id]]
  ; A handling-item? függvény visszatérési értéke akkor TRUE, ...
  ; ... ha az item-id paraméterként átadott azonosítójú elem van megnyitva kezelésre.
  ; ... ha az item-handler engine body komponense a React-fába van csatolva.
  (r core.subs/current-item? db handler-id item-id))

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [current-item-id (r get-current-item-id db handler-id)]
       (= "create" current-item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-suggestions?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [suggestion-keys (r body.subs/get-body-prop db handler-id :suggestion-keys)]
       (vector/nonempty? suggestion-keys)))

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [new-item? (r new-item? db handler-id)]
       (not new-item?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handler-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  ; XXX#3219
  ; Azért szükséges vizsgálni az {:data-received? ...} tulajdonság értékét, hogy
  ; a kezelő {:disabled? true} állapotban legyen, amíg még NEM kezdődött el az
  ; adatok letöltése!
  (let [data-received?         (r download.subs/data-received? db handler-id)
        handler-synchronizing? (r handler-synchronizing?       db handler-id)]
       (or handler-synchronizing? (not data-received?))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) handler-id
; @param (keyword) item-key
;
; @usage
;  [:item-handler/get-meta-item :my-handler :my-item]
(r/reg-sub :item-handler/get-meta-item get-meta-item)

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/get-current-item-id :my-handler]
(r/reg-sub :item-handler/get-current-item-id get-current-item-id)

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/get-current-item :my-handler]
(r/reg-sub :item-handler/get-current-item get-current-item)

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/export-current-item :my-handler]
(r/reg-sub :item-handler/export-current-item export-current-item)

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/get-current-item-label :my-handler]
(r/reg-sub :item-handler/get-current-item-label get-current-item-label)

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/get-current-item-modified-at :my-handler]
(r/reg-sub :item-handler/get-current-item-modified-at get-current-item-modified-at)

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/new-item? :my-handler]
(r/reg-sub :item-handler/new-item? new-item?)

; @param (keyword) handler-id
;
; @usage
;  [:item-handler/handler-disabled? :my-handler]
(r/reg-sub :item-handler/handler-disabled? handler-disabled?)
