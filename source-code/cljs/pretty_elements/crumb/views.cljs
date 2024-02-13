
(ns pretty-elements.crumb.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.crumb.attributes :as crumb.attributes]
              [pretty-elements.crumb.prototypes :as crumb.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- crumb
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ; {:label (metamorphic-content)(opt)}
  [crumb-id {:keys [label] :as crumb-props}]
  [:div (crumb.attributes/crumb-attributes crumb-id crumb-props)
        [(pretty-elements.engine/clickable-auto-tag crumb-id crumb-props)
         (crumb.attributes/crumb-body-attributes    crumb-id crumb-props)
         [:div (crumb.attributes/crumb-label-attributes crumb-id crumb-props)
               (-> label)]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  [crumb-id crumb-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    crumb-id crumb-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount crumb-id crumb-props))
                         :reagent-render         (fn [_ crumb-props] [crumb crumb-id crumb-props])}))

(defn view
  ; @description
  ; Simplified button element for breadcrumb style menus.
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) crumb-id
  ; @param (map) crumb-props
  ; Check out the implemented properties below.
  ;
  ; @usage (crumb.png)
  ; ...
  ([crumb-props]
   [view (random/generate-keyword) crumb-props])

  ([crumb-id crumb-props]
   ; @note (tutorials#parameterizing)
   (fn [_ crumb-props]
       (let [crumb-props (pretty-presets.engine/apply-preset     crumb-id crumb-props)
             crumb-props (crumb.prototypes/crumb-props-prototype crumb-id crumb-props)]
            [view-lifecycles crumb-id crumb-props]))))
