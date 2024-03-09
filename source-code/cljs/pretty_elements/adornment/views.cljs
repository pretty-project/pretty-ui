
(ns pretty-elements.adornment.views
    (:require [fruits.random.api                    :as random]
              [pretty-accessories.api               :as pretty-accessories]
              [pretty-elements.adornment.attributes :as adornment.attributes]
              [pretty-elements.adornment.prototypes :as adornment.prototypes]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.methods.api           :as pretty-elements.methods]
              [pretty-models.api                    :as pretty-models]
              [pretty-subitems.api                  :as pretty-subitems]
              [pretty-models.api :as pretty-models]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [cover icon label tooltip] :as props}]
  [:div (adornment.attributes/outer-attributes id props)
        [(pretty-models/clickable-model-auto-tag props) (adornment.attributes/inner-attributes id props)
         (cond label   [pretty-accessories/label   (pretty-subitems/subitem-id id :label)   label]
               icon    [pretty-accessories/icon    (pretty-subitems/subitem-id id :icon)    icon])
         (when cover   [pretty-accessories/cover   (pretty-subitems/subitem-id id :cover)   cover])
         (when tooltip [pretty-accessories/tooltip (pretty-subitems/subitem-id id :tooltip) tooltip])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  ;
  ; @note (#8097)
  ; The 'element-did-update' function re-registers the keypress events of the element when the property map gets changed.
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   id props %))
                         :reagent-render         (fn [_ props] [adornment id props])}))

(defn view
  ; @description
  ; Downsized button element.
  ;
  ; @links Implemented accessories
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Icon](pretty-ui/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Keypress control properties](pretty-core/cljs/pretty-properties/api.html#keypress-control-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Progress properties](pretty-core/cljs/pretty-properties/api.html#progress-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/adornment.png)
  ; [adornment {:border-radius {:all :xs}
  ;             :fill-color    :highlight
  ;             :hover-color   :highlight
  ;             :icon          {:icon-name :settings}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map   id props {:icon :icon-name :label :content})
             props (pretty-elements.methods/apply-element-preset          id props)
             props (pretty-elements.methods/import-element-timeout-events id props)
             props (pretty-elements.methods/import-element-timeout-state  id props)
             props (adornment.prototypes/props-prototype                  id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
