
(ns pretty-elements.blank.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.blank.attributes :as blank.attributes]
              [pretty-elements.blank.prototypes :as blank.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.methods.api      :as pretty-elements.methods]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (blank.attributes/outer-attributes id props)
        [:div (blank.attributes/inner-attributes id props)
              [:div (blank.attributes/content-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [blank id props])}))

(defn view
  ; @description
  ; Simplified element for displaying content.
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ; [Content model](pretty-core/cljs/pretty-models/api.html#content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/blank.png)
  ; [blank {:content [:div "My blank"]}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [blank "My blank"]
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
             props (blank.prototypes/props-prototype                     id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
