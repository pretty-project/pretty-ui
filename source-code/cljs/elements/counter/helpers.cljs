
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.counter.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.app-environment.api    :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (function)
  [counter-id {:keys [initial-value] :as counter-props}]
  #(if initial-value (r/dispatch [:elements.counter/counter-box-did-mount counter-id counter-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:style style})

(defn counter-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [counter-id {:keys [border-color] :as counter-props}]
  (merge (element.helpers/element-default-attributes counter-id counter-props)
         (element.helpers/element-indent-attributes  counter-id counter-props)
         (element.helpers/apply-color {} :color :data-border-color border-color)
         {:data-selectable false}))

(defn increase-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [counter-id {:keys [disabled? max-value value-path] :as counter-props}]
  (let [value @(r/subscribe [:db/get-item value-path])]
       (if (or disabled? (= max-value value))
           {:disabled       true
            :data-disabled  true}
           {:data-clickable true
            :on-click    #(r/dispatch [:elements.counter/increase-value! counter-id counter-props])
            :on-mouse-up #(x.environment/blur-element!)})))

(defn decrease-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [counter-id {:keys [disabled? min-value value-path] :as counter-props}]
  (let [value @(r/subscribe [:db/get-item value-path])]
       (if (or disabled? (= min-value value))
           {:disabled       true
            :data-disabled  true}
           {:data-clickable true
            :on-click    #(r/dispatch [:elements.counter/decrease-value! counter-id counter-props])
            :on-mouse-up #(x.environment/blur-element!)})))

(defn reset-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [counter-id {:keys [] :as counter-props}])