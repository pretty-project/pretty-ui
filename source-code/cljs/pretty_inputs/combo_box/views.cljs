
(ns pretty-inputs.combo-box.views
    (:require [fruits.random.api                  :as random]
              [pretty-inputs.combo-box.attributes :as combo-box.attributes]
              [pretty-inputs.combo-box.prototypes :as combo-box.prototypes]
              [pretty-inputs.engine.api           :as pretty-inputs.engine]
              [pretty-inputs.field.views          :as field.views]
              [pretty-inputs.header.views         :as header.views]
              [pretty-inputs.methods.api          :as pretty-inputs.methods]
              [pretty-inputs.option-group.views   :as option-group.views]
              [pretty-subitems.api                :as pretty-subitems]
              [reagent.core                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:field        field.views/SHORTHAND-MAP
                    :header       header.views/SHORTHAND-MAP
                    :option-group option-group.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- combo-box
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:field (map)(opt)
  ;  :header (map)(opt)
  ;  :option-group (map)(opt)
  ;  ...}
  [id {:keys [field header option-group] :as props}]
  (let [option-group [option-group.views/view (pretty-subitems/subitem-id id :option-group) option-group]
        field        (assoc-in field [:expandable :content] option-group)]
       [:div (combo-box.attributes/outer-attributes id props)
             [:div (combo-box.attributes/inner-attributes id props)
                   (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
                   (if field  [field.views/view  (pretty-subitems/subitem-id id :field)  field])]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/pseudo-input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/pseudo-input-will-unmount id props))
                         :reagent-render         (fn [_ props] [combo-box id props])}))

(defn view
  ; @description
  ; Combo box style input.
  ;
  ; @links Implemented inputs
  ; [Field](pretty-core/cljs/pretty-inputs/api.html#field)
  ; [Header](pretty-core/cljs/pretty-inputs/api.html#header)
  ; [Option-group](pretty-core/cljs/pretty-inputs/api.html#option-group)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ; Check out the implemented inputs.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-inputs/combo-box.png)
  ; ...
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-inputs.methods/apply-input-presets        id props)
             props (pretty-inputs.methods/import-input-dynamic-props id props)
             props (pretty-inputs.methods/import-input-state-events  id props)
             props (pretty-inputs.methods/import-input-state         id props)
             props (combo-box.prototypes/props-prototype             id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
