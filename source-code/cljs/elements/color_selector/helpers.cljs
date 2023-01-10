
(ns elements.color-selector.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [vector.api               :as vector]
              [x.environment.api        :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:value-path (vector)}
  ; @param (string) option
  ;
  ; @return (map)
  ; {}
  [selector-id {:keys [value-path] :as selector-props} option]
  (let [on-click [:elements/toggle-color-selector-option! selector-id selector-props option]
        selected-options @(r/subscribe [:x.db/get-item value-path])]
       {:data-click-effect :opacity
        :data-icon-family  :material-icons-filled
        :data-collected    (vector/contains-item? selected-options option)
        :on-click          #(r/dispatch on-click)
        :on-mouse-up       #(x.environment/blur-element! selector-id)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [selector-id {:keys [style] :as selector-props}]
  (merge (element.helpers/element-indent-attributes selector-id selector-props)
         {:style style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:size (keyword)}
  ;
  ; @return (map)
  ; {:data-size (keyword)}
  [selector-id {:keys [size] :as selector-props}]
  (merge (element.helpers/element-default-attributes selector-id selector-props)
         (element.helpers/element-outdent-attributes selector-id selector-props)
         {:data-size size}))
