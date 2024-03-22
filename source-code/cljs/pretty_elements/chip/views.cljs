
(ns pretty-elements.chip.views
    (:require [fruits.random.api                     :as random]
              [pretty-accessories.label.views        :as label.views]
              [pretty-accessories.tooltip.views      :as tooltip.views]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.chip.attributes       :as chip.attributes]
              [pretty-elements.chip.prototypes       :as chip.prototypes]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-elements.methods.api           :as pretty-elements.methods]
              [pretty-models.api                     :as pretty-models]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:end-adornment-group   adornment-group.views/SHORTHAND-MAP
                    :label                 label.views/SHORTHAND-KEY
                    :start-adornment-group adornment-group.views/SHORTHAND-MAP
                    :tooltip               tooltip.views/SHORTHAND-MAP})
 
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:end-adornment-group (map)(opt)
  ;  :label (map)(opt)
  ;  :start-adornment-group (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [end-adornment-group label start-adornment-group tooltip] :as props}]
  [:div (chip.attributes/outer-attributes id props)
        [:div (chip.attributes/inner-attributes id props)
              (if start-adornment-group [adornment-group.views/view (pretty-subitems/subitem-id id :start-adornment-group) start-adornment-group])
              (if label                 [label.views/view           (pretty-subitems/subitem-id id :label)                 label])
              (if end-adornment-group   [adornment-group.views/view (pretty-subitems/subitem-id id :end-adornment-group)   end-adornment-group])
              (if tooltip               [tooltip.views/view         (pretty-subitems/subitem-id id :tooltip)               tooltip])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [chip id props])}))

(defn view
  ; @description
  ; Clickable chip style element.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-ui/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/chip.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (chip.prototypes/props-prototype                      id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
