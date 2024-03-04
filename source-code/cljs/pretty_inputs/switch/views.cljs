
(ns pretty-inputs.switch.views
    (:require [fruits.random.api :as random]
              [pretty-inputs.switch.attributes :as switch.attributes]
              [pretty-inputs.switch.prototypes :as switch.prototypes]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-inputs.option-group.views :as option-group.views]
              [pretty-inputs.header.views :as header.views]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:header (map)(opt)
  ;  :option-group (map)(opt)
  ;  ...}
  [id {:keys [header option-group] :as props}]
  [:div (switch.attributes/outer-attributes id props)
        [:div (switch.attributes/inner-attributes id props)
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
                         :reagent-render         (fn [_ props] [switch id props])}))

(defn view
  ; @description
  ; Checkbox style input.
  ;
  ; @links Implemented inputs
  ; [Header](pretty-core/cljs/pretty-inputs/api.html#header)
  ; [Option-group](pretty-core/cljs/pretty-inputs/api.html#option-group)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
  ; Check out the implemented inputs.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/switch.png)
  ; [switch {:header {:label       {:content "My switch"}
  ;                   :helper-text {:content "My helper text"}
  ;                   :info-text   {:content "My info text"}}}
  ;          :option-group {:option-default  {}
  ;                         :option-selected {}
  ;                         :options [{:label {:content "My option #1"}}
  ;                                   {:label {:content "My option #2"}}
  ;                                   {:label {:content "My option #3"}}]}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset id props)
             props (switch.prototypes/props-prototype  id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
