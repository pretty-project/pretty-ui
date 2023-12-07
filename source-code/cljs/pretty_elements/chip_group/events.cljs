
(ns pretty-elements.chip-group.events
    (:require [fruits.vector.api :as vector]
              [re-frame.api      :refer [r]]
              [re-frame.db.api   :as r.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-chip!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:chips-path (Re-Frame path vector)}
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  [db [_ _ {:keys [chips-path]} chip-dex]]
  (r r.db/apply-item! db chips-path vector/remove-nth-item chip-dex))
