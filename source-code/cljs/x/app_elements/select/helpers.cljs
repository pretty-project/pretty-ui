
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.helpers
    (:require [x.app-core.api               :as a]
              [x.app-elements.select.config :as select.config]
              [x.app-environment.api        :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  [select-id {:keys [disabled?] :as select-props}]
  (let [on-click [:elements.select/render-options! select-id select-props]]
       (if-not disabled? {:data-clickable true
                          :on-click       #(a/dispatch on-click)
                          :on-mouse-up    #(environment/blur-element!)})))

(defn select-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ; @param (*) option
  [select-id {:keys [value-path] :as select-props} option]
  (let [selected-value  @(a/subscribe [:db/get-item value-path])
        option-selected? (= selected-value option)
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:data-selected option-selected?
        :on-click     #(a/dispatch on-click)
        :on-mouse-up  #(environment/blur-element!)}))
