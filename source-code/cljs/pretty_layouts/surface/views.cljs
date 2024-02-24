
(ns pretty-layouts.surface.views
    (:require [fruits.random.api                 :as random]
              [pretty-layouts.engine.api         :as pretty-layouts.engine]
              [pretty-subitems.api         :as pretty-subitems]
              [pretty-layouts.surface.attributes :as surface.attributes]
              [pretty-layouts.surface.prototypes :as surface.prototypes]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-layouts.footer.views :as footer.views]
              [pretty-layouts.body.views :as body.views]
              [pretty-layouts.header.views :as header.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-header
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  (let [header-id    (pretty-subitems/subitem-id                surface-id :header)
        header-props (surface.prototypes/header-props-prototype surface-id surface-props)]
       [header.views/view header-id header-props]))

(defn- surface-body
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:footer (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [surface-id {:keys [footer header] :as surface-props}]
  (let [body-id    (pretty-subitems/subitem-id              surface-id :body)
        body-props (surface.prototypes/body-props-prototype surface-id surface-props)]
       [:div (surface.attributes/surface-content-attributes surface-id surface-props)
             (when header  [pretty-layouts.engine/layout-header-sensor surface-id surface-props])
             (when :always [body.views/view                            body-id    body-props])
             (when footer  [pretty-layouts.engine/layout-footer-sensor surface-id surface-props])]))

(defn- surface-footer
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  (let [footer-id    (pretty-subitems/subitem-id                surface-id :footer)
        footer-props (surface.prototypes/footer-props-prototype surface-id surface-props)]
       [footer.views/view footer-id footer-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:body (map)(opt)
  ;  :footer (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [surface-id {:keys [body footer header] :as surface-props}]
  [:div (surface.attributes/surface-attributes surface-id surface-props)
        [:div (surface.attributes/surface-inner-attributes surface-id surface-props)
              (if header [surface-header surface-id surface-props])
              (if body   [surface-body   surface-id surface-props])
              (if footer [surface-footer surface-id surface-props])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    surface-id surface-props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount surface-id surface-props))
                         :reagent-render         (fn [_ surface-props] [surface surface-id surface-props])}))

(defn view
  ; @description
  ; Surface style layout.
  ;
  ; @links Implemented layouts
  ; [Body](pretty-ui/cljs/pretty-layouts/api.html#body)
  ; [Footer](pretty-ui/cljs/pretty-layouts/api.html#footer)
  ; [Header](pretty-ui/cljs/pretty-layouts/api.html#header)
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Fullscreen properties](pretty-core/cljs/pretty-properties/api.html#fullscreen-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Structure properties](pretty-core/cljs/pretty-properties/api.html#structure-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ; Check out the implemented layouts.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/surface.png)
  ; [surface {:body       {:content "My body" :fill-color :highlight :outer-height :parent}
  ;           :header     {:content "My header"}
  ;           :footer     {:content "My footer"}
  ;           :fill-color :default}]
  ([surface-props]
   [view (random/generate-keyword) surface-props])

  ([surface-id surface-props]
   ; @note (tutorials#parameterizing)
   (fn [_ surface-props]
       (let [surface-props (pretty-presets.engine/apply-preset         surface-id surface-props)
             surface-props (surface.prototypes/surface-props-prototype surface-id surface-props)]
            [view-lifecycles surface-id surface-props]))))
