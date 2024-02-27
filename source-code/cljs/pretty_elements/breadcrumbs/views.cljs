
(ns pretty-elements.breadcrumbs.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-elements.breadcrumbs.attributes :as breadcrumbs.attributes]
              [pretty-elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [pretty-elements.crumb.views            :as crumb.views]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)(opt)
  ;  ...}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  [:div (breadcrumbs.attributes/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
        [:div (breadcrumbs.attributes/breadcrumbs-inner-attributes breadcrumbs-id breadcrumbs-props)
              (letfn [(f0 [crumb-props] [crumb.views/view crumb-props])]
                     (hiccup/put-with [:<>] crumbs f0))]])

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
  ; @links Implemented elements
  ; [Crumb](pretty-ui/cljs/pretty-elements/api.html#crumb)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#State-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/breadcrumbs.png)
  ; [breadcrumbs {:crumb-default {:bullet {:border-radius {:all :xxs} :fill-color :primary}}
  ;               :crumbs        [{:label {:content "My crumb #1"} :href-uri "/my-uri-1"}
  ;                               {:label {:content "My crumb #2"} :href-uri "/my-uri-2"}
  ;                               {:label {:content "My crumb #3" :text-color :muted}}]
  ;               :gap           :xs}
  ([breadcrumbs-props]
   [view (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   ; @note (tutorials#parameterizing)
   (fn [_ breadcrumbs-props]
       (let [breadcrumbs-props (pretty-presets.engine/apply-preset                 breadcrumbs-id breadcrumbs-props)
             breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-id breadcrumbs-props)]
            [view-lifecycles breadcrumbs-id breadcrumbs-props]))))
