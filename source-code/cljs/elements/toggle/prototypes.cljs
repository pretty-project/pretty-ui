
(ns elements.toggle.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)}
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :hover-color (keyword)
  ;  :marker-position (keyword)}
  [{:keys [border-color disabled? marker-color] :as toggle-props}]
  (merge (if marker-color {:marker-position :tr})
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param toggle-props)
         (if disabled? {:hover-color :none})))
