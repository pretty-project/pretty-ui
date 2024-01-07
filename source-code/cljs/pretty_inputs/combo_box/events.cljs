
(ns pretty-inputs.combo-box.events
    (:require [pretty-inputs.input.events :as input.events]
              [pretty-inputs.input.utils  :as input.utils]
              [re-frame.api                 :refer [r]]
              [re-frame.db.api              :as r.db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-did-mount
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  [db [_ box-id box-props]]
  (r input.events/use-initial-options! db box-id box-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [option-value-f value-path]} option]]
  (let [option-value (option-value-f option)]
       (if (input.utils/value-path->vector-item? value-path)
           (r r.db/set-vector-item! db value-path option-value)
           (r r.db/set-item!        db value-path option-value))))
