
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.selection.subs
    (:require [engines.engine-handler.body.subs :as body.subs]
              [engines.engine-handler.core.subs :as core.subs]
              [re-frame.api                     :refer [r]]
              [vector.api                       :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (strings in vector)
  [db [_ engine-id]]
  ; XXX#8891 (source-code/cljs/engines/engine_handler/selection/README.md)
  (r core.subs/get-meta-item db engine-id :selected-items))

(defn export-single-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (string)
  [db [_ engine-id]]
  ; XXX#8891 (source-code/cljs/engines/engine_handler/selection/README.md)
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

(defn all-listed-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [listed-items   (r core.subs/get-listed-items db engine-id)
        selected-items (r core.subs/get-meta-item    db engine-id :selected-items)]
       (letfn [(f [{:keys [id]}] (not (vector/contains-item? selected-items id)))]
              (if (vector/min? listed-items 1)
                  (not (some f listed-items))))))

(defn any-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [selected-items (r core.subs/get-meta-item db engine-id :selected-items)]
       (vector/nonempty? selected-items)))

(defn any-listed-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [listed-items   (r core.subs/get-listed-items db engine-id)
        selected-items (r core.subs/get-meta-item    db engine-id :selected-items)]
       (letfn [(f [{:keys [id]}] (vector/contains-item? selected-items id))]
              (some f listed-items))))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (let [selected-items (r core.subs/get-meta-item db engine-id :selected-items)]
       (empty? selected-items)))

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
