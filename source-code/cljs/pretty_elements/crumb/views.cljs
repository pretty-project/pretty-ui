
(ns pretty-elements.crumb.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.crumb.attributes :as crumb.attributes]
              [pretty-elements.crumb.prototypes :as crumb.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [pretty-accessories.api :as pretty-accessories]
              [pretty-models.api             :as pretty-models]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- crumb
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ; {:bullet (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [crumb-id {:keys [bullet label] :as crumb-props}]
  [:div (crumb.attributes/crumb-attributes crumb-id crumb-props)
        [(pretty-models/clickable-auto-tag        crumb-id crumb-props)
         (crumb.attributes/crumb-inner-attributes crumb-id crumb-props)
         (if bullet [pretty-accessories/bullet crumb-id bullet])
         (if label  [pretty-accessories/label  crumb-id label])]])

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
  ; @links Implemented accessories
  ; [Bullet](pretty-ui/cljs/pretty-accessories/api.html#bullet)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) crumb-id
  ; @param (map) crumb-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/crumb.png)
  ; [crumb {:bullet   {:border-radius {:all :xxs} :fill-color :primary}
  ;         :gap      :xs
  ;         :href-uri "/my-uri"
  ;         :label    {:content "My crumb"}}]
  ([crumb-props]
   [view (random/generate-keyword) crumb-props])

  ([crumb-id crumb-props]
   ; @note (tutorials#parameterizing)
   (fn [_ crumb-props]
       (let [crumb-props (pretty-presets.engine/apply-preset     crumb-id crumb-props)
             crumb-props (crumb.prototypes/crumb-props-prototype crumb-id crumb-props)]
            [view-lifecycles crumb-id crumb-props]))))
