
(ns pretty-elements.anchor.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.anchor.attributes :as anchor.attributes]
              [pretty-elements.anchor.prototypes :as anchor.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-elements.methods.api       :as pretty-elements.methods]
              [pretty-models.api                 :as pretty-models]
              [pretty-subitems.api               :as pretty-subitems]
              [reagent.core                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- anchor
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  ...}
  [id {:keys [content] :as props}]
  [:div (anchor.attributes/outer-attributes id props)
        [:a (anchor.attributes/inner-attributes id props)
            [:div (anchor.attributes/body-attributes id props) content]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   id props %))
                         :reagent-render         (fn [_ props] [anchor id props])}))

(defn view
  ; @description
  ; Inline anchor element.
  ;
  ; @links Implemented models
  ; [Click control model](pretty-core/cljs/pretty-models/api.html#click-control-model)
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/anchor.png)
  ; [anchor {:border-color  :highlight
  ;          :border-radius {:all :l}
  ;          :fill-color    :highlight
  ;          :indent        {:horizontal :s :vertical :xxs}
  ;          :content       "My anchor"
  ;          :outer-width   :5xl}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [anchor "My anchor"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-key    id props SHORTHAND-KEY)
             props (pretty-elements.methods/apply-element-presets          id props)
             props (pretty-elements.methods/import-element-dynamic-props   id props)
             props (pretty-elements.methods/import-element-focus-reference id props)
             props (pretty-elements.methods/import-element-state-events    id props)
             props (pretty-elements.methods/import-element-state           id props)
             props (pretty-elements.methods/import-element-timeout-events  id props)
             props (pretty-elements.methods/import-element-timeout         id props)
             props (anchor.prototypes/props-prototype                      id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
