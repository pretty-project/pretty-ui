
(ns pretty-elements.crumb-group.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-elements.crumb-group.attributes :as crumb-group.attributes]
              [pretty-elements.crumb-group.prototypes :as crumb-group.prototypes]
              [pretty-elements.crumb.views            :as crumb.views]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:crumbs (maps in vector)(opt)
  ;  ...}
  [group-id {:keys [crumbs] :as group-props}]
  [:div (crumb-group.attributes/group-attributes group-id group-props)
        [:div (crumb-group.attributes/group-inner-attributes group-id group-props)
              (letfn [(f0 [crumb-props] [crumb.views/view crumb-props])]
                     (hiccup/put-with [:<>] crumbs f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    group-id group-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount group-id group-props))
                         :reagent-render         (fn [_ group-props] [crumb-group group-id group-props])}))

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
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/crumb-group.png)
  ; [crumb-group {:crumb-default {:bullet {:border-radius {:all :xxs} :fill-color :primary}}
  ;               :crumbs        [{:label {:content "My crumb #1"} :href-uri "/my-uri-1"}
  ;                               {:label {:content "My crumb #2"} :href-uri "/my-uri-2"}
  ;                               {:label {:content "My crumb #3" :text-color :muted}}]
  ;               :gap           :xs}
  ([group-props]
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset           group-id group-props)
             group-props (crumb-group.prototypes/group-props-prototype group-id group-props)]
            [view-lifecycles group-id group-props]))))
