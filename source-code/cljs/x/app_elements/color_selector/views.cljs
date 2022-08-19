
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.views
    (:require [layouts.popup-a.api                      :as popup-a]
              [mid-fruits.candy                         :refer [param]]
              [mid-fruits.vector                        :as vector]
              [x.app-components.api                     :as components]
              [x.app-core.api                           :as a]
              [x.app-elements.color-selector.helpers    :as color-selector.helpers]
              [x.app-elements.color-selector.prototypes :as color-selector.prototypes]
              [x.app-elements.color-stamp.views         :refer [color-stamp]]
              [x.app-environment.api                    :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:value-path (vector)}
  ; @param (string) option
  [selector-id {:keys [value-path] :as selector-props} option]
  (let [on-click [:elements/toggle-color-selector-option! selector-id selector-props option]
        selected-options @(a/subscribe [:db/get-item value-path])]
       [:button.x-color-selector--option {:data-clickable true
                                          :data-collected (vector/contains-item? selected-options option)
                                          :on-click      #(a/dispatch on-click)
                                          :on-mouse-up   #(environment/blur-element!)}
                                         [:div.x-color-selector--option--color {:style {:background-color option}}]]))

(defn color-selector-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id {:keys [options] :as selector-props}]
  (reduce (fn [option-list option]
              (conj option-list [color-selector-option selector-id selector-props option]))
          [:div.x-color-selector--options]
          (param options)))

(defn color-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [popup-a/layout :elements.color-selector/options
                  {:body [color-selector-options selector-id selector-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ;  {}
  ;
  ; @usage
  ;  [elements/color-selector {...}]
  ;
  ; @usage
  ;  [elements/color-selector :my-color-selector {...}]
  ([selector-props]
   [element (a/id) selector-props])

  ([selector-id selector-props]))
