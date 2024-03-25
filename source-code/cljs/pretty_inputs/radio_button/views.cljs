
(ns pretty-inputs.radio-button.views
    (:require [fruits.random.api                     :as random]
              [pretty-inputs.engine.api              :as pretty-inputs.engine]
              [pretty-inputs.header.views            :as header.views]
              [pretty-inputs.methods.api             :as pretty-inputs.methods]
              [pretty-inputs.option-group.views      :as option-group.views]
              [pretty-inputs.radio-button.attributes :as radio-button.attributes]
              [pretty-inputs.radio-button.prototypes :as radio-button.prototypes]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:header       header.views/SHORTHAND-MAP
                    :option-group option-group.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- radio-button
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:header (map)(opt)
  ;  :option-group (map)(opt)
  ;  ...}
  [id {:keys [header option-group] :as props}]
  [:div (radio-button.attributes/outer-attributes id props)
        [:div (radio-button.attributes/inner-attributes id props)
              (if header       [header.views/view       (pretty-subitems/subitem-id id :header)       header])
              (if option-group [option-group.views/view (pretty-subitems/subitem-id id :option-group) option-group])]])

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
                         :reagent-render         (fn [_ props] [radio-button id props])}))

(defn view
  ; @description
  ; Radio button style input.
  ;
  ; @links Implemented inputs
  ; [Header](pretty-core/cljs/pretty-inputs/api.html#header)
  ; [Option-group](pretty-core/cljs/pretty-inputs/api.html#option-group)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented inputs.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-inputs/radio-button.png)
  ; [radio-button {:header {:label       {:content "My radio button"}
  ;                         :helper-text {:content "My helper text"}
  ;                         :info-text   {:content "My info text"}}}
  ;                :option-group {:option-default  {}
  ;                               :option-selected {}
  ;                               :options [{:label {:content "My option #1"}}
  ;                                         {:label {:content "My option #2"}}
  ;                                         {:label {:content "My option #3"}}]
  ;                               :get-value-f #(deref  MY-ATOM)
  ;                               :set-value-f #(reset! MY-ATOM %)}}]
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
             props (radio-button.prototypes/props-prototype          id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
