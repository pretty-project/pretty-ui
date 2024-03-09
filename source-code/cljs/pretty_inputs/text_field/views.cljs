
(ns pretty-inputs.text-field.views
    (:require [fruits.random.api                   :as random]
              [pretty-inputs.engine.api            :as pretty-inputs.engine]
              [pretty-inputs.field.views :as field.views]
              [pretty-inputs.header.views          :as header.views]
              [pretty-inputs.text-field.attributes :as text-field.attributes]
              [pretty-inputs.text-field.prototypes :as text-field.prototypes]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [reagent.core                        :as reagent]
              [pretty-subitems.api :as pretty-subitems]))

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
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented inputs.
  ; Check out the implemented properties.
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
  ;                       :end-adornment-group {:adornment-default {:icon {:icon-size :m}}
  ;                                             :adornments [{:icon {:icon-name :close}}]}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset    id props)
             props (text-field.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
