
(ns pretty-elements.breadcrumbs.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-elements.breadcrumbs.attributes :as breadcrumbs.attributes]
              [pretty-elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [pretty-elements.crumb.views            :as crumb.views]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-accessories.api :as pretty-accessories]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-bullet
  ; @ignore
  ;
  ; @param (integer) bullet-dex
  ; @param (map) bullet-props
  [bullet-dex bullet-props]
  (if (-> bullet-dex pos?)
      (let [bullet-props (breadcrumbs.prototypes/bullet-props-prototype bullet-dex bullet-props)]
           [pretty-accessories/bullet bullet-props])))

(defn breadcrumbs-crumb
  ; @ignore
  ;
  ; @param (integer) crumb-dex
  ; @param (map) crumb-props
  [crumb-dex crumb-props]
  (let [crumb-props (breadcrumbs.prototypes/crumb-props-prototype crumb-dex crumb-props)]
       [crumb.views/view crumb-props]))

(defn breadcrumbs
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:bullet (map)(opt)
  ;  :crumbs (maps in vector)(opt)
  ;  ...}
  [breadcrumbs-id {:keys [bullet crumbs] :as breadcrumbs-props}]
  [:div (breadcrumbs.attributes/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
        [:div (breadcrumbs.attributes/breadcrumbs-body-attributes breadcrumbs-id breadcrumbs-props)
              (letfn [(f0 [crumb-dex crumb-props]
                          [:<> [breadcrumbs-bullet crumb-dex bullet]
                               [breadcrumbs-crumb  crumb-dex crumb-props]])]
                     (hiccup/put-with-indexed [:<>] crumbs f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    breadcrumbs-id breadcrumbs-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount breadcrumbs-id breadcrumbs-props))
                         :reagent-render         (fn [_ breadcrumbs-props] [breadcrumbs breadcrumbs-id breadcrumbs-props])}))

(defn view
  ; @description
  ; Breadcrumb style menu element.
  ;
  ; @links Implemented accessories
  ; [Bullet](pretty-ui/cljs/pretty-accessories/api.html#bullet)
  ;
  ; @links Implemented elements
  ; [Crumb](pretty-ui/cljs/pretty-elements/api.html#crumb)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#State-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (breadcrumbs.png)
  ; [breadcrumbs {:bullet        {:border-radius {:all :xs}}
  ;               :crumb-default {:font-size :xs}
  ;               :crumbs [{:label "My crumb #1" :href-uri "/my-uri-1"}
  ;                        {:label "My crumb #2" :href-uri "/my-uri-2"}
  ;                        {:label "My crumb #3"}]}
  ([breadcrumbs-props]
   [view (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   ; @note (tutorials#parameterizing)
   (fn [_ breadcrumbs-props]
       (let [breadcrumbs-props (pretty-presets.engine/apply-preset                            breadcrumbs-id breadcrumbs-props)
             breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype            breadcrumbs-id breadcrumbs-props)
             breadcrumbs-props (pretty-elements.engine/element-subitem-group<-subitem-default breadcrumbs-id breadcrumbs-props :crumbs :crumb-default)
             breadcrumbs-props (pretty-elements.engine/element-subitem-group<-disabled-state  breadcrumbs-id breadcrumbs-props :crumbs)
             breadcrumbs-props (pretty-elements.engine/leave-element-disabled-state           breadcrumbs-id breadcrumbs-props :crumbs)]
            [view-lifecycles breadcrumbs-id breadcrumbs-props]))))
