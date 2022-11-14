
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-sorter.helpers
    (:require [engines.item-sorter.state :as state]
              [re-frame.api              :as r]
              [reagent.api               :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ;
  ; @return (vector)
  [sorter-id]
  [:engines :item-sorter/items sorter-id])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-order-changed-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ; @param (map) sorter-props
  ;  {:value-path (vector)}
  [_ {:keys [value-path]} items]
  (r/dispatch-sync [:x.db/set-item value-path items]))

(defn on-drag-start-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ; @param (map) sorter-props
  [_ _])

(defn on-drag-end-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ; @param (map) sorter-props
  [_ _])
