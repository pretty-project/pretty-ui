
(ns pretty-layouts.body.views
    (:require [fruits.random.api :as random]
              [pretty-layouts.engine.api :as pretty-layouts.engine]
              [pretty-layouts.body.attributes :as body.attributes]
              [pretty-layouts.body.prototypes :as body.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @ignore
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [body-id {:keys [content] :as body-props}]
  [:div (body.attributes/body-attributes body-id body-props)
        [:div (body.attributes/body-inner-attributes body-id body-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  [body-id body-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    body-id body-props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount body-id body-props))
                         :reagent-render         (fn [_ body-props] [body body-id body-props])}))

(defn view
  ; @description
  ; Body element for layouts.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
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
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) body-id
  ; @param (map) body-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/body.png)
  ; [body {:content    "My body"
  ;        :fill-color :highlight
  ;        :indent     {:all :s}}]
  ([body-props]
   [view (random/generate-keyword) body-props])

  ([body-id body-props]
   ; @note (tutorials#parameterizing)
   (fn [_ body-props]
       (let [body-props (pretty-presets.engine/apply-preset       body-id body-props)
             body-props (body.prototypes/body-props-prototype body-id body-props)]
            [view-lifecycles body-id body-props]))))
