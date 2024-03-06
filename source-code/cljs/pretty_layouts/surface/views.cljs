
(ns pretty-layouts.surface.views
    (:require [fruits.random.api                 :as random]
              [pretty-layouts.body.views         :as body.views]
              [pretty-layouts.engine.api         :as pretty-layouts.engine]
              [pretty-layouts.footer.views       :as footer.views]
              [pretty-layouts.header.views       :as header.views]
              [pretty-layouts.surface.attributes :as surface.attributes]
              [pretty-layouts.surface.prototypes :as surface.prototypes]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [pretty-subitems.api               :as pretty-subitems]
              [reagent.core                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:body (map)(opt)
  ;  :footer (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [id {:keys [body footer header] :as props}]
  [:div (surface.attributes/outer-attributes id props)
        [:div (surface.attributes/inner-attributes id props)
              (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
              (if body   [:div (surface.attributes/content-attributes id props)
                               (if header [pretty-layouts.engine/layout-overlap-sensor (pretty-subitems/subitem-id id :header-sensor) header])
                               (if body   [body.views/view                             (pretty-subitems/subitem-id id :body)          body])
                               (if footer [pretty-layouts.engine/layout-overlap-sensor (pretty-subitems/subitem-id id :footer-sensor) footer])])
              (if footer [footer.views/view (pretty-subitems/subitem-id id :footer) footer])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount id props))
                         :reagent-render         (fn [_ props] [surface id props])}))

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
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented layouts.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/surface.png)
  ; [surface {:body       {:content "My body" :fill-color :highlight :outer-height :parent}
  ;           :header     {:content "My header"}
  ;           :footer     {:content "My footer"}
  ;           :fill-color :default}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset id props)
             props (surface.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
