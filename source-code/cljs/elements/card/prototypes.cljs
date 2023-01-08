
(ns elements.card.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :marker-color (keyword)}
  [{:keys [badge-content marker-color] :as card-props}]
  (merge (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (param card-props)))
