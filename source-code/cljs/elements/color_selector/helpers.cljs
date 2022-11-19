
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.color-selector.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]
              [vector.api                    :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:value-path (vector)}
  ; @param (string) option
  ;
  ; @return (map)
  ;  {}
  [selector-id {:keys [value-path] :as selector-props} option]
  (let [on-click [:elements/toggle-color-selector-option! selector-id selector-props option]
        selected-options @(r/subscribe [:x.db/get-item value-path])]
       {:data-clickable   true
        :data-collected   (vector/contains-item? selected-options option)
        :data-icon-family :material-icons-filled
        :on-click        #(r/dispatch on-click)
        :on-mouse-up     #(element.side-effects/blur-element! selector-id)}))

(defn color-selector-options-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ;  {}
  [_ _]
  {:class :e-element})

(defn color-selector-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:style style})

(defn color-selector-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:size (keyword)}
  ;
  ; @return (map)
  ;  {:data-size (keyword)}
  [selector-id {:keys [size] :as selector-props}]
  (merge (element.helpers/element-default-attributes selector-id selector-props)
         (element.helpers/element-indent-attributes  selector-id selector-props)
         {:data-size size}))
