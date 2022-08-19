
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-stamp.views
    (:require [mid-fruits.vector                     :as vector]
              [x.app-core.api                        :as a]
              [x.app-elements.color-stamp.helpers    :as color-stamp.helpers]
              [x.app-elements.color-stamp.prototypes :as color-stamp.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-stamp-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {:colors (strings in vector)(opt)}
  [stamp-id {:keys [colors] :as stamp-props}]
  (if (vector/nonempty? colors)
      (letfn [(f [color-stamp color]
                 (let [stamp-color-attributes (color-stamp.helpers/stamp-color-attributes stamp-id stamp-props color)]
                      (conj color-stamp [:div.x-color-stamp--color stamp-color-attributes])))]
             (reduce f [:<>] colors))
      [:div.x-color-stamp--placeholder]))

(defn color-stamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  [stamp-id stamp-props]
  [:div.x-color-stamp (color-stamp.helpers/stamp-attributes stamp-id stamp-props)
                      [color-stamp-colors                   stamp-id stamp-props]])

(defn element
  ; @param (keyword)(opt) stamp-id
  ; @param (map) stamp-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :colors (keywords or strings in vector)(opt)
  ;    []
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
  ;  [elements/color-stamp {...}]
  ;
  ; @usage
  ;  [elements/color-stamp :my-color-stamp {...}]
  ;
  ; @usage
  ;  [elements/color-stamp :my-color-stamp {:colors ["red" "green" "blue"]}]
  ([stamp-props]
   [element (a/id) stamp-props])

  ([stamp-id stamp-props]
   (let [stamp-props (color-stamp.prototypes/stamp-props-prototype stamp-props)]
        [color-stamp stamp-id stamp-props])))
