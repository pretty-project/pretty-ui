
(ns pretty-inputs.counter.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.api              :as pretty-elements]
              [pretty-inputs.engine.api                :as pretty-inputs.engine]
              [pretty-forms.api                 :as pretty-forms]
              [pretty-inputs.counter.attributes :as counter.attributes]
              [pretty-inputs.counter.prototypes :as counter.prototypes]
              [pretty-presets.api               :as pretty-presets]
              [re-frame.api                     :as r]
              [reagent.api                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {}
  [counter-id {:keys [resetable? value-path] :as counter-props}]
  [:div (counter.attributes/counter-attributes counter-id counter-props)
        (if-let [label-props (pretty-inputs.engine/input-label-props counter-id counter-props)]
                [pretty-elements/label label-props])
        [pretty-forms/invalid-message            counter-id counter-props]
        [pretty-inputs.engine/input-synchronizer counter-id counter-props]
        [:div (counter.attributes/counter-body-attributes counter-id counter-props)
              [:button (counter.attributes/decrease-button-attributes counter-id counter-props)]
              (let [value @(r/subscribe [:get-item value-path])]
                   [:div {:class :pi-counter--value} value])
              [:button (counter.attributes/increase-button-attributes counter-id counter-props)]
              (if resetable? [:button (counter.attributes/reset-button-attributes counter-id counter-props)])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-lifecycles
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-inputs.counter/counter-did-mount counter-id counter-props]))
                       :reagent-render      (fn [_ counter-props] [counter counter-id counter-props])}))

(defn input
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
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (integer)(opt)
  ;   Default: 0
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :max-value (integer)(opt)
  ;  :min-value (integer)(opt)
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
   [input (random/generate-keyword) counter-props])

  ([counter-id counter-props]
   ; @note (tutorials#parametering)
   (fn [_ counter-props]
       (let [counter-props (pretty-presets/apply-preset                           counter-props)
             counter-props (counter.prototypes/counter-props-prototype counter-id counter-props)]
            [input-lifecycles counter-id counter-props]))))
