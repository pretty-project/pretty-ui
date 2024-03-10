
(ns pretty-accessories.tooltip.views
    (:require [fruits.random.api                     :as random]
              [pretty-accessories.engine.api         :as pretty-accessories.engine]
              [pretty-accessories.icon.views         :as icon.views]
              [pretty-accessories.label.views        :as label.views]
              [pretty-accessories.methods.api        :as pretty-accessories.methods]
              [pretty-accessories.tooltip.attributes :as tooltip.attributes]
              [pretty-accessories.tooltip.prototypes :as tooltip.prototypes]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:icon  icon.views/SHORTHAND-KEY
                    :label label.views/SHORTHAND-KEY})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tooltip
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [id {:keys [icon label] :as props}]
  [:div (tooltip.attributes/outer-attributes id props)
        [:div (tooltip.attributes/inner-attributes id props)
              (cond label [label.views/view (pretty-subitems/subitem-id id :label) label]
                    icon  [icon.views/view  (pretty-subitems/subitem-id id :icon)  icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-accessories.engine/accessory-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-accessories.engine/accessory-will-unmount id props))
                         :reagent-render         (fn [_ props] [tooltip id props])}))

(defn view
  ; @description
  ; Tooltip accessory for elements.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-accessories/tooltip.png)
  ; [tooltip {:border-radius {:all :s}
  ;           :label         {:content "My tooltip" :text-color :invert}
  ;           :fill-color    :invert
  ;           :indent        {:all :xxs}
  ;           :position      :br}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-accessories.methods/apply-accessory-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-accessories.methods/apply-accessory-preset         id props)
             props (pretty-accessories.methods/import-accessory-dynamic-props id props)
             props (pretty-accessories.methods/import-accessory-state-events  id props)
             props (pretty-accessories.methods/import-accessory-state         id props)
             props (tooltip.prototypes/props-prototype                        id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
