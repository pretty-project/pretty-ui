
(ns pretty-elements.horizontal-separator.views
    (:require [fruits.random.api                               :as random]
              [pretty-accessories.label.views                  :as label.views]
              [pretty-elements.engine.api                      :as pretty-elements.engine]
              [pretty-elements.horizontal-separator.attributes :as horizontal-separator.attributes]
              [pretty-elements.horizontal-separator.prototypes :as horizontal-separator.prototypes]
              [pretty-elements.methods.api                     :as pretty-elements.methods]
              [pretty-subitems.api                             :as pretty-subitems]
              [reagent.core                                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:label label.views/SHORTHAND-KEY})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-separator
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:label (map)(opt)
  ;  ...}
  [id {:keys [label] :as props}]
  [:div (horizontal-separator.attributes/outer-attributes id props)
        [:div (horizontal-separator.attributes/inner-attributes id props)
              (when :always [:hr (horizontal-separator.attributes/line-attributes id props)])
              (when label   [label.views/view (pretty-subitems/subitem-id id :label) label])
              (when :always [:hr (horizontal-separator.attributes/line-attributes id props)])]])

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
                         :reagent-render         (fn [_ props] [horizontal-separator id props])}))

(defn view
  ; @description
  ; Horizontal line element with optional label.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Line canvas model](pretty-core/cljs/pretty-models/api.html#line-canvas-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/horizontal-separator.png)
  ; [horizontal-separator {:gap        :xs
  ;                        :label      {:content "My horizontal separator" :text-color :muted}
  ;                        :line-color :muted}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-presets        id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (horizontal-separator.prototypes/props-prototype      id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
