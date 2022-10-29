
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.color-stamp.views
    (:require [elements.color-stamp.helpers    :as color-stamp.helpers]
              [elements.color-stamp.prototypes :as color-stamp.prototypes]
              [elements.label.views            :as label.views]
              [mid-fruits.random               :as random]
              [mid-fruits.vector               :as vector]))



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
                      (conj color-stamp [:div.e-color-stamp--color stamp-color-attributes])))]
             (reduce f [:<>] colors))
      [:div.e-color-stamp--placeholder]))

(defn color-stamp-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  [stamp-id stamp-props]
  [:div.e-color-stamp--body (color-stamp.helpers/stamp-body-attributes stamp-id stamp-props)
                            [color-stamp-colors                        stamp-id stamp-props]])

(defn- color-stamp-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {}
  [_ {:keys [helper info-text label]}]
  (if label [label.views/element {:content   label
                                  :helper    helper
                                  :info-text info-text}]))

(defn color-stamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  [stamp-id stamp-props]
  [:div.e-color-stamp (color-stamp.helpers/stamp-attributes stamp-id stamp-props)
                      [color-stamp-label                    stamp-id stamp-props]
                      [color-stamp-body                     stamp-id stamp-props]])

(defn element
  ; @param (keyword)(opt) stamp-id
  ; @param (map) stamp-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :colors (keywords or strings in vector)(opt)
  ;    []
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :helper (metamorphic-content)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :label (metamorphic-content)(opt)
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [color-stamp {...}]
  ;
  ; @usage
  ;  [color-stamp :my-color-stamp {...}]
  ;
  ; @usage
  ;  [color-stamp :my-color-stamp {:colors ["red" "green" "blue"]}]
  ([stamp-props]
   [element (random/generate-keyword) stamp-props])

  ([stamp-id stamp-props]
   (let [stamp-props (color-stamp.prototypes/stamp-props-prototype stamp-props)]
        [color-stamp stamp-id stamp-props])))
