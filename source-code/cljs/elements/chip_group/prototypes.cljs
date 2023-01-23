
(ns elements.chip-group.prototypes
    (:require [noop.api               :refer [param return]]
              [elements.input.helpers :as input.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {}
  ; @param (integer) chip-dex
  ; @param (*) chip
  ;
  ; @return (map)
  ; {}
  [group-id {:keys [chip-label-f deletable?] :as group-props} chip-dex chip]
  (if deletable? {:primary-button {:icon     :close
                                   :on-click [:elements.chip-group/delete-chip! group-id group-props chip-dex]}
                  :label (chip-label-f chip)}
                 {:label (chip-label-f chip)}))

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [group-id group-props]
  (merge {:chip-label-f return
          :value-path (input.helpers/default-value-path group-id)}
         (param group-props)))
