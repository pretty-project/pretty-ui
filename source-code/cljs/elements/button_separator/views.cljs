
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.button-separator.views
    (:require [elements.button-separator.helpers    :as button-separator.helpers]
              [elements.button-separator.prototypes :as button-separator.prototypes]
              [random.api                           :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-separator-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  [separator-id separator-props]
  [:div.e-button-separator--body (button-separator.helpers/separator-body-attributes separator-id separator-props)])

(defn button-separator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  [separator-id separator-props]
  [:div.e-button-separator (button-separator.helpers/separator-attributes separator-id separator-props)
                           [button-separator-body                         separator-id separator-props]])

(defn element
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :highlight, :muted
  ;   Default: :muted
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [button-separator {...}]
  ;
  ; @usage
  ; [button-separator :my-separator {...}]
  ([separator-props]
   [element (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   (let [separator-props (button-separator.prototypes/separator-props-prototype separator-props)]
        [button-separator separator-id separator-props])))
