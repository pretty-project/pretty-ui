
(ns pretty-elements.crumb-group.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-elements.crumb-group.attributes :as crumb-group.attributes]
              [pretty-elements.crumb-group.prototypes :as crumb-group.prototypes]
              [pretty-elements.crumb.views            :as crumb.views]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-elements.methods.api            :as pretty-elements.methods]
              [pretty-subitems.api                    :as pretty-subitems]
              [reagent.core                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:crumbs [crumb.views/SHORTHAND-MAP]})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-group
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:crumbs (maps in vector)(opt)
  ;  ...}
  [id {:keys [crumbs] :as props}]
  [:div (crumb-group.attributes/outer-attributes id props)
        [:div (crumb-group.attributes/inner-attributes id props)
              (letfn [(f0 [dex crumb] [crumb.views/view (pretty-subitems/subitem-id id dex) crumb])]
                     (hiccup/put-with-indexed [:<>] crumbs f0))]])

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
                         :reagent-render         (fn [_ props] [crumb-group id props])}))

(defn view
  ; @description
  ; Breadcrumb style menu element.
  ;
  ; @links Implemented elements
  ; [Crumb](pretty-ui/cljs/pretty-elements/api.html#crumb)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/crumb-group.png)
  ; [crumb-group {:crumb-default {:bullet {:border-radius {:all :xxs} :fill-color :primary}}
  ;               :crumbs        [{:label {:content "My crumb #1"} :href-uri "/my-uri-1"}
  ;                               {:label {:content "My crumb #2"} :href-uri "/my-uri-2"}
  ;                               {:label {:content "My crumb #3" :text-color :muted}}]
  ;               :gap           :xs}
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (crumb-group.prototypes/props-prototype               id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
