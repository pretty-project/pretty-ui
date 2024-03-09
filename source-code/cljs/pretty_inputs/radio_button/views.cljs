
(ns pretty-inputs.radio-button.views
    (:require [fruits.random.api                     :as random]
              [pretty-inputs.engine.api              :as pretty-inputs.engine]
              [pretty-inputs.header.views            :as header.views]
              [pretty-inputs.option-group.views      :as option-group.views]
              [pretty-inputs.radio-button.attributes :as radio-button.attributes]
              [pretty-inputs.radio-button.prototypes :as radio-button.prototypes]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]))

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
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented inputs.
  ; Check out the implemented properties.
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
       (let [props (pretty-presets.engine/apply-preset      id props)
             props (radio-button.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
