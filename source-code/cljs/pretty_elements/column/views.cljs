
(ns pretty-elements.column.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.column.attributes :as column.attributes]
              [pretty-elements.column.prototypes :as column.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-elements.methods.api       :as pretty-elements.methods]
              [reagent.core                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- column
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (column.attributes/outer-attributes id props)
        [:div (column.attributes/inner-attributes id props)
              [:div (column.attributes/body-attributes id props) content]]])

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
                         :reagent-render         (fn [_ props] [column id props])}))

(defn view
  ; @description
  ; Vertical flex container element.
  ;
  ; @links Implemented models
  ; [Flex content model](pretty-core/cljs/pretty-models/api.html#flex-content-model)
  ; [Plain container model](pretty-core/cljs/pretty-models/api.html#plain-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/column.png)
  ; [column {:border-color     :primary
  ;          :border-radius    {:all :m}
  ;          :border-width     :xs
  ;          :content          [:<> [:div "My row #1"]
  ;                                 [:div "My row #2"]
  ;                                 [:div "My row #3"]]
  ;          :fill-color       :highlight
  ;          :gap              :xs
  ;          :horizontal-align :center
  ;          :vertical-align   :center
  ;          :outer-height     :s
  ;          :outer-width      :5xl}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [column "My column"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-key  id props SHORTHAND-KEY)
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (column.prototypes/props-prototype                    id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
