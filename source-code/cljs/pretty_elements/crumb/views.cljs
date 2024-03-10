
(ns pretty-elements.crumb.views
    (:require [fruits.random.api                :as random]
              [pretty-accessories.api           :as pretty-accessories]
              [pretty-elements.crumb.attributes :as crumb.attributes]
              [pretty-elements.crumb.prototypes :as crumb.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.methods.api      :as pretty-elements.methods]
              [pretty-models.api                :as pretty-models]
              [pretty-subitems.api              :as pretty-subitems]
              [reagent.core                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:label label.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- crumb
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:bullet (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [id {:keys [bullet label] :as props}]
  [:div (crumb.attributes/outer-attributes id props)
        [(pretty-models/click-control-auto-tag props) (crumb.attributes/inner-attributes id props)
         (if bullet [pretty-accessories/bullet (pretty-subitems/subitem-id id :bullet) bullet])
         (if label  [pretty-accessories/label  (pretty-subitems/subitem-id id :label)  label])]])

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
                         :reagent-render         (fn [_ props] [crumb id props])}))

(defn view
  ; @description
  ; Simplified button element for breadcrumb style menus.
  ;
  ; @links Implemented accessories
  ; [Bullet](pretty-ui/cljs/pretty-accessories/api.html#bullet)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented models
  ; [Click control model](pretty-core/cljs/pretty-models/api.html#click-control-model)
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/crumb.png)
  ; [crumb {:bullet   {:border-radius {:all :xxs} :fill-color :primary}
  ;         :gap      :xs
  ;         :href-uri "/my-uri"
  ;         :label    {:content "My crumb"}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map    id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset           id props)
             props (pretty-elements.methods/import-element-dynamic-props   id props)
             props (pretty-elements.methods/import-element-focus-reference id props)
             props (pretty-elements.methods/import-element-state-events    id props)
             props (pretty-elements.methods/import-element-state           id props)
             props (pretty-elements.methods/import-element-timeout-events  id props)
             props (pretty-elements.methods/import-element-timeout         id props)
             props (crumb.prototypes/props-prototype                       id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
