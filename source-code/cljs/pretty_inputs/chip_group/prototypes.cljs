
(ns pretty-inputs.chip-group.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]
              [fruits.vector.api :as vector]
              [pretty-inputs.chip-group.adornments :as chip-group.adornments]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {}
  ; @param (integer) chip-dex
  ; @param (*) chip-value
  ;
  ; @return (map)
  ; {}
  [group-id {:keys [chip chip-label-f chips-deletable?] :as group-props} chip-dex chip-value]
  (let [delete-chip-adornment (chip-group.adornments/delete-chip-adornment group-id group-props chip-dex chip-value)]
       (merge {:label        (-> chip-value chip-label-f)}
              (if chips-deletable? (-> chip (update :end-adornments vector/conj-item delete-chip-adornment))
                                   (-> chip)))))

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [group-props]
  (merge {:chip-label-f return}
         (-> group-props)))
