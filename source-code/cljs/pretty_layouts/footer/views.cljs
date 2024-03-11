
(ns pretty-layouts.footer.views
    (:require [fruits.random.api                :as random]
              [pretty-layouts.engine.api        :as pretty-layouts.engine]
              [pretty-layouts.footer.attributes :as footer.attributes]
              [pretty-layouts.footer.prototypes :as footer.prototypes]
              [pretty-layouts.methods.api       :as pretty-layouts.methods]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (footer.attributes/outer-attributes id props)
        [:div (footer.attributes/inner-attributes id props)
              [:div (footer.attributes/content-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [footer id props])}))

(defn view
  ; @description
  ; Footer element for layouts.
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-layouts/footer.png)
  ; [footer {:content    "My footer"
  ;          :fill-color :highlight
  ;          :indent     {:all :s}}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [footer "My content"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-layouts.methods/apply-layout-shorthand-key  id props SHORTHAND-KEY)
             props (pretty-layouts.methods/apply-layout-preset         id props)
             props (pretty-layouts.methods/import-layout-dynamic-props id props)
             props (pretty-layouts.methods/import-layout-state-events  id props)
             props (pretty-layouts.methods/import-layout-state         id props)
             props (footer.prototypes/props-prototype                  id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
