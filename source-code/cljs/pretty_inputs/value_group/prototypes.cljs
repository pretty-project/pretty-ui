
(ns pretty-inputs.value-group.prototypes
    (:require [fruits.noop.api                     :refer [return]]
              [fruits.vector.api                   :as vector]
              [pretty-inputs.value-group.adornments :as value-group.adornments]
              [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-props-prototype
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
  (let [delete-chip-adornment (value-group.adornments/delete-chip-adornment group-id group-props chip-dex chip-value)]
       (merge {:label              (-> chip-value chip-label-f)}
              (if chips-deletable? (-> chip (update :end-adornments vector/conj-item delete-chip-adornment))
                                   (-> chip)))))

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  (merge {:chip-label-f return}
         (-> group-props)))
