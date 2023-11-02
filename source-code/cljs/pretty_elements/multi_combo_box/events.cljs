
(ns pretty-elements.multi-combo-box.events
    (:require [re-frame.api    :refer [r]]
              [re-frame.db.api :as r.db]
              [vector.api      :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ; @param (*) selected-option
  ;
  ; @return (map)
  [db [_ _ {:keys [option-value-f value-path]} selected-option]]
  (let [option-value (option-value-f selected-option)]
       (r r.db/apply-item! db value-path vector/conj-item-once option-value)))

(defn use-field-content!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:field-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  ; @param (*) field-content
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]} field-content]]
  (let [field-value (field-value-f field-content)]
       (r r.db/apply-item! db value-path vector/conj-item-once field-value)))
