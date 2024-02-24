
(ns pretty-accessories.bullet.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.bullet.attributes :as bullet.attributes]
              [pretty-accessories.bullet.prototypes :as bullet.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bullet
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  [bullet-id bullet-props]
  [:div (bullet.attributes/bullet-attributes bullet-id bullet-props)
        [:div (bullet.attributes/bullet-inner-attributes bullet-id bullet-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  [bullet-id bullet-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bullet-id bullet-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bullet-id bullet-props))
                         :reagent-render         (fn [_ bullet-props] [bullet bullet-id bullet-props])}))

(defn view
  ; @description
  ; Bullet accessory for elements.
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) bullet-id
  ; @param (map) bullet-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/bullet.png)
  ; [bullet {:border-radius {:all :xs}
  ;          :fill-color    :muted
  ;          :outer-height  :xs
  ;          :outer-width   :xs}]
  ([bullet-props]
   [view (random/generate-keyword) bullet-props])

  ([bullet-id bullet-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bullet-props]
       (let [bullet-props (pretty-presets.engine/apply-preset       bullet-id bullet-props)
             bullet-props (bullet.prototypes/bullet-props-prototype bullet-id bullet-props)]
            [view-lifecycles bullet-id bullet-props]))))
