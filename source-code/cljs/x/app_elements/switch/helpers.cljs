
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.switch.helpers
    (:require [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {}
  ;
  ; @return (function)
  [switch-id {:keys [initial-options initial-value] :as switch-props}]
  #(if (or initial-options initial-value)
       (a/dispatch [:elements.switch/init-switch! switch-id switch-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [switch-id {:keys [border-color font-size options-orientation] :as switch-props}]
  (merge (engine/element-default-attributes switch-id switch-props)
         (engine/element-indent-attributes  switch-id switch-props)
         {:data-border-color        border-color
          :data-font-size           font-size
          :data-options-orientation options-orientation
          :data-selectable          false}))

(defn switch-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {}
  ; @param (*) option
  ;
  ; @return (map)
  ;  {}
  [switch-id {:keys [disabled?] :as switch-props} option]
  (let [option-switched? @(a/subscribe [:elements.switch/option-switched? switch-id switch-props option])]
       (if disabled? {:data-switched option-switched?
                      :disabled      true}
                     {:data-switched option-switched?
                      :on-click     #(a/dispatch [:elements.switch/toggle-option! switch-id switch-props option])
                      :on-mouse-up  #(environment/blur-element!)})))
