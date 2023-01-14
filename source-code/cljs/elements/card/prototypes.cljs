
(ns elements.card.prototypes
    (:require [candy.api        :refer [param]]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :border-color (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :hover-color (keyword)
  ;  :marker-color (keyword)}
  [{:keys [badge-content border-color disabled? marker-color] :as card-props}]
  (merge (if badge-content {:badge-color :primary :badge-position :tr})
         (if border-color  {:border-position :all
                            :border-width    :xxs})
         (if marker-color  {:marker-position :tr})
         (param card-props)
         (if badge-content {:badge-content (x.components/content badge-content)})
         (if disabled?     {:hover-color :none})))
