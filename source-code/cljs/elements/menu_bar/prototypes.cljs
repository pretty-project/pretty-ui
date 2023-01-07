
(ns elements.menu-bar.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) bar-props
  ; {:orientation (keyword)(opt)}
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :font-weight (keyword)
  ;  :height (keyword)
  ;  :horizontal-align (keyword)
  ;  :orientation (keyword)}
  [{:keys [orientation] :as bar-props}]
  (merge {:font-size   :s
          :font-weight :bold
          :height      :xxl
          :orientation :horizontal}
         (if-not (= orientation :vertical)
                 {:horizontal-align :left})
         (param bar-props)))

(defn item-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) item-props
  ; {:badge-content (metamorphic-content)(opt)
  ;  :icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {:badge-color (keyword)
  ;  :badge-position (keyword)
  ;  :icon-family (keyword)}
  [{:keys [badge-content icon marker-color] :as item-props}]
  (merge (if badge-content {:badge-color :primary :badge-position :tr})
         (if marker-color  {:marker-position :tr})
         (if icon          {:icon-family :material-icons-filled})
         (param item-props)))
