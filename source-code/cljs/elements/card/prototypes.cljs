
(ns elements.card.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [noop.api                :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; @ignore
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
  ;  :height (keyword)
  ;  :hover-color (keyword)
  ;  :marker-color (keyword)
  ;  :width (keyword)}
  [{:keys [badge-content border-color disabled? marker-color] :as card-props}]
  (merge {:height :auto
          :width  :content}
         (if badge-content {:badge-color :primary :badge-position :tr})
         (if border-color  {:border-position :all
                            :border-width    :xxs})
         (if marker-color  {:marker-position :tr})
         (param card-props)
         (if badge-content {:badge-content (metamorphic-content/compose badge-content)})
         (if disabled?     {:hover-color :none})))
