
(ns pretty-inputs.counter.views
    (:require [fruits.random.api                :as random]
              [pretty-accessories.label.views   :as label.views]
              [pretty-elements.button.views     :as button.views]
              [pretty-inputs.counter.attributes :as counter.attributes]
              [pretty-inputs.counter.prototypes :as counter.prototypes]
              [pretty-inputs.engine.api         :as pretty-inputs.engine]
              [pretty-inputs.methods.api        :as pretty-inputs.methods]
              [pretty-subitems.api              :as pretty-subitems]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:end-button   button.views/SHORTHAND-MAP
                    :label        label.views/SHORTHAND-KEY
                    :start-button button.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:end-button (map)(opt)
  ;  :label (map)(opt)
  ;  :start-button (map)(opt)
  ;  ...}
  [id {:keys [end-button label start-button] :as props}]
  [:div (counter.attributes/outer-attributes   id props)
        [pretty-inputs.engine/input-synchronizer id props]
        [:div (counter.attributes/inner-attributes id props)
              (if start-button [button.views/view (pretty-subitems/subitem-id id :start-button) start-button])
              (if label        [label.views/view  (pretty-subitems/subitem-id id :label)        label])
              (if end-button   [button.views/view (pretty-subitems/subitem-id id :end-button)   end-button])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount id props))
                         :reagent-render         (fn [_ props] [counter id props])}))

(defn view
  ; @param (keyword)(opt) id
  ; @param (map) props
  ;
  ; @usage (pretty-inputs/counter.png)
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-inputs.methods/apply-input-preset         id props)
             props (pretty-inputs.methods/import-input-dynamic-props id props)
             props (pretty-inputs.methods/import-input-state-events  id props)
             props (pretty-inputs.methods/import-input-state         id props)
             props (counter.prototypes/props-prototype               id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
