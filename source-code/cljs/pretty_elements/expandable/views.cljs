
(ns pretty-elements.expandable.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core :as reagent]
              [dynamic-props.api :as dynamic-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:content (multitype-content)(opt)
  ;  :expanded? (boolean)(opt)
  ;  ...}
  [expandable-id {:keys [content expanded?] :as expandable-props}]
  (if expanded? [:div (expandable.attributes/expandable-attributes expandable-id expandable-props)
                      [:div (expandable.attributes/expandable-inner-attributes expandable-id expandable-props)
                            [:div (expandable.attributes/expandable-content-attributes expandable-id expandable-props) content]]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    expandable-id expandable-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount expandable-id expandable-props))
                         :reagent-render         (fn [_ expandable-props] [expandable expandable-id expandable-props])}))

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
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)  
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ; Check out the implemented controls.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/expandable.png)
  ; [expandable {:border-radius {:all :m}
  ;              :content       [:div "My expandable"]
  ;              :fill-color    :highlight
  ;              :indent        {:all :s}}]
  ([expandable-props]
   [view (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   ; @note (tutorials#parameterizing)
   (fn [_ expandable-props]
       (let [expandable-props (pretty-presets.engine/apply-preset               expandable-id expandable-props)
             expandable-props (expandable.prototypes/expandable-props-prototype expandable-id expandable-props)
             expandable-props (dynamic-props/import-props                       expandable-id expandable-props)]
            [view-lifecycles expandable-id expandable-props]))))
