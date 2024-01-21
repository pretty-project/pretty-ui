
(ns pretty-inputs.plain-field.views
    (:require [fruits.random.api                    :as random]
              [metamorphic-content.api              :as metamorphic-content]
              [pretty-inputs.core.side-effects      :as core.side-effects]
              [pretty-inputs.plain-field.attributes :as plain-field.attributes]
              [pretty-inputs.plain-field.env        :as plain-field.env]
              [pretty-inputs.plain-field.prototypes :as plain-field.prototypes]
              [pretty-presets.api                   :as pretty-presets]
              [reagent.api                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field
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

(defn- text-field-lifecycles
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    field-id field-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount field-id field-props))
                       :reagent-render         (fn [_ field-props] [text-field field-id field-props])}))

(defn input
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;  :autofocus? (boolean)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-value (string)(opt)
  ;  :modifier-f (function)(opt)
  ;  :on-blur-f (function)(opt)
  ;  :on-changed-f (function)(opt)
  ;  :on-enter-f (function)(opt)
  ;  :on-escape-f (function)(opt)
  ;  :on-focus-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-type-ended-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
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
  ; [text-field {...}]
  ;
  ; @usage
  ; [text-field :my-text-field {...}]
  ([field-props]
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (pretty-presets/apply-preset field-props)]
             ; field-props (text-field.prototypes/field-props-prototype field-props)
            [text-field-lifecycles field-id field-props]))))
