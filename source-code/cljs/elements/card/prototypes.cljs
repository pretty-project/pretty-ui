
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
  ;  :cursor (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :href (string)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-content (string)
  ;  :badge-position (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :cursor (keyword)
  ;  :height (keyword)
  ;  :hover-color (keyword)
  ;  :marker-color (keyword)
  ;  :width (keyword)}
  [{:keys [badge-content border-color cursor disabled? href marker-color on-click] :as card-props}]
  ; XXX#5603 (source-code/cljs/elements/button/prototypes.cljs)
  (merge {:height :auto
          :width  :content}
         (if badge-content {:badge-color     :primary
                            :badge-position  :tr})
         (if border-color  {:border-position :all
                            :border-width    :xxs})
         (if marker-color  {:marker-position :tr})
         (if href          {:cursor          :pointer})
         (if on-click      {:cursor          :pointer})
         (param card-props)
         (if badge-content {:badge-content (metamorphic-content/compose badge-content)})
         (if disabled?     {:cursor      (or cursor :default)
                            :hover-color :none})))
