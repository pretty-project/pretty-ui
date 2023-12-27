
(ns pretty-elements.search-field.prototypes
    (:require [pretty-elements.plain-field.env :as plain-field.env]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  (let [field-empty? (plain-field.env/field-empty? field-id)]
       (merge (if on-enter {:start-adornments [{:icon :search :on-click on-enter :tab-indexed? false :disabled? field-empty?}]}
                           {:start-adornments [{:icon :search}]})
              (-> field-props))))
