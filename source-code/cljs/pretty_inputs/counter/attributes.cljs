
(ns pretty-inputs.counter.attributes
    (:require [dom.api               :as dom]
              [pretty-attributes.api :as pretty-attributes]
              [re-frame.api          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn increase-button-attributes
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:disabled? (boolean)(opt)
  ;  :max-value (integer)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-disabled (boolean)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [counter-id {:keys [disabled? max-value value-path] :as counter-props}]
  (let [value @(r/subscribe [:get-item value-path])]
       (-> (if (or disabled? (= max-value value))
               {:class             :pi-counter--increase-button
                :disabled          true
                :data-disabled     true}
               {:class             :pi-counter--increase-button
                :data-click-effect :opacity
                :on-click          #(r/dispatch [:pretty-inputs.counter/increase-value! counter-id counter-props])
                :on-mouse-up       #(dom/blur-active-element!)})
           (pretty-attributes/border-attributes counter-props))))

(defn decrease-button-attributes
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:disabled? (boolean)(opt)
  ;  :min-value (integer)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-disabled (boolean)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [counter-id {:keys [disabled? min-value value-path] :as counter-props}]
  (let [value @(r/subscribe [:get-item value-path])]
       (-> (if (or disabled? (= min-value value))
               {:class             :pi-counter--decrease-button
                :disabled          true
                :data-disabled     true}
               {:class             :pi-counter--decrease-button
                :data-click-effect :opacity
                :on-click          #(r/dispatch [:pretty-inputs.counter/decrease-value! counter-id counter-props])
                :on-mouse-up       #(dom/blur-active-element!)})
           (pretty-attributes/border-attributes counter-props))))

(defn reset-button-attributes
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-border-color (keyword)}
  [_ counter-props]
  ; The reset button border color is independent from the increase and decrease
  ; buttons border color.
  (-> {:class :pi-counter--reset-button}
      (pretty-attributes/border-attributes counter-props)
      (merge {:data-border-color :default})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ counter-props]
  (-> {:class :pi-counter--inner}
      (pretty-attributes/inner-space-attributes counter-props)
      (pretty-attributes/style-attributes       counter-props)

      ; nem elég ha a value-n van csak alkalmazva a text-attributes?
      (pretty-attributes/text-attributes counter-props)))
      ; + :text-selectable? false

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [_ counter-props]
  (-> {:class :pi-counter--outer}
      (pretty-attributes/class-attributes  counter-props)
      (pretty-attributes/outer-space-attributes counter-props)
      (pretty-attributes/state-attributes  counter-props)
      (pretty-attributes/theme-attributes   counter-props)))
