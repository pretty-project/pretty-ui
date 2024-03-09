
(ns pretty-inputs.checkbox.views
    (:require [fruits.random.api                 :as random]
              [pretty-inputs.checkbox.attributes :as checkbox.attributes]
              [pretty-inputs.checkbox.prototypes :as checkbox.prototypes]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-inputs.methods.api :as pretty-inputs.methods]
              [pretty-inputs.header.views        :as header.views]
              [pretty-inputs.option-group.views  :as option-group.views]
              [pretty-subitems.api               :as pretty-subitems]
              [reagent.core                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:header (map)(opt)
  ;  :option-group (map)(opt)
  ;  ...}
  [id {:keys [header option-group] :as props}]
  [:div (checkbox.attributes/outer-attributes id props)
        [:div (checkbox.attributes/inner-attributes id props)
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
                         :reagent-render         (fn [_ props] [checkbox id props])}))

(defn view
  ; @description
  ; Checkbox style input.
  ;
  ; @links Implemented inputs
  ; [Header](pretty-core/cljs/pretty-inputs/api.html#header)
  ; [Option-group](pretty-core/cljs/pretty-inputs/api.html#option-group)
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented inputs.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-inputs/checkbox.png)
  ; [checkbox {:header {:label       {:content "My checkbox"}
  ;                     :helper-text {:content "My helper text"}
  ;                     :info-text   {:content "My info text"}}}
  ;            :option-group {:option-default  {}
  ;                           :option-selected {}
  ;                           :options [{:label {:content "My option #1"}}
  ;                                     {:label {:content "My option #2"}}
  ;                                     {:label {:content "My option #3"}}]
  ;                           :get-value-f #(deref  MY-ATOM)
  ;                           :set-value-f #(reset! MY-ATOM %)}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-preset         id props)
             props (pretty-inputs.methods/import-input-dynamic-props id props)
             props (pretty-inputs.methods/import-input-state-events  id props)
             props (pretty-inputs.methods/import-input-state         id props)
             props (checkbox.prototypes/props-prototype              id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
