
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.23
; Description:
; Version: v0.3.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.vertical-separator
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-core.api            :as a]
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
  ;  [elements/vertical-separator {...}]
  ;
  ; @usage
  ;  [elements/vertical-separator :my-vertical-separator {...}]
  ;
  ; @return (component)
  ([separator-props]
   [element (a/id) separator-props])

  ([separator-id separator-props]
   (let [separator-props (separator-props-prototype separator-props)]
        [:div.x-vertical-separator (engine/element-attributes separator-id separator-props)])))
