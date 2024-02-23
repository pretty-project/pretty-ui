
(ns pretty-layouts.footer.views
    (:require [fruits.random.api :as random]
              [pretty-layouts.engine.api :as pretty-layouts.engine]
              [pretty-layouts.footer.attributes :as footer.attributes]
              [pretty-layouts.footer.prototypes :as footer.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  ; @ignore
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [footer-id {:keys [content] :as footer-props}]
  [:div (footer.attributes/footer-attributes footer-id footer-props)
        [:div (footer.attributes/footer-body-attributes footer-id footer-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) footer-id
  ; @param (map) footer-props
  [footer-id footer-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    footer-id footer-props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount footer-id footer-props))
                         :reagent-render         (fn [_ footer-props] [footer footer-id footer-props])}))

(defn view
  ; @description
  ; Footer element for layouts.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) footer-id
  ; @param (map) footer-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/footer.png)
  ([footer-props]
   [view (random/generate-keyword) footer-props])

  ([footer-id footer-props]
   ; @note (tutorials#parameterizing)
   (fn [_ footer-props]
       (let [footer-props (pretty-presets.engine/apply-preset       footer-id footer-props)
             footer-props (footer.prototypes/footer-props-prototype footer-id footer-props)]
            [view-lifecycles footer-id footer-props]))))
