
(ns pretty-elements.adornment.views
    (:require [fruits.random.api                    :as random]
              [pretty-accessories.api               :as pretty-accessories]
              [pretty-elements.adornment.attributes :as adornment.attributes]
              [pretty-elements.adornment.prototypes :as adornment.prototypes]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.methods.api          :as pretty-elements.methods]
              [pretty-models.api                    :as pretty-models]
              [pretty-models.api                    :as pretty-models]
              [pretty-subitems.api                  :as pretty-subitems]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:cover   cover.views/SHORTHAND-MAP
                    :icon    icon.views/SHORTHAND-KEY
                    :label   label.views/SHORTHAND-KEY
                    :tooltip tooltip.views/SHORTHAND-MAP})

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
        [(pretty-models/click-control-auto-tag props) (adornment.attributes/inner-attributes id props)
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
  ; @links Implemented models
  ; [Click control model](pretty-core/cljs/pretty-models/api.html#click-control-model)
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
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
       (let [props (pretty-elements.methods/apply-element-shorthand-map    id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset           id props)
             props (pretty-elements.methods/import-element-dynamic-props   id props)
             props (pretty-elements.methods/import-element-focus-reference id props)
             props (pretty-elements.methods/import-element-state-events    id props)
             props (pretty-elements.methods/import-element-state           id props)
             props (pretty-elements.methods/import-element-timeout-events  id props)
             props (pretty-elements.methods/import-element-timeout         id props)
             props (adornment.prototypes/props-prototype                   id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
