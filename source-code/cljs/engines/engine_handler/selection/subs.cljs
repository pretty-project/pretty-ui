
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.selection.subs
    (:require [engines.engine-handler.body.subs :as body.subs]
              [engines.engine-handler.core.subs :as core.subs]
              [mid-fruits.vector                :as vector]
              [re-frame.api                     :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (strings in vector)
  [db [_ engine-id]]
  ; XXX#8891 (cljs/engines/engine-handler/selection/README.md)
  ; Az export-selection függvény visszatérési értéke a kijelölt listaelemek
  ; azonosítói egy vektorban felsorolva.
  (r core.subs/get-meta-item db engine-id :selected-items))

(defn export-single-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (string)
  [db [_ engine-id]]
  ; XXX#8891 (cljs/engines/engine-handler/selection/README.md)
  (let [exported-selection (r export-selection db engine-id)]
       (first exported-selection)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (integer)
  [db [_ engine-id]]
  (let [selected-items (r core.subs/get-meta-item db engine-id :selected-items)]
       (count selected-items)))

(defn all-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [selected-items-count  (r get-selected-item-count             db engine-id)
        downloaded-item-count (r core.subs/get-downloaded-item-count db engine-id)]
       (and (not= downloaded-item-count 0)
            (= selected-items-count downloaded-item-count))))

(defn all-downloaded-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [downloaded-items (r core.subs/get-downloaded-items db engine-id)
        selected-items   (r core.subs/get-meta-item        db engine-id :selected-items)]
       (letfn [(f [{:keys [id]}] (not (vector/contains-item? selected-items id)))]
              (if (vector/min? downloaded-items 1)
                  (not (some f downloaded-items))))))

(defn any-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [selected-items (r core.subs/get-meta-item db engine-id :selected-items)]
       (vector/nonempty? selected-items)))

(defn any-downloaded-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [downloaded-items (r core.subs/get-downloaded-items db engine-id)
        selected-items   (r core.subs/get-meta-item        db engine-id :selected-items)]
       (letfn [(f [{:keys [id]}] (vector/contains-item? selected-items id))]
              (some f downloaded-items))))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [selected-items (r core.subs/get-meta-item db engine-id :selected-items)]
       (-> selected-items vector/nonempty? not)))

(defn item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ engine-id item-id]]
  (let [selected-items (r core.subs/get-meta-item db engine-id :selected-items)]
       (vector/contains-item? selected-items item-id)))
