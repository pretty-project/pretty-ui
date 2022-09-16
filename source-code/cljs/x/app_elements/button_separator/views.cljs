
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button-separator.views
    (:require [mid-fruits.random                          :as random]
              [x.app-elements.button-separator.helpers    :as button-separator.helpers]
              [x.app-elements.button-separator.prototypes :as button-separator.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-separator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  [separator-id separator-props]
  [:div.x-button-separator (button-separator.helpers/separator-attributes separator-id separator-props)
                           [:div.x-button-separator--body]])

(defn element
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :color (keyword or string)(opt)
  ;    :highlight, :muted
  ;    Default: :muted
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/button-separator {...}]
  ;
  ; @usage
  ;  [elements/button-separator :my-separator {...}]
  ([separator-props]
   [element (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   (let [separator-props (button-separator.prototypes/separator-props-prototype separator-props)]
        [button-separator separator-id separator-props])))
