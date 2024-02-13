
(ns pretty-inputs.counter.views
    (:require [fruits.random.api                :as random]
              [pretty-inputs.counter.attributes :as counter.attributes]
              [pretty-inputs.counter.prototypes :as counter.prototypes]
              [pretty-inputs.engine.api         :as pretty-inputs.engine]
              [pretty-inputs.header.views       :as header.views]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [re-frame.api                     :as r]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {}
  [counter-id {:keys [resetable? value-path] :as counter-props}]
  [:div (counter.attributes/counter-attributes   counter-id counter-props)
        [pretty-inputs.header.views/view         counter-id counter-props]
        [pretty-inputs.engine/input-synchronizer counter-id counter-props]
        [:div (counter.attributes/counter-body-attributes counter-id counter-props)
              [:button (counter.attributes/decrease-button-attributes counter-id counter-props)]
              (let [value @(r/subscribe [:get-item value-path])]
                   [:div {:class :pi-counter--value} value])
              [:button (counter.attributes/increase-button-attributes counter-id counter-props)]
              (if resetable? [:button (counter.attributes/reset-button-attributes counter-id counter-props)])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount (fn [_ _] (r/dispatch [:pretty-inputs.counter/counter-did-mount counter-id counter-props]))
                         :reagent-render      (fn [_ counter-props] [counter counter-id counter-props])}))

(defn view
  ; @param (keyword)(opt) counter-id
  ; @param (map) counter-props
  ; {:border-color (keyword or string)(opt)
  ;   Default: :default
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;   Default: {:all :m}
  ;  :border-width (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info (metamorphic-content)(opt)
  ;  :initial-value (integer)(opt)
  ;   Default: 0
  ;  :label (metamorphic-content)(opt)
  ;  :marker (map)(opt)
  ;  :max-value (integer)(opt)
  ;  :min-value (integer)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :resetable? (boolean)(opt)
  ;   Default: false
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [counter {...}]
  ;
  ; @usage
  ; [counter :my-counter {...}]
  ([counter-props]
   [view (random/generate-keyword) counter-props])

  ([counter-id counter-props]
   ; @note (tutorials#parameterizing)
   (fn [_ counter-props]
       (let [counter-props (pretty-presets.engine/apply-preset         counter-id counter-props)
             counter-props (counter.prototypes/counter-props-prototype counter-id counter-props)]
            [view-lifecycles counter-id counter-props]))))
