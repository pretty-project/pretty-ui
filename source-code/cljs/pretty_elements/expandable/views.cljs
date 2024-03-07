
(ns pretty-elements.expandable.views
    (:require [dynamic-props.api                     :as dynamic-props]
              [fruits.random.api                     :as random]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]
              [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  :expanded? (boolean)(opt)
  ;  ...}
  [id {:keys [content expanded?] :as props}]
  [:div (expandable.attributes/outer-attributes id props)
        [:div (expandable.attributes/inner-attributes id props)
              [:div (expandable.attributes/content-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [expandable id props])}))

(defn view
  ; @description
  ; Expandable element with controller functions.
  ;
  ; @links Implemented controls
  ; [expand-content!](pretty-ui/cljs/pretty-controls/api.html#expand-content_)
  ; [collapse-content!](pretty-ui/cljs/pretty-controls/api.html#collapse-content_)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Expandable properties](pretty-core/cljs/pretty-properties/api.html#expandable-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
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
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented controls.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/expandable.png)
  ; [expandable {:border-radius {:all :m}
  ;              :content       [:div "My expandable"]
  ;              :fill-color    :highlight
  ;              :indent        {:all :s}}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [card "My content"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-models/use-longhand            id props :content)
             props (pretty-presets.engine/apply-preset    id props)
             props (expandable.prototypes/props-prototype id props)
             props (dynamic-props/import-props            id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
