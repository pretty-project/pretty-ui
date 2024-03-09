
(ns pretty-layouts.header.views
    (:require [fruits.random.api                :as random]
              [pretty-layouts.engine.api        :as pretty-layouts.engine]
              [pretty-layouts.header.attributes :as header.attributes]
              [pretty-layouts.header.prototypes :as header.prototypes]
              [pretty-layouts.methods.api       :as pretty-layouts.methods]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (header.attributes/outer-attributes id props)
        [:div (header.attributes/inner-attributes id props)
              [:div (header.attributes/content-attributes id props) content]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/pseudo-layout-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/pseudo-layout-will-unmount id props))
                         :reagent-render         (fn [_ props] [header id props])}))

(defn view
  ; @description
  ; Header element for layouts.
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ; [Content model](pretty-core/cljs/pretty-models/api.html#content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-layouts/header.png)
  ; [header {:content    "My header"
  ;          :fill-color :highlight
  ;          :indent     {:all :s}}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [header "My content"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-layouts.methods/apply-layout-shorthand-key  id props :content)
             props (pretty-layouts.methods/apply-layout-preset         id props)
             props (pretty-layouts.methods/import-layout-dynamic-props id props)
             props (pretty-layouts.methods/import-layout-state-events  id props)
             props (pretty-layouts.methods/import-layout-state         id props)
             props (header.prototypes/props-prototype                  id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
