
(ns pretty-elements.expandable.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-elements.expandable.attributes :as expandable.attributes]
              [pretty-elements.expandable.prototypes :as expandable.prototypes]
              [pretty-elements.methods.api           :as pretty-elements.methods]
              [pretty-subitems.api                   :as pretty-subitems]
              [reagent.core                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

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
              [:div (expandable.attributes/body-attributes id props) content]]])

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
  ; @links Implemented models
  ; [Flex content model](pretty-core/cljs/pretty-models/api.html#flex-content-model)
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented controls.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/expandable.png)
  ; [expandable {:border-radius {:all :m}
  ;              :content       "My expandable"
  ;              :fill-color    :highlight
  ;              :indent        {:all :s}}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [card "My expandable"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-key  id props SHORTHAND-KEY)
             props (pretty-elements.methods/apply-element-presets        id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (expandable.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
