
(ns pretty-elements.ghost.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.ghost.attributes :as ghost.attributes]
              [pretty-elements.ghost.prototypes :as ghost.prototypes]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  [:div (ghost.attributes/ghost-attributes ghost-id ghost-props)
        [:div (ghost.attributes/ghost-body-attributes ghost-id ghost-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    ghost-id ghost-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount ghost-id ghost-props))
                         :reagent-render         (fn [_ ghost-props] [ghost ghost-id ghost-props])}))

(defn view
  ; @description
  ; Placeholder element for loading contents.
  ;
  ; @links Implemented properties
  ; [Animation properties color](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background properties color](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) ghost-id
  ; @param (map) ghost-props
  ; Check out the implemented properties below.
  ;
  ; @usage (ghost.png)
  ; ...
  ([ghost-props]
   [view (random/generate-keyword) ghost-props])

  ([ghost-id ghost-props]
   ; @note (tutorials#parameterizing)
   (fn [_ ghost-props]
       (let [ghost-props (pretty-presets.engine/apply-preset     ghost-id ghost-props)
             ghost-props (ghost.prototypes/ghost-props-prototype ghost-id ghost-props)]
            [view-lifecycles ghost-id ghost-props]))))
