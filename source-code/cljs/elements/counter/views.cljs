
(ns elements.counter.views
    (:require [elements.counter.attributes :as counter.attributes]
              [elements.counter.prototypes :as counter.prototypes]
              [elements.element.views      :as element.views]
              [random.api                  :as random]
              [re-frame.api                :as r]
              [reagent.api                 :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter-structure
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {}
  [counter-id {:keys [resetable? value-path] :as counter-props}]
  [:div (counter.attributes/counter-attributes counter-id counter-props)
        [element.views/element-label           counter-id counter-props]
        [:div (counter.attributes/counter-body-attributes counter-id counter-props)
              [:button (counter.attributes/decrease-button-attributes counter-id counter-props)]
              (let [value @(r/subscribe [:x.db/get-item value-path])]
                   [:div {:class :e-counter--value} value])
              [:button (counter.attributes/increase-button-attributes counter-id counter-props)]
              (if resetable? [:button (counter.attributes/reset-button-attributes counter-id counter-props)])]])

(defn- counter
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.counter/counter-did-mount counter-id counter-props]))
                       :reagent-render      (fn [_ counter-props] [counter-structure counter-id counter-props])}))


(defn element
  ; @param (keyword)(opt) counter-id
  ; @param (map) counter-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;   Default: {:all :m}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (integer)(opt)
  ;   Default: 0
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :max-value (integer)(opt)
  ;  :min-value (integer)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :resetable? (boolean)(opt)
  ;   Default: false
  ;  :style (map)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [counter {...}]
  ;
  ; @usage
  ; [counter :my-counter {...}]
  ([counter-props]
   [element (random/generate-keyword) counter-props])

  ([counter-id counter-props]
   (let [counter-props (counter.prototypes/counter-props-prototype counter-id counter-props)]
        [counter counter-id counter-props])))
