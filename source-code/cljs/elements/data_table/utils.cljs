
(ns elements.data-table.utils
    (:require [noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-preset
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:presets (map)(opt)}
  ; @param (map) n
  ; {:preset (keyword)(opt)}
  ;
  ; @return (map)
  [_ {:keys [presets]} {:keys [preset] :as n}]
  (if preset (merge (preset presets) n)
             (return n)))
