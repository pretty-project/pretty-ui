
(ns pretty-layouts.body.views
    (:require [fruits.random.api              :as random]
              [pretty-layouts.body.attributes :as body.attributes]
              [pretty-layouts.body.prototypes :as body.prototypes]
              [pretty-layouts.engine.api      :as pretty-layouts.engine]
              [pretty-layouts.methods.api     :as pretty-layouts.methods]
              [reagent.core                   :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (body.attributes/outer-attributes id props)
        [:div (body.attributes/inner-attributes id props)
              [:div (body.attributes/body-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [body id props])}))

(defn view
  ; @description
  ; Body element for layouts.
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-layouts/body.png)
  ; [body {:content    "My body"
  ;        :fill-color :highlight
  ;        :indent     {:all :s}}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [body "My content"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-layouts.methods/apply-layout-shorthand-key  id props SHORTHAND-KEY)
             props (pretty-layouts.methods/apply-layout-presets        id props)
             props (pretty-layouts.methods/import-layout-dynamic-props id props)
             props (pretty-layouts.methods/import-layout-state-events  id props)
             props (pretty-layouts.methods/import-layout-state         id props)
             props (body.prototypes/props-prototype                    id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
