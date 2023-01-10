
(ns elements.toggle.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) toggle-props
  ; {:disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:hover-color (keyword)
  ;  :marker-position (keyword)}
  [{:keys [disabled? marker-color] :as toggle-props}]
  (merge (if marker-color {:marker-position :tr})
         (param toggle-props)
         (if disabled? {:hover-color :none})))
