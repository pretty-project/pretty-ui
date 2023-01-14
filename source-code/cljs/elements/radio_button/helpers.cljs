
(ns elements.radio-button.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.components.api  :as x.components]
              [x.environment.api :as x.environment]))

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
  (-> (if-let [any-option-selected? @(r/subscribe [:elements.radio-button/any-option-selected? button-id button-props])]
              {:data-click-effect :opacity
               :on-click      #(r/dispatch [:elements.radio-button/clear-value! button-id button-props])
               :on-mouse-up   #(x.environment/blur-element! button-id)
               :title          (x.components/content :uncheck-selected!)}
              {:data-disabled  true
               :disabled       true})
      (pretty-css/border-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-option-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [button-id {:keys [border-radius] :as button-props}]
  (-> {:style {"--adaptive-border-radius" (pretty-css/adaptive-border-radius border-radius 0.3)}}
      (pretty-css/border-attributes button-props)))

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
       (merge {:data-click-effect :targeted
               :data-selected option-selected?}
              (if disabled? {:disabled    true}
                            {:on-click    #(r/dispatch [:elements.radio-button/select-option! button-id button-props option])
                             :on-mouse-up #(x.environment/blur-element! button-id)}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [options-orientation style] :as button-props}]
  (-> {:data-options-orientation options-orientation
       :data-selectable          false
       :style                    style}
      (pretty-css/indent-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn radio-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [_ button-props]
  (-> {} (pretty-css/default-attributes button-props)
         (pretty-css/outdent-attributes button-props)))
