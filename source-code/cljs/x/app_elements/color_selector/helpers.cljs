
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.helpers
    (:require [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:value-path (vector)}
  ; @param (string) option
  [selector-id {:keys [value-path] :as selector-props} option]
  (let [on-click [:elements/toggle-color-selector-option! selector-id selector-props option]
        selected-options @(a/subscribe [:db/get-item value-path])]
       {:data-clickable true
        :data-collected (vector/contains-item? selected-options option)
        :on-click      #(a/dispatch on-click)
        :on-mouse-up   #(environment/blur-element!)}))

(defn color-selector-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [selector-id {:keys [size] :as selector-props}]
  (merge (engine/element-default-attributes selector-id selector-props)
         (engine/element-indent-attributes  selector-id selector-props)
         {:data-size size}))
