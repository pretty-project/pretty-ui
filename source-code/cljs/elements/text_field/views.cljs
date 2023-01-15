
(ns elements.text-field.views
    (:require [elements.element.views         :as element.views]
              [elements.plain-field.helpers   :as plain-field.helpers]
              [elements.plain-field.views     :as plain-field.views]
              [elements.text-field.helpers    :as text-field.helpers]
              [elements.text-field.prototypes :as text-field.prototypes]
              [random.api                     :as random]
              [re-frame.api                   :as r]
              [reagent.api                    :as reagent]
              [string.api                     :as string]
              [vector.api                     :as vector]
              [x.components.api               :as x.components]))

;; -- Field adornments components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-adornments-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-text-field--adornments-placeholder (plain-field.helpers/field-accessory-attributes field-id field-props)])

(defn button-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (string)(opt)}
  [field-id field-props {:keys [icon label] :as adornment-props}]
  (let [adornment-attributes (text-field.helpers/button-adornment-attributes field-id field-props adornment-props)]
       (cond icon  [:button.e-text-field--button-adornment adornment-attributes icon]
             label [:button.e-text-field--button-adornment adornment-attributes label])))

(defn static-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (string)(opt)}
  [field-id field-props {:keys [icon label] :as adornment-props}]
  (let [adornment-attributes (text-field.helpers/static-adornment-attributes field-id field-props adornment-props)]
       (cond icon  [:div.e-text-field--static-adornment adornment-attributes icon]
             label [:div.e-text-field--static-adornment adornment-attributes (x.components/content label)])))

(defn field-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:on-click (metamorphic-event)(opt)}
  [field-id field-props {:keys [on-click] :as adornment-props}]
  (let [adornment-props (text-field.prototypes/adornment-props-prototype adornment-props)]
       (if on-click [button-adornment field-id field-props adornment-props]
                    [static-adornment field-id field-props adornment-props])))

(defn field-end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [end-adornments (text-field.prototypes/end-adornments-prototype field-id field-props)]
       (if (vector/nonempty? end-adornments)
           (letfn [(f [% adornment-props] (conj % [field-adornment field-id field-props adornment-props]))]
                  (reduce f [:div.e-text-field--adornments] end-adornments))
           [field-adornments-placeholder field-id field-props])))

(defn field-start-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (if (vector/nonempty? start-adornments)
      (letfn [(f [% adornment-props] (conj % [field-adornment field-id field-props adornment-props]))]
             (reduce f [:div.e-text-field--adornments] start-adornments))
      [field-adornments-placeholder field-id field-props]))

;; -- Field surface components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:surface (metamorphic-content)(opt)}
  [field-id {:keys [surface] :as field-props}]
  (if surface (if (plain-field.helpers/surface-visible? field-id)
                  [:div.e-text-field--surface (plain-field.helpers/field-surface-attributes field-id field-props)
                                              [x.components/content                         field-id surface]])))

;; -- Field structure components ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:placeholder (metamorphic-content)}
  [field-id {:keys [placeholder] :as field-props}]
  (if placeholder (if-let [field-empty? (plain-field.helpers/field-empty? field-id)]
                          [:div.e-text-field--placeholder (text-field.helpers/field-placeholder-attributes field-id field-props)
                                                          (x.components/content placeholder)])))

(defn- text-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:multiline? (boolean)(opt)}
  [field-id {:keys [multiline?] :as field-props}]
  (if multiline? [:textarea.e-text-field--input (text-field.helpers/field-input-attributes field-id field-props)]
                 [:input.e-text-field--input    (text-field.helpers/field-input-attributes field-id field-props)]))

(defn- text-field-input-emphasize
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-text-field--input-emphasize (text-field.helpers/input-emphasize-attributes field-id field-props)
                                      [text-field-input                              field-id field-props]])

(defn- text-field-input-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; The placeholder element has an absolute position, therefore ...
  ; ... it has to be placed in the same ancestor element as the input element!
  ; ... but it cannot be in the very same parent element as the input element!
  ;     (otherwise it covers the input no matter what's their order)
  [:div.e-text-field--input-structure [text-field-placeholder     field-id field-props]
                                      [text-field-input-emphasize field-id field-props]])

(defn- text-field-input-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-text-field--input-container (text-field.helpers/input-container-attributes field-id field-props)
                                      [field-start-adornments                        field-id field-props]
                                      [text-field-input-structure                    field-id field-props]
                                      [field-end-adornments                          field-id field-props]
                                      [text-field-surface                            field-id field-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-text-field (text-field.helpers/field-attributes        field-id field-props)
                     [element.views/element-label                field-id field-props]
                     [text-field-input-container                 field-id field-props]
                     [plain-field.views/plain-field-synchronizer field-id field-props]])

(defn- text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.text-field/field-did-mount    field-id field-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.text-field/field-will-unmount field-id field-props]))
                       :reagent-render         (fn [_ field-props] [text-field-structure field-id field-props])}))

(defn element
  ; XXX#0711
  ; Some other items based on the text-field element and their documentations are linked to here.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;  :autofill-name (keyword)(opt)
  ;  :autofocus? (boolean)(opt)
  ;  :border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :emptiable? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;   [{:color (keyword)(opt)
  ;      :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: :default
  ;     :disabled? (boolean)(opt)
  ;     :icon (keyword)
  ;     :icon-family (keyword)(opt)
  ;      :material-symbols-filled, :material-symbols-outlined
  ;      Default: :material-symbols-outlined
  ;     :label (string)(opt)
  ;     :on-click (metamorphic-event)(opt)
  ;     :tab-indexed? (boolean)(opt)
  ;      Default: true
  ;     :tooltip (metamorphic-content)(opt)}]
  ;  :field-content-f (function)(opt)
  ;   Default: return
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (string)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword or px)(opt)
  ;   :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;  :max-length (integer)(opt)
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :modifier (function)(opt)
  ;  :on-blur (metamorphic-event)(opt)
  ;  :on-changed (metamorphic-event)(opt)
  ;   It happens BEFORE the application state gets updated with the actual value!
  ;   If you have to get the ACTUAL value from the application state, use the
  ;   :on-type-ended event instead!
  ;   It happens BEFORE the application state gets updated with the actual value!
  ;   This event takes the field content as its last parameter
  ;  :on-empty (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-enter (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-focus (metamorphic-event)(opt)
  ;  :on-mount (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-type-ended (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-unmount (metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :placeholder (metamorphic-content)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;   Same as the :end-adornments property.
  ;  :stretch-orientation (keyword)(opt)
  ;   :horizontal, :none
  ;   Default: :none
  ;  :style (map)(opt)
  ;  :surface (metamorphic-content)(opt)
  ;  :validator (map)(opt)
  ;   {:f (function)
  ;    :invalid-message (metamorphic-content)(opt)
  ;    :invalid-message-f (function)(opt)
  ;    :prevalidate? (boolean)(opt)}
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [text-field {...}]
  ;
  ; @usage
  ; [text-field :my-text-field {...}]
  ;
  ; @usage
  ; [text-field {:validator {:f #(not (empty? %))
  ;                          :invalid-message "Invalid value"}}]
  ;
  ; @usage
  ; (defn get-invalid-message [value] "Invalid value")
  ; [text-field {:validator {:f #(not (empty? %))
  ;                          :invalid-message-f get-invalid-message}}]
  ;
  ; @usage
  ; (defn my-surface [field-id])
  ; [text-field {:surface #'my-surface}]
  ;
  ; @usage
  ; (defn my-surface [field-id])
  ; [text-field {:surface {:content #'my-surface}}]
  ;
  ; @usage
  ; [text-field {:modifier #(string/starts-with! % "/")}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (text-field.prototypes/field-props-prototype field-id field-props)]
        [text-field field-id field-props])))
