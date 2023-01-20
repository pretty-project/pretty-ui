
(ns elements.counter.views
    (:require [elements.counter.helpers    :as counter.helpers]
              [elements.counter.prototypes :as counter.prototypes]
              [elements.element.views      :as element.views]
              [random.api                  :as random]
              [re-frame.api                :as r]
              [reagent.api                 :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter-reset-button
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:resetable? (boolean)(opt)}
  [counter-id {:keys [resetable?] :as counter-props}]
  (if resetable? [:button.e-counter--reset-button (counter.helpers/reset-button-attributes counter-id counter-props)]))

(defn- counter-increase-button
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:disabled? (boolean)(opt)}
  [counter-id counter-props]
  [:button.e-counter--increase-button (counter.helpers/increase-button-attributes counter-id counter-props)])

(defn- counter-decrease-button
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:disabled? (boolean)(opt)}r/
  [counter-id counter-props]
  [:button.e-counter--decrease-button (counter.helpers/decrease-button-attributes counter-id counter-props)])

(defn- counter-value
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ; {:value-path (vector)}
  [_ {:keys [value-path]}]
  (let [value @(r/subscribe [:x.db/get-item value-path])]
       [:div.e-counter--value value]))

(defn- counter-body
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  [:div.e-counter--body (counter.helpers/counter-body-attributes counter-id counter-props)
                        [counter-decrease-button                 counter-id counter-props]
                        [counter-value                           counter-id counter-props]
                        [counter-increase-button                 counter-id counter-props]
                        [counter-reset-button                    counter-id counter-props]])

(defn- counter
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  [counter-id counter-props]
  [:div.e-counter (counter.helpers/counter-attributes counter-id counter-props)
                  [element.views/element-label        counter-id counter-props]
                  [counter-body                       counter-id counter-props]])

(defn- counter-structure
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
