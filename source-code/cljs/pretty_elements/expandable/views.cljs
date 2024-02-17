
(ns pretty-elements.expandable.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.button.views          :as button.views]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [reagent.core :as reagent]
              [dynamic-props.api :as dynamic-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-button
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:button (map)(opt)
  ;  ...}
  [expandable-id {:keys [button] :as expandable-props}]
  (if button (let [button-id    (pretty-elements.engine/element-id->subitem-id expandable-id :button)
                   button-props (expandable.prototypes/button-props-prototype  expandable-id expandable-props)]
                  [button.views/view button-id button-props])))

(defn expandable-content
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [expandable-id {:keys [content] :as expandable-props}]
  [:div (expandable.attributes/expandable-content-attributes expandable-id expandable-props) content])

(defn expandable
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ; {:expanded? (boolean)(opt)
  ;  ...}
  [expandable-id {:keys [expanded?] :as expandable-props}]
  (if expanded? [:div (expandable.attributes/expandable-attributes expandable-id expandable-props)
                      [:div (expandable.attributes/expandable-body-attributes expandable-id expandable-props)
                            [expandable-button                                expandable-id expandable-props]
                            [expandable-content                               expandable-id expandable-props]]]))

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
  ; @links Implemented elements
  ; [Button](pretty-ui/cljs/pretty-elements/api.html#button)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Expandable properties](pretty-core/cljs/pretty-properties/api.html#expandable-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Position properties](pretty-core/cljs/pretty-properties/api.html#position-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/expandable.png)
  ; [expandable {:border-radius {:all :m}
  ;              :button        {:label "My expandable"}
  ;              :content       "My expandable content"
  ;              :fill-color    :highlight
  ;              :indent        {:all :s}
  ;              :text-color    :muted}]
  ([expandable-props]
   [view (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   ; @note (tutorials#parameterizing)
   (fn [_ expandable-props]
       (let [expandable-props (pretty-presets.engine/apply-preset                     expandable-id expandable-props)
             expandable-props (expandable.prototypes/expandable-props-prototype       expandable-id expandable-props)
             expandable-props (pretty-elements.engine/element-subitem<-disabled-state expandable-id expandable-props :button)
             expandable-props (pretty-elements.engine/leave-element-disabled-state    expandable-id expandable-props :button)
             expandable-props (dynamic-props/import-props                             expandable-id expandable-props)]
            [view-lifecycles expandable-id expandable-props]))))
