
(ns elements.counter.helpers
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

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
       (-> (if (or disabled? (= max-value value))
               {:disabled          true
                :data-disabled     true}
               {:data-click-effect :opacity
                :on-click          #(r/dispatch [:elements.counter/increase-value! counter-id counter-props])
                :on-mouse-up       #(x.environment/blur-element! counter-id)})
           (pretty-css/border-attributes counter-props))))

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
       (-> (if (or disabled? (= min-value value))
               {:disabled          true
                :data-disabled     true}
               {:data-click-effect :opacity
                :on-click          #(r/dispatch [:elements.counter/decrease-value! counter-id counter-props])
                :on-mouse-up       #(x.environment/blur-element! counter-id)})
           (pretty-css/border-attributes counter-props))))

(defn reset-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:data-border-color (keyword)}
  [_ counter-props]
  (-> {} (pretty-css/border-attributes counter-props)
         (merge {:data-border-color :default})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-selectable (boolean)
  ;  :style (map)}
  [_ {:keys [style] :as counter-props}]
  (-> {:data-selectable false
       :style           style}
      (pretty-css/indent-attributes counter-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [_ counter-props]
  (-> {} (pretty-css/default-attributes counter-props)
         (pretty-css/outdent-attributes counter-props)))
