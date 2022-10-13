
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.horizontal-separator
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.random         :as random]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- separator-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) separator-props
  ;
  ; @return (map)
  ;  {:size (keyword)}
  [separator-props]
  (merge {:size :s}
         (param separator-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ;  {:size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s}
  ;
  ; @usage
  ;  [elements/horizontal-separator {...}]
  ;
  ; @usage
  ;  [elements/horizontal-separator :my-horizontal-separator {...}]
  ([separator-props]
   [element (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   (let [separator-props (separator-props-prototype separator-props)]
        [:div.x-horizontal-separator (engine/element-attributes separator-id separator-props)])))
