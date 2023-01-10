
(ns elements.card.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :hover-color (keyword)
  ;  :marker-color (keyword)}
  [{:keys [badge-content disabled? marker-color] :as card-props}]
  (merge (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (param card-props)
         (if disabled? {:hover-color :none})))
