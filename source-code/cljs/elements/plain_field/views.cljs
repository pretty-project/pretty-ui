
(ns elements.plain-field.views
    (:require [elements.plain-field.attributes :as plain-field.attributes]
              [elements.plain-field.env        :as plain-field.env]
              [elements.plain-field.prototypes :as plain-field.prototypes]
              [elements.plain-field.utils      :as plain-field.utils]
              [metamorphic-content.api         :as metamorphic-content]
              [random.api                      :as random]
              [re-frame.api                    :as r]
              [reagent.api                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-field-synchronizer-debug
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-content-f (function)
  ;  :value-path (Re-Frame path vector)}
  [field-id {:keys [field-content-f value-path]}]
  ; HACK#9910
  (let [stored-value @(r/subscribe [:get-item value-path])]
       [:div [:br] "field content:   " (plain-field.env/get-field-content field-id)
             [:br] "field output:    " (plain-field.env/get-field-output  field-id)
             [:br] "stored value:    " (str             stored-value)
             [:br] "derived content: " (field-content-f stored-value)]))

(defn- plain-field-synchronizer-sensor
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (*) stored-value
  [field-id field-props stored-value]
  ; HACK#9910
  (reagent/lifecycles {:component-will-unmount (fn [_ _ _] (plain-field.utils/synchronizer-will-unmount-f field-id field-props))
                       :component-did-mount    (fn [_ _ _] (plain-field.utils/synchronizer-did-mount-f    field-id field-props))
                       :component-did-update   (fn [%]     (plain-field.utils/synchronizer-did-update-f   field-id %))
                      ;:reagent-render         (fn [_ _ _] [plain-field-synchronizer-debug                field-id field-props])
                       :reagent-render         (fn [_ _ _])}))

(defn- plain-field-synchronizer
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:value-path (Re-Frame path vector)}
  [field-id {:keys [value-path] :as field-props}]
  ; HACK#9910
  (let [stored-value @(r/subscribe [:get-item value-path])]
       [plain-field-synchronizer-sensor field-id field-props stored-value]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-field-structure
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:surface (metamorphic-content)(opt)}
  [field-id {:keys [surface] :as field-props}]
  [:div (plain-field.attributes/field-attributes field-id field-props)
        ; ...
        [:div (plain-field.attributes/field-body-attributes field-id field-props)
              [:input (plain-field.attributes/field-input-attributes field-id field-props)]]
        ; ...
        (and surface (plain-field.env/surface-visible? field-id)
                     [:div (plain-field.attributes/field-surface-attributes field-id field-props)
                           [metamorphic-content/compose surface]])
        ; HACK#9910
        [plain-field-synchronizer field-id field-props]])

(defn- plain-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.plain-field/field-did-mount    field-id field-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.plain-field/field-will-unmount field-id field-props]))
                       :reagent-render         (fn [_ field-props] [plain-field-structure field-id field-props])}))

(defn element
  ; @description
  ; The plain-field element writes its actual value into the Re-Frame state
  ; delayed after the user stopped typing or without a delay when the user
  ; leaves the field!
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;   Removes the value stored in the application state (on the value-path)
  ;   when the element unmounts.
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
  ;  :modifier-f (function)(opt)
  ;  :on-blur (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-change-f (function)(opt)
  ;   This function takes the field-id, the field-props and the change event as its parameters.
  ;  :on-changed (Re-Frame metamorphic-event)(opt)
  ;   It happens BEFORE the application state gets updated with the actual value!
  ;   If you want to get the ACTUAL value from the application state, use the
  ;   :on-type-ended event instead!
  ;   This event takes the field content as its last parameter
  ;  :on-focus (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-type-ended (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;   {:border-radius (map)(opt)
  ;    :content (metamorphic-content)(opt)
  ;    :indent (map)(opt)}
  ;  :value-path (Re-Frame path vector)(opt)}
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
