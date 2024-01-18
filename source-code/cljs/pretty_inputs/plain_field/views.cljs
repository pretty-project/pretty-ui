
(ns pretty-inputs.plain-field.views
    (:require [fruits.random.api                    :as random]
              [metamorphic-content.api              :as metamorphic-content]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [pretty-inputs.plain-field.attributes :as plain-field.attributes]
              [pretty-inputs.plain-field.env        :as plain-field.env]
              [pretty-inputs.plain-field.prototypes :as plain-field.prototypes]
              [pretty-inputs.plain-field.utils      :as plain-field.utils]
              [pretty-presets.api                   :as pretty-presets]
              [reagent.api                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-field
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
        (and surface (plain-field.env/field-surface-visible? field-id field-props)
                     [:div (plain-field.attributes/field-surface-attributes field-id field-props)
                           [metamorphic-content/compose (:content surface) (:placeholder surface)]])])
        ; HACK#9910
        ;[plain-field-synchronizer field-id field-props]])

(defn- plain-field-lifecycles
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    field-id field-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount field-id field-props))
                       :reagent-render         (fn [_ field-props] [plain-field field-id field-props])}))

(defn input
  ; @description
  ; The 'plain-field' element writes its actual value into the Re-Frame state
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
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-value (string)(opt)
  ;  :modifier-f (function)(opt)
  ;  :on-blur (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the field content as parameter.
  ;  :on-change-f (function)(opt)
  ;   Takes the 'field-id', the 'field-props' and the change event as parameters.
  ;  :on-changed (function or Re-Frame metamorphic-event)(opt)
  ;   Applied BEFORE the application state gets updated with the actual value!
  ;   If you want to get the ACTUAL value from the application state, use the ':on-type-ended-f' function instead!
  ;   Takes the field content as parameter.
  ;  :on-focus (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the field content as parameter.
  ;  :on-mount (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the field content as parameter.
  ;  :on-type-ended-f (function)(opt)
  ;   Takes the field content as parameter.
  ;  :on-unmount (function or Re-Frame metamorphic-event)(opt)
  ;   Takes the field content as parameter.
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;   {:border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :content (metamorphic-content)(opt)
  ;    :indent (map)(opt)
  ;    :placeholder (metamorphic-content)(opt)}
  ;  :value-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [plain-field {...}]
  ;
  ; @usage
  ; [plain-field :my-plain-field {...}]
  ([field-props]
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (pretty-presets/apply-preset                           field-props)
             field-props (plain-field.prototypes/field-props-prototype field-id field-props)]
            [plain-field-lifecycles field-id field-props]))))
