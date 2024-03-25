
(ns pretty-elements.row.views
    (:require [fruits.random.api              :as random]
              [pretty-elements.engine.api     :as pretty-elements.engine]
              [pretty-elements.methods.api    :as pretty-elements.methods]
              [pretty-elements.row.attributes :as row.attributes]
              [pretty-elements.row.prototypes :as row.prototypes]
              [reagent.core                   :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (row.attributes/outer-attributes id props)
        [:div (row.attributes/inner-attributes id props)
              [:div (row.attributes/body-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [row id props])}))

(defn view
  ; @description
  ; Horizontal flex container element.
  ;
  ; @links Implemented models
  ; [Flex content model](pretty-core/cljs/pretty-models/api.html#flex-content-model)
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/row.png)
  ; [row {:border-color     :primary
  ;       :border-radius    {:all :m}
  ;       :border-width     :xs
  ;       :content          [:<> [:div "My column #1"]
  ;                              [:div "My column #2"]
  ;                              [:div "My column #3"]]
  ;       :fill-color       :highlight
  ;       :gap              :xs
  ;       :horizontal-align :center
  ;       :vertical-align   :center
  ;       :outer-height     :s
  ;       :outer-width      :5xl}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [row "My row"]
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
             props (row.prototypes/props-prototype                       id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
