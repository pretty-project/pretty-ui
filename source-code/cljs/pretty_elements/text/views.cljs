
(ns pretty-elements.text.views
    (:require [fruits.hiccup.api               :as hiccup]
              [fruits.random.api               :as random]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-elements.methods.api     :as pretty-elements.methods]
              [pretty-elements.text.attributes :as text.attributes]
              [pretty-elements.text.prototypes :as text.prototypes]
              [reagent.core                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (text.attributes/outer-attributes id props)
        [:div (text.attributes/inner-attributes id props)
              [:div (text.attributes/content-attributes id props)
                    (hiccup/parse-newlines [:<> content])]]])

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
                         :reagent-render         (fn [_ props] [text id props])}))

(defn view
  ; @description
  ; Customizable text element.
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ; [Content model](pretty-core/cljs/pretty-models/api.html#content-model)
  ; [Multiline model](pretty-core/cljs/pretty-models/api.html#multiline-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/text.png)
  ; [text {:border-radius {:all :m}
  ;        :content       "My text line #1\nMy text line #2\nMy text line #3"
  ;        :fill-color    :highlight
  ;        :outer-height  :5xl
  ;        :outer-width   :5xl}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [text "My text"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-key  id props :content)
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (text.prototypes/props-prototype                      id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
