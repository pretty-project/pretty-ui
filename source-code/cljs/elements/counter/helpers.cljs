
(ns elements.counter.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn increase-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:disabled? (boolean)(opt)
  ;  :max-value (integer)(opt)
  ;  :value-path (vector)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-disabled (boolean)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [counter-id {:keys [disabled? max-value value-path] :as counter-props}]
  (let [value @(r/subscribe [:x.db/get-item value-path])]
       (if (or disabled? (= max-value value))
           {:disabled          true
            :data-disabled     true}
           {:data-click-effect :opacity
            :on-click          #(r/dispatch [:elements.counter/increase-value! counter-id counter-props])
            :on-mouse-up       #(x.environment/blur-element! counter-id)})))

(defn decrease-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:disabled? (boolean)(opt)
  ;  :min-value (integer)(opt)
  ;  :value-path (vector)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-disabled (boolean)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [counter-id {:keys [disabled? min-value value-path] :as counter-props}]
  (let [value @(r/subscribe [:x.db/get-item value-path])]
       (if (or disabled? (= min-value value))
           {:disabled          true
            :data-disabled     true}
           {:data-click-effect :opacity
            :on-click    #(r/dispatch [:elements.counter/decrease-value! counter-id counter-props])
            :on-mouse-up #(x.environment/blur-element! counter-id)})))

(defn reset-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [_ _]
  {})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:border-color (keyword or string)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [border-color style]}]
  (-> {:style style}
      (element.helpers/apply-color :border-color :data-border-color border-color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:data-selectable (boolean)}
  [counter-id counter-props]
  (merge (element.helpers/element-indent-attributes counter-id counter-props)
         (counter-style-attributes                  counter-id counter-props)
         {:data-selectable false}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [counter-id counter-props]
  (merge (element.helpers/element-default-attributes counter-id counter-props)
         (element.helpers/element-outdent-attributes counter-id counter-props)))
