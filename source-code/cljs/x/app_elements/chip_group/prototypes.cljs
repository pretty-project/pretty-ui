
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.chip-group.prototypes
    (:require [mid-fruits.candy             :refer [param return]]
              [x.app-elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {}
  ; @param (integer) chip-dex
  ; @param (*) chip
  ;
  ; @return (map)
  ;  {}
  [group-id {:keys [chip-label-f deletable?] :as group-props} chip-dex chip]
  (if deletable? {:label                (chip-label-f chip)
                  :primary-button-event [:elements.chip-group/delete-chip! group-id group-props chip-dex]}
                 {:label                (chip-label-f chip)}))

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {}
  [group-id group-props]
  (merge {:chip-label-f return
          :value-path (input.helpers/default-value-path group-id)}
         (param group-props)))
