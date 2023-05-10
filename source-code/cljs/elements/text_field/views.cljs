
(ns elements.text-field.views
    (:require [elements.element.views         :as element.views]
              [elements.plain-field.env       :as plain-field.env]
              [elements.plain-field.views     :as plain-field.views]
              [elements.text-field.attributes :as text-field.attributes]
              [elements.text-field.env        :as text-field.env]
              [elements.text-field.prototypes :as text-field.prototypes]
              [hiccup.api                     :as hiccup]
              [metamorphic-content.api        :as metamorphic-content]
              [random.api                     :as random]
              [re-frame.api                   :as r]
              [reagent.api                    :as reagent]
              [vector.api                     :as vector]))

;; -- Field adornments components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-adornment
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (string)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)}
  [field-id field-props {:keys [icon label on-click] :as adornment-props}]
  (let [adornment-props (text-field.prototypes/adornment-props-prototype field-props adornment-props)]
       [(if on-click :button :div)
        (text-field.attributes/adornment-attributes field-id field-props adornment-props)
        (or icon (metamorphic-content/compose label))]))

(defn field-end-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [end-adornments (text-field.prototypes/end-adornments-prototype field-id field-props)]
       (if (vector/nonempty? end-adornments)
           (letfn [(f [adornment-props] [field-adornment field-id field-props adornment-props])]
                  (hiccup/put-with [:div {:class :e-text-field--adornments}] end-adornments f))
           [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)])))

(defn field-start-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (if (vector/nonempty? start-adornments)
      (letfn [(f [adornment-props] [field-adornment field-id field-props adornment-props])]
             (hiccup/put-with [:div {:class :e-text-field--adornments}] start-adornments f))
      [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-structure
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  [field-id {:keys [multiline? placeholder surface] :as field-props}]
  ; The placeholder element has an absolute position, therefore ...
  ; ... it has to be placed in the same ancestor element as the input element!
  ; ... but it cannot be in the very same parent element as the input element!
  ;     (otherwise it covers the input no matter what's their order)
  [:div (text-field.attributes/field-attributes field-id field-props)
        ; ...
        [element.views/element-label field-id field-props]
        ; ...
        [:div (text-field.attributes/input-container-attributes field-id field-props)
              ; ...
              [field-start-adornments field-id field-props]
              ; ...
              [:div {:class :e-text-field--input-structure}
                    ; ...
                    (if placeholder (if-let [field-empty? (plain-field.env/field-empty? field-id)]
                                            [:div (text-field.attributes/field-placeholder-attributes field-id field-props)
                                                  (metamorphic-content/compose placeholder)]))
                    ; ...
                    [:div (text-field.attributes/input-emphasize-attributes field-id field-props)
                          [(if multiline? :textarea :input)
                           (text-field.attributes/field-input-attributes field-id field-props)]]]
              ; ...
              [field-end-adornments field-id field-props]
              ; ...
              (if surface (if (plain-field.env/surface-visible? field-id)
                              [:div (text-field.attributes/field-surface-attributes field-id field-props)
                                    [metamorphic-content/compose surface]]))]
        ; ...
        [plain-field.views/plain-field-synchronizer field-id field-props]])

(defn- text-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.text-field/field-did-mount    field-id field-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.text-field/field-will-unmount field-id field-props]))
                       :reagent-render         (fn [_ field-props] [text-field-structure field-id field-props])}))

(defn element
  ; XXX#0711
  ; Some other items based on the text-field element and their documentations link here.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;  :autofill-name (keyword)(opt)
  ;  :autofocus? (boolean)(opt)
  ;  :border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :emptiable? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;   [{:click-effect (keyword)(opt)
  ;      :opacity
  ;      Default: :opacity
  ;     :color (keyword)(opt)
  ;      :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: :default
  ;     :disabled? (boolean)(opt)
  ;     :hover-effect (keyword)(opt)
  ;      :opacity
  ;     :icon (keyword)
  ;     :icon-family (keyword)(opt)
  ;      :material-symbols-filled, :material-symbols-outlined
  ;      Default: :material-symbols-outlined
  ;     :label (string)(opt)
  ;     :on-click (Re-Frame metamorphic-event)(opt)
  ;     :tab-indexed? (boolean)(opt)
  ;      Default: true
  ;     :tooltip-content (metamorphic-content)(opt)}]
  ;  :field-content-f (function)(opt)
  ;   Default: return
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default: :normal
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
  ;  :initial-value (string)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword or px)(opt)
  ;   :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, left, :right, bottom, :top
  ;  :max-length (integer)(opt)
  ;  :modifier (function)(opt)
  ;  :on-blur (Re-Frame metamorphic-event)(opt)
  ;  :on-changed (Re-Frame metamorphic-event)(opt)
  ;   It happens BEFORE the application state gets updated with the actual value!
  ;   If you have to get the ACTUAL value from the application state, use the
  ;   :on-type-ended event instead!
  ;   It happens BEFORE the application state gets updated with the actual value!
  ;   This event takes the field content as its last parameter
  ;  :on-empty (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-enter (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-focus (Re-Frame metamorphic-event)(opt)
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-type-ended (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :placeholder (metamorphic-content)(opt)
  ;  :reveal-effect (keyword)(opt)
  ;   :delayed, :opacity
  ;  :start-adornments (maps in vector)(opt)
  ;   Same as the :end-adornments property.
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;   {:border-radius (map)(opt)
  ;    :content (metamorphic-content)(opt)
  ;    :indent (map)(opt)}
  ;  :validator (map)(opt)
  ;   {:f (function)
  ;    :invalid-message (metamorphic-content)(opt)
  ;    :invalid-message-f (function)(opt)
  ;    :prevalidate? (boolean)(opt)}
  ;  :value-path (Re-Frame path vector)(opt)
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
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
