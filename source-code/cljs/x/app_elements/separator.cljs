
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.23
; Description:
; Version: v0.2.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.separator
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
  ;  {:orientation (keyword)
  ;   :size (keyword)}
  [separator-props]
  (merge {:orientation :horizontal
          :size        :s}
         (param separator-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ;  {:orientation (keyword)(opt)
  ;    :horizontal, :vertical
  ;    Default: :horizontal
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s}
  ;
  ; @usage
  ;  [elements/separator {...}]
  ;
  ; @usage
  ;  [elements/separator :my-separator {...}]
  ;
  ; @return (component)
  ([separator-props]
   [view (a/id) separator-props])

  ([separator-id separator-props]
   (let [separator-props (a/prot separator-props separator-props-prototype)]
        [:div.x-separator (engine/element-attributes separator-id separator-props)])))
