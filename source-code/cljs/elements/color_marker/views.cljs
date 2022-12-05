
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.color-marker.views
    (:require [elements.color-marker.helpers    :as color-marker.helpers]
              [elements.color-marker.prototypes :as color-marker.prototypes]
              [random.api                       :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-marker-color
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ; @param (keyword or string) color
  [marker-id marker-props color]
  [:div.e-color-marker--color (color-marker.helpers/marker-color-attributes marker-id marker-props color)])

(defn- color-marker-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ; {}
  [marker-id {:keys [colors] :as marker-props}]
  (letfn [(f [body color] (conj body [color-marker-color marker-id marker-props color]))]
         (reduce f [:<>] colors)))

(defn- color-marker-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  [marker-id marker-props]
  [:div.e-color-marker--body (color-marker.helpers/marker-body-attributes marker-id marker-props)
                             [color-marker-colors                         marker-id marker-props]])

(defn- color-marker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ; {}
  [marker-id {:keys [colors] :as marker-props}]
  [:div.e-color-marker (color-marker.helpers/marker-attributes marker-id marker-props)
                       [color-marker-body                      marker-id marker-props]])

(defn element
  ; @param (keyword)(opt) marker-id
  ; @param (map) marker-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :colors (keywords or strings in vector)(opt)
  ;   Default: [:highlight]
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :size (keyword)(opt)
  ;   :m, :l, :xl, :xxl
  ;   Default: :s
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [color-marker {...}]
  ;
  ; @usage
  ; [color-marker :my-color-marker {...}]
  ([marker-props]
   [element (random/generate-keyword) marker-props])

  ([marker-id marker-props]
   (let [marker-props (color-marker.prototypes/marker-props-prototype marker-props)]
        [color-marker marker-id marker-props])))
