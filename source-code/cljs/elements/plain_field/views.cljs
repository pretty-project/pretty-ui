
(ns elements.plain-field.views
    (:require [elements.plain-field.helpers    :as plain-field.helpers]
              [elements.plain-field.prototypes :as plain-field.prototypes]
              [random.api                      :as random]
              [re-frame.api                    :as r]
              [reagent.api                     :as reagent]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-field-synchronizer-debug
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-content-f (function)
  ;  :value-path (vector)}
  [field-id {:keys [field-content-f value-path]}]
  ; HACK#9910
  (let [stored-value @(r/subscribe [:x.db/get-item value-path])]
       [:div [:br] "field content:   " (plain-field.helpers/get-field-content field-id)
             [:br] "field output:    " (plain-field.helpers/get-field-output  field-id)
             [:br] "stored value:    " (str             stored-value)
             [:br] "derived content: " (field-content-f stored-value)]))

(defn- plain-field-synchronizer-sensor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (*) stored-value
  [field-id field-props stored-value]
  ; HACK#9910
  (reagent/lifecycles {:component-will-unmount (fn [_ _ _] (plain-field.helpers/synchronizer-will-unmount-f field-id field-props))
                       :component-did-mount    (fn [_ _ _] (plain-field.helpers/synchronizer-did-mount-f    field-id field-props))
                       :component-did-update   (fn [%]     (plain-field.helpers/synchronizer-did-update-f   field-id %))
                      ;:reagent-render         (fn [_ _ _] [plain-field-synchronizer-debug                  field-id field-props])
                       :reagent-render         (fn [_ _ _])}))

(defn- plain-field-synchronizer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:value-path (vector)}
  [field-id {:keys [value-path] :as field-props}]
  ; HACK#9910
  (let [stored-value @(r/subscribe [:x.db/get-item value-path])]
       [plain-field-synchronizer-sensor field-id field-props stored-value]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-field-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:surface (metamorphic-content)(opt)}
  [field-id {:keys [surface] :as field-props}]
  (if surface (if (plain-field.helpers/surface-visible? field-id)
                  [:div.e-plain-field--surface (plain-field.helpers/field-surface-attributes field-id field-props)
                                               [x.components/content                         field-id surface]])))

(defn- plain-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input.e-plain-field--input (plain-field.helpers/field-input-attributes field-id field-props)])

(defn- plain-field-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-plain-field--body (plain-field.helpers/field-body-attributes field-id field-props)
                            [plain-field-input                         field-id field-props]])

(defn- plain-field-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-plain-field (plain-field.helpers/field-attributes field-id field-props)
                      [plain-field-body                     field-id field-props]
                      [plain-field-surface                  field-id field-props]
                      [plain-field-synchronizer             field-id field-props]])

(defn- plain-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.plain-field/field-did-mount    field-id field-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.plain-field/field-will-unmount field-id field-props]))
                       :reagent-render         (fn [_ field-props] [plain-field-structure field-id field-props])}))

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;  :autofocus? (boolean)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :field-content-f (function)(opt)
  ;   Default: return
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :initial-value (string)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :modifier (function)(opt)
  ;  :on-blur (metamorphic-event)(opt)
  ;  :on-changed (metamorphic-event)(opt)
  ;   It happens BEFORE the application state gets updated with the actual value!
  ;   If you have to get the ACTUAL value from the application state, use the
  ;   :on-type-ended event instead!
  ;   This event takes the field content as its last parameter
  ;  :on-focus (metamorphic-event)(opt)
  ;  :on-mount (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-type-ended (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-unmount (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :surface (metamorphic-content)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [plain-field {...}]
  ;
  ; @usage
  ; [plain-field :my-plain-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (plain-field.prototypes/field-props-prototype field-id field-props)]
        [plain-field field-id field-props])))
