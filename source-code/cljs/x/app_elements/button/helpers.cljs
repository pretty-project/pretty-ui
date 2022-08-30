
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button.helpers
    (:require [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon-family (keyword)
  ;   :icon-position (keyword)}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [icon-family icon-position]}]
  {:data-icon-family   icon-family
   :data-icon-position icon-position})

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [button-id {:keys [disabled? on-click]}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :id              (a/dom-value button-id "body")
                 :on-click       #(a/dispatch on-click)
                 :on-mouse-up    #(environment/blur-element!)}))

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:background-color (keyword or string)(opt)
  ;   :border-color (keyword or string)(opt)
  ;   :border-radius (keyword)(opt)
  ;   :color (keyword or string)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :horizontal-align (keyword)
  ;   :hover-color (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-font-size (keyword)
  ;   :data-font-weight (keyword)
  ;   :data-horizontal-align (keyword)
  ;   :data-selectable (boolean)}
  [button-id {:keys [background-color border-color border-radius color font-size
                     font-weight horizontal-align hover-color] :as button-props}]
  (merge (engine/element-default-attributes button-id button-props)
         (engine/element-indent-attributes  button-id button-props)
         (engine/apply-color {} :background-color :data-background-color background-color)
         (engine/apply-color {} :border-color     :data-border-color         border-color)
         (engine/apply-color {} :color            :data-color                       color)
         (engine/apply-color {} :hover-color      :data-hover-color           hover-color)
         (if border-radius {:data-border-radius border-radius})
         {:data-font-size        font-size
          :data-font-weight      font-weight
          :data-horizontal-align horizontal-align
          :data-selectable       false}))
