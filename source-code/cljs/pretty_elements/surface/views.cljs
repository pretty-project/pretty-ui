
(ns pretty-elements.surface.views
    (:require [dynamic-props.api                  :as dynamic-props]
              [fruits.random.api                  :as random]
              [pretty-elements.engine.api         :as pretty-elements.engine]
              [pretty-elements.surface.attributes :as surface.attributes]
              [pretty-elements.surface.config     :as surface.config]
              [pretty-elements.surface.prototypes :as surface.prototypes]
              [pretty-presets.engine.api          :as pretty-presets.engine]
              [reagent.core :as reagent]
              [transition-controller.api          :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-controller
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [surface-id {:keys [content]}]
  ; The 'surface' element doesn't react on the changes of the ':content' property, unless it gets re-mounted,
  ; then the transition controller uses the given ':content' property as its initial content.
  [transition-controller/view surface-id {:initial-content     (if content [:div {:class :pe-surface--content} content])
                                          :transition-duration (-> surface.config/TRANSITION-DURATION)}])

(defn- surface
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:mounted? (boolean)(opt)
  ;  ...}
  [surface-id {:keys [mounted?] :as surface-props}]
  (if mounted? [:div (surface.attributes/surface-attributes surface-id surface-props)
                     [:div (surface.attributes/surface-body-attributes surface-id surface-props)
                           [surface-controller                         surface-id surface-props]]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    surface-id surface-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount surface-id surface-props))
                         :reagent-render         (fn [_ surface-props] [surface surface-id surface-props])}))

(defn view
  ; @description
  ; Surface element for displaying content with controller functions and optionally animated transitions.
  ;
  ; @links Implemented controls
  ; [surface-mounted?](pretty-ui/cljs/pretty-controls/api.html#surface-mounted_)
  ; [mount-surface!](pretty-ui/cljs/pretty-controls/api.html#mount-surface_)
  ; [unmount-surface!](pretty-ui/cljs/pretty-controls/api.html#unmount-surface_)
  ; [set-surface-content!](pretty-ui/cljs/pretty-controls/api.html#set-surface-content_)
  ; [swap-surface-content!](pretty-ui/cljs/pretty-controls/api.html#swap-surface-content_)
  ; [show-surface-content!](pretty-ui/cljs/pretty-controls/api.html#show-surface-content_)
  ; [hide-surface-content!](pretty-ui/cljs/pretty-controls/api.html#hide-surface-content_)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Position properties](pretty-core/cljs/pretty-properties/api.html#position-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ; Check out the implemented properties.
  ;
  ;  :mounted? (boolean)(opt)
  ;  + mounted? doesnt trigger on-mount-f and on-unmount-f
  ;
  ; @usage (surface.png)
  ; ...
  ([surface-props]
   [view (random/generate-keyword) surface-props])

  ([surface-id surface-props]
   ; @note (tutorials#parameterizing)
   (fn [_ surface-props]
       (let [surface-props (pretty-presets.engine/apply-preset         surface-id surface-props)
             surface-props (surface.prototypes/surface-props-prototype surface-id surface-props)
             surface-props (dynamic-props/import-props                 surface-id surface-props)]
            [view-lifecycles surface-id surface-props]))))
