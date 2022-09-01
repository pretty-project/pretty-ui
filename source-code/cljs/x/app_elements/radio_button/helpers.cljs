
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button.helpers
    (:require [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [button-id {:keys [border-color font-size options-orientation] :as button-props}]
  (merge (engine/element-default-attributes button-id button-props)
         (engine/element-indent-attributes  button-id button-props)
         {:data-border-color        border-color
          :data-font-size           font-size
          :data-options-orientation options-orientation
          :data-selectable          false}))

(defn clear-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {}
  [button-id button-props]
  (if-let [any-option-selected? @(a/subscribe [:elements.radio-button/any-option-selected? button-id button-props])]
          {:data-clickable true
           :on-click      #(a/dispatch [:elements.radio-button/clear-value! button-id button-props])
           :on-mouse-up   #(environment/blur-element!)
           :title          (components/content :uncheck-selected!)}
          {:data-disabled  true
           :disabled       true}))

(defn radio-button-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  ;
  ; @return (map)
  ;  {}
  [button-id {:keys [disabled? value-path] :as button-props} option]
  (let [selected-option @(a/subscribe [:db/get-item value-path])
        option-selected? (= selected-option option)]
       (if disabled? {:data-selected option-selected?
                      :disabled      true}
                     {:data-selected option-selected?
                      :on-click     #(a/dispatch [:elements.radio-button/select-option! button-id button-props option])
                      :on-mouse-up  #(environment/blur-element!)})))
