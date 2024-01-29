
(ns pretty-inputs.search-field.prototypes
    (:require [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-enter (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:start-adornments (maps in vector)}
  [field-id {:keys [on-enter] :as field-props}]
  (let [field-empty? (pretty-inputs.engine/input-empty? field-id field-props)]
       (merge (if on-enter {:start-adornments [{:icon :search :on-click on-enter :tab-indexed? false :disabled? field-empty?}]}
                           {:start-adornments [{:icon :search}]})
              (-> field-props))))
