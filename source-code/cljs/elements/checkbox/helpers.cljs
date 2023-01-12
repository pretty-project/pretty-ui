
(ns elements.checkbox.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-option-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:border-color (keyword or string)
  ;  :border-radius (keyword)
  ;  :border-width (keyword)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-border-width (keyword)
  ;  :data-icon-family (keyword)}
  [_ {:keys [border-color border-radius border-width]} _]
  (-> {:data-border-radius border-radius
       :data-border-width  border-width
       :data-icon-family   :material-icons-filled}
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
  ; {:data-checked (boolean)
  ;  :data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [checkbox-id {:keys [disabled? value-path] :as checkbox-props} option]
  (let [option-checked? @(r/subscribe [:elements.checkbox/option-checked? checkbox-id checkbox-props option])]
       (merge {:data-checked option-checked?
               :data-click-effect :targeted}
              (if disabled? {:disabled     true}
                            {:on-click     #(r/dispatch [:elements.checkbox/toggle-option! checkbox-id checkbox-props option])
                             :on-mouse-up  #(x.environment/blur-element! checkbox-id)}))))

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
