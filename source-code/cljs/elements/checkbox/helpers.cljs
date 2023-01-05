
(ns elements.checkbox.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (boolean)(opt)}
  [checkbox-id {:keys [initial-options initial-value] :as checkbox-props}]
  (if (or initial-options initial-value)
      (r/dispatch [:elements.checkbox/checkbox-did-mount checkbox-id checkbox-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [checkbox-id {:keys [style] :as checkbox-props}]
  (merge (element.helpers/element-indent-attributes checkbox-id checkbox-props)
         {:style style}))

(defn checkbox-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  [checkbox-id checkbox-props]
  (merge (element.helpers/element-default-attributes checkbox-id checkbox-props)
         (element.helpers/element-outdent-attributes checkbox-id checkbox-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-option-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [_ {:keys [border-color]} _]
  (-> {:data-icon-family :material-icons-filled}
      (element.helpers/apply-color :border-color :data-border-color border-color)))

(defn checkbox-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [checkbox-id {:keys [disabled? value-path] :as checkbox-props} option]
  (let [option-checked? @(r/subscribe [:elements.checkbox/option-checked? checkbox-id checkbox-props option])]
       (if disabled? {:data-checked option-checked?
                      :disabled     true}
                     {:data-checked option-checked?
                      :on-click     #(r/dispatch [:elements.checkbox/toggle-option! checkbox-id checkbox-props option])
                      :on-mouse-up  #(element.side-effects/blur-element! checkbox-id)})))
