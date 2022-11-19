
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.horizontal-separator.views
    (:require [elements.horizontal-separator.helpers    :as horizontal-separator.helpers]
              [elements.horizontal-separator.prototypes :as horizontal-separator.prototypes]
              [random.api                               :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ;  {:size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s}
  ;
  ; @usage
  ;  [horizontal-separator {...}]
  ;
  ; @usage
  ;  [horizontal-separator :my-horizontal-separator {...}]
  ([separator-props]
   [element (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   (let [separator-props (horizontal-separator.prototypes/separator-props-prototype separator-props)]
        [:div.e-horizontal-separator (horizontal-separator.helpers/separator-attributes separator-id separator-props)])))
