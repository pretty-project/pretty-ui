
(ns pretty-elements.chip-group.prototypes
    (:require [fruits.noop.api             :refer [return]]
              [pretty-elements.input.utils :as input.utils]
              [pretty-build-kit.api :as pretty-build-kit]))

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
                                   :on-click [:pretty-elements.chip-group/delete-chip! group-id group-props chip-dex]}
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
          :chips-path (input.utils/default-value-path group-id)}
         (-> group-props)))
