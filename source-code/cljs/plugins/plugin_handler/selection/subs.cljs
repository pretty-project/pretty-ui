
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.selection.subs
    (:require [mid-fruits.vector                 :as vector]
              [plugins.plugin-handler.body.subs  :as body.subs]
              [plugins.plugin-handler.core.subs :as core.subs]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (integer)
  [db [_ plugin-id]]
  (let [selected-items (r core.subs/get-meta-item db plugin-id :selected-items)]
       (count selected-items)))

(defn all-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (let [selected-items-count  (r get-selected-item-count             db plugin-id)
        downloaded-item-count (r core.subs/get-downloaded-item-count db plugin-id)]
       (and (not= downloaded-item-count 0)
            (= selected-items-count downloaded-item-count))))

(defn any-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (let [selected-items (r core.subs/get-meta-item db plugin-id :selected-items)]
       (vector/nonempty? selected-items)))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (let [selected-items (r core.subs/get-meta-item db plugin-id :selected-items)]
       (-> selected-items vector/nonempty? not)))

(defn item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ plugin-id item-id]]
  (let [selected-items (r core.subs/get-meta-item db plugin-id :selected-items)]
       (vector/contains-item? selected-items item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (strings in vector)
  [db [_ plugin-id]]
  ; XXX#8891
  ; Az export-selection függvény visszatérési értéke a kijelölt listaelemek
  ; azonosítói egy vektorban felsorolva.
  (r core.subs/get-meta-item db plugin-id :selected-items))

(defn export-single-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (string)
  [db [_ plugin-id]]
  ; XXX#8891
  (let [exported-selection (r export-selection db plugin-id)]
       (first exported-selection)))

(defn get-imported-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (strings in vector)
  [db [_ plugin-id]]
  ; XXX#8891
  (get-in db [:plugins :plugin-handler/meta-items plugin-id :imported-selection]))
