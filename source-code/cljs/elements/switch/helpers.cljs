
(ns elements.switch.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-option-track-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:border-color (keyword)
  ;  :border-radius (keyword)
  ;  :border-width (keyword)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-border-width (keyword)}
  [_ {:keys [border-color border-radius border-width]}]
  (-> {:data-border-radius border-radius
       :data-border-width  border-width}
      (element.helpers/apply-color :border-color :data-border-color border-color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-switched (boolean)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [switch-id {:keys [disabled?] :as switch-props} option]
  (let [option-switched? @(r/subscribe [:elements.switch/option-switched? switch-id switch-props option])]
       (merge {:data-click-effect :targeted
               :data-switched option-switched?}
              (if disabled? {:disabled    true}
                            {:on-click    #(r/dispatch [:elements.switch/toggle-option! switch-id switch-props option])
                             :on-mouse-up #(x.environment/blur-element! switch-id)}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style]}]
  {:style style})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:options-orientation (keyword)}
  ;
  ; @return (map)
  ; {data-options-orientation (keyword)
  ;  :data-selectable (boolean)}
  [switch-id {:keys [options-orientation] :as switch-props}]
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
