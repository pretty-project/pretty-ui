
(ns pretty-inputs.counter.attributes
    (:require [dom.api              :as dom]
              [pretty-build-kit.api :as pretty-build-kit]
              [re-frame.api         :as r]))

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
           (pretty-build-kit/border-attributes counter-props))))

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
           (pretty-build-kit/border-attributes counter-props))))

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
      (pretty-build-kit/border-attributes counter-props)
      (merge {:data-border-color :default})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-body-attributes
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-selectable (boolean)
  ;  :style (map)}
  [_ {:keys [style] :as counter-props}]
  (-> {:class           :pi-counter--body
       :data-selectable false
       :style           style}
      (pretty-build-kit/indent-attributes counter-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-attributes
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  [_ counter-props]
  (-> {:class :pi-counter}
      (pretty-build-kit/class-attributes   counter-props)
      (pretty-build-kit/outdent-attributes counter-props)
      (pretty-build-kit/state-attributes   counter-props)))
