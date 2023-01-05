
(ns elements.radio-button.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]
              [x.components.api              :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  [button-id {:keys [initial-options initial-value] :as button-props}]
  (if (or initial-options initial-value)
      (r/dispatch [:elements.radio-button/radio-button-box-did-mount button-id button-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [button-id {:keys [border-color style] :as button-props}]
  (-> {:style style}
      (element.helpers/apply-color :border-color :data-border-color border-color)))

(defn radio-button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [button-id {:keys [border-color options-orientation] :as button-props}]
  (merge (element.helpers/element-indent-attributes button-id button-props)
         (radio-button-style-attributes             button-id button-props)
         {:data-options-orientation options-orientation
          :data-selectable          false}))

(defn radio-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [button-id button-props]
  (merge (element.helpers/element-default-attributes button-id button-props)
         (element.helpers/element-outdent-attributes button-id button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-id button-props]
  (if-let [any-option-selected? @(r/subscribe [:elements.radio-button/any-option-selected? button-id button-props])]
          {:data-clickable true
           :on-click      #(r/dispatch [:elements.radio-button/clear-value! button-id button-props])
           :on-mouse-up   #(element.side-effects/blur-element! button-id)
           :title          (x.components/content :uncheck-selected!)}
          {:data-disabled  true
           :disabled       true}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [button-id {:keys [disabled?] :as button-props} option]
  (let [option-selected? @(r/subscribe [:elements.radio-button/option-selected? button-id button-props option])]
       (if disabled? {:data-selected option-selected?
                      :disabled      true}
                     {:data-selected option-selected?
                      :on-click     #(r/dispatch [:elements.radio-button/select-option! button-id button-props option])
                      :on-mouse-up  #(element.side-effects/blur-element! button-id)})))
