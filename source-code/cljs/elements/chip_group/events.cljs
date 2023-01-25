
(ns elements.chip-group.events
    (:require [re-frame.api    :refer [r]]
              [re-frame.db.api :as r.db]
              [vector.api      :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-chip!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (vector)}
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]} chip-dex]]
  (r r.db/apply-item! db value-path vector/remove-nth-item chip-dex))
