
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.2.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.icon
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ;  {:icon-family
  ;   :layout (keyword)
  ;   :size (keyword)}
  [icon-props]
  (merge {:icon-family :material-icons-filled
          :layout      :fit
          :size        :m}
         (param icon-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;  {:icon (keyword)}
  ;
  ; @return (hiccup)
  [icon-id {:keys [icon] :as icon-props}]
  [:i.x-icon (engine/element-attributes icon-id icon-props)
             (param icon)])

(defn element
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :layout (keyword)(opt)
  ;    :fit Az ikont tartalmazó elem méretei megegyeznek az ikon méreteivel
  ;    :row ...
  ;    :touch-target Az ikont tartalmazó ... az icon-button típus méreteivel
  ;    Default: :fit
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :m
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/icon {...}]
  ;
  ; @usage
  ;  [elements/icon :my-icon {...}]
  ;
  ; @return (hiccup)
  ([icon-props]
   [element (a/id) icon-props])

  ([icon-id icon-props]
   (let [icon-props (a/prot icon-props icon-props-prototype)]
        [icon icon-id icon-props])))
