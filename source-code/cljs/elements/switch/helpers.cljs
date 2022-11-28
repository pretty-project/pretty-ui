
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.switch.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:initial-options (vector)(opt)
  ;   :initial-value (boolean)(opt)}
  [switch-id {:keys [initial-options initial-value] :as switch-props}]
  (if (or initial-options initial-value)
      (r/dispatch [:elements.switch/switch-did-mount switch-id switch-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:style style})

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
  (merge (element.helpers/element-default-attributes switch-id switch-props)
         (element.helpers/element-indent-attributes  switch-id switch-props)
         (element.helpers/apply-color {} :color :data-border-color border-color)
         {:data-font-size           font-size
          :data-options-orientation options-orientation
          :data-selectable          false}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [option-switched? @(r/subscribe [:elements.switch/option-switched? switch-id switch-props option])]
       (if disabled? {:data-switched option-switched?
                      :disabled      true}
                     {:data-switched option-switched?
                      :on-click     #(r/dispatch [:elements.switch/toggle-option! switch-id switch-props option])
                      :on-mouse-up  #(element.side-effects/blur-element! switch-id)})))
