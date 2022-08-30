
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.helpers
    (:require [x.app-core.api               :as a]
              [x.app-elements.engine.api    :as engine]
              [x.app-elements.select.config :as select.config]
              [x.app-environment.api        :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [select-id {:keys [border-radius layout min-width] :as select-props}]
  (merge (engine/element-default-attributes select-id select-props)
         (engine/element-indent-attributes  select-id select-props)
         {:data-border-radius border-radius
          :data-layout        layout
          :data-min-width     min-width}))

(defn select-button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-clickable (boolean)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [select-id {:keys [disabled?] :as select-props}]
  (let [on-click [:elements.select/render-options! select-id select-props]]
       (if disabled? {:disabled       true}
                     {:data-clickable true
                      :on-click       #(a/dispatch on-click)
                      :on-mouse-up    #(environment/blur-element!)})))

(defn select-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ; @param (*) option
  ;
  ; @return (map)
  ;  {}
  [select-id {:keys [value-path] :as select-props} option]
  (let [selected-value  @(a/subscribe [:db/get-item value-path])
        option-selected? (= selected-value option)
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:data-selected option-selected?
        :on-click     #(a/dispatch on-click)
        :on-mouse-up  #(environment/blur-element!)}))
