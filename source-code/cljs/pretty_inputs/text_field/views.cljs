
(ns pretty-inputs.text-field.views
    (:require [fruits.random.api                   :as random]
              [pretty-inputs.engine.api            :as pretty-inputs.engine]
              [pretty-inputs.field.views           :as field.views]
              [pretty-inputs.header.views          :as header.views]
              [pretty-inputs.methods.api           :as pretty-inputs.methods]
              [pretty-inputs.text-field.attributes :as text-field.attributes]
              [pretty-inputs.text-field.prototypes :as text-field.prototypes]
              [pretty-subitems.api                 :as pretty-subitems]
              [reagent.core                        :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:field  field.views/SHORTHAND-MAP
                    :header header.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {field (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [id {:keys [field header] :as props}]
  [:div (text-field.attributes/outer-attributes id props)
        [:div (text-field.attributes/inner-attributes id props)
              (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
              (if field  [field.views/view  (pretty-subitems/subitem-id id :field)  field])]])

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
                         :reagent-render         (fn [_ props] [text-field id props])}))

(defn view
  ; @description
  ; Text field input.
  ;
  ; @links Implemented inputs
  ; [Field](pretty-core/cljs/pretty-inputs/api.html#field)
  ; [Header](pretty-core/cljs/pretty-inputs/api.html#header)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented inputs.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-inputs/text-field.png)
  ; [text-field {:header {:label       {:content "My text field"}
  ;                       :helper-text {:content "My helper text"}
  ;                       :info-text   {:content "My info text"}}
  ;              :field  {:border-radius       {:all :s}
  ;                       :fill-color          :highlight
  ;                       :indent              {:all :xs}
  ;                       :get-value-f         #(deref  MY-ATOM)
  ;                       :set-value-f         #(reset! MY-ATOM %)
  ;                       :placeholder-text    {:content "My placeholder text"}
  ;                       ;; start-adornment-group {...}
  ;                       :end-adornment-group {:adornment-default {:icon {:icon-size :m}}
  ;                                             :adornments [{:icon {:icon-name :close}}]}}]
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
             props (text-field.prototypes/props-prototype            id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
