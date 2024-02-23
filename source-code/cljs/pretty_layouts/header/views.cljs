
(ns pretty-layouts.header.views
    (:require [fruits.random.api :as random]
              [pretty-layouts.engine.api :as pretty-layouts.engine]
              [pretty-layouts.header.attributes :as header.attributes]
              [pretty-layouts.header.prototypes :as header.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [header-id {:keys [content] :as header-props}]
  [:div (header.attributes/header-attributes header-id header-props)
        [:div (header.attributes/header-body-attributes header-id header-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    header-id header-props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount header-id header-props))
                         :reagent-render         (fn [_ header-props] [header header-id header-props])}))

(defn view
  ; @description
  ; Header element for layouts.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/header.png)
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parameterizing)
   (fn [_ header-props]
       (let [header-props (pretty-presets.engine/apply-preset       header-id header-props)
             header-props (header.prototypes/header-props-prototype header-id header-props)]
            [view-lifecycles header-id header-props]))))
