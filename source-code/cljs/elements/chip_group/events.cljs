
(ns elements.chip-group.events
    (:require [re-frame.api :refer [r]]
              [vector.api   :as vector]
              [x.db.api     :as x.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-chip!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (vector)}
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]} chip-dex]]
  (r x.db/apply-item! db value-path vector/remove-nth-item chip-dex))
