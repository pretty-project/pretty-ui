
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-marker.views
    (:require [x.app-core.api                         :as a]
              [x.app-elements.color-marker.helpers    :as color-marker.helpers]
              [x.app-elements.color-marker.prototypes :as color-marker.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-marker-color
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ; @param (keyword or string) color
  [marker-id marker-props color]
  [:div.x-color-marker--color (color-marker.helpers/marker-color-attributes marker-id marker-props color)])

(defn color-marker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;  {}
  [marker-id {:keys [colors] :as marker-props}]
  [:div.x-color-marker (color-marker.helpers/marker-attributes marker-id marker-props)
                       (letfn [(f [body color] (conj body [color-marker-color marker-id marker-props color]))]
                              (reduce f [:div.x-color-marker--body] colors))])

(defn element
  ; @param (keyword)(opt) marker-id
  ; @param (map) marker-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :colors (keywords or strings in vector)(opt)
  ;    Default: [:highlight]
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/color-marker {...}]
  ;
  ; @usage
  ;  [elements/color-marker :my-color-marker {...}]
  ([marker-props]
   [element (a/id) marker-props])

  ([marker-id marker-props]
   (let [marker-props (color-marker.prototypes/marker-props-prototype marker-props)]
        [color-marker marker-id marker-props])))
