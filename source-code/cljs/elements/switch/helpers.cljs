
(ns elements.switch.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (boolean)(opt)}
  [switch-id {:keys [initial-options initial-value] :as switch-props}]
  (if (or initial-options initial-value)
      (r/dispatch [:elements.switch/switch-did-mount switch-id switch-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [border-color style]}]
  (-> {:style style}
      (element.helpers/apply-color :border-color :data-border-color border-color)))

(defn switch-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [switch-id {:keys [border-color options-orientation] :as switch-props}]
  (merge (element.helpers/element-indent-attributes switch-id switch-props)
         (switch-style-attributes                   switch-id switch-props)
         {:data-options-orientation options-orientation
          :data-selectable          false}))

(defn switch-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  [switch-id switch-props]
  (merge (element.helpers/element-default-attributes switch-id switch-props)
         (element.helpers/element-outdent-attributes switch-id switch-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [switch-id {:keys [disabled?] :as switch-props} option]
  (let [option-switched? @(r/subscribe [:elements.switch/option-switched? switch-id switch-props option])]
       (merge {:data-clickable :targeted
               :data-switched  option-switched?}
              (if disabled? {:disabled     true}
                            {:on-click     #(r/dispatch [:elements.switch/toggle-option! switch-id switch-props option])
                             :on-mouse-up  #(x.environment/blur-element! switch-id)}))))
