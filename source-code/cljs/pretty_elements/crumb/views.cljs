
(ns pretty-elements.crumb.views
    (:require [fruits.random.api                :as random]
              [pretty-accessories.api           :as pretty-accessories]
              [pretty-elements.crumb.attributes :as crumb.attributes]
              [pretty-elements.crumb.prototypes :as crumb.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-models.api                :as pretty-models]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api              :as pretty-subitems]
              [reagent.core                     :as reagent]))

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
        [(pretty-models/clickable-auto-tag  id props)
         (crumb.attributes/inner-attributes id props)
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
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
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
       (let [props (pretty-models/use-subitem-longhand id props :label :content)
             props (pretty-presets.engine/apply-preset id props)
             props (crumb.prototypes/props-prototype   id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
