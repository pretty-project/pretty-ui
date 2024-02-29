
(ns pretty-accessories.overlay.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.overlay.attributes :as overlay.attributes]
              [pretty-accessories.overlay.prototypes :as overlay.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- overlay
  ; @ignore
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  [overlay-id overlay-props]
  [:div (overlay.attributes/overlay-attributes overlay-id overlay-props)
        [:div (overlay.attributes/overlay-inner-attributes overlay-id overlay-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Overlay accessory for elements.
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) overlay-id
  ; @param (map) overlay-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/overlay.png)
  ; [overlay {:fill-color :invert}]
  ([overlay-props]
   [view (random/generate-keyword) overlay-props])

  ([overlay-id overlay-props]
   ; @note (tutorials#parameterizing)
   (fn [_ overlay-props]
       (let [overlay-props (pretty-presets.engine/apply-preset         overlay-id overlay-props)
             overlay-props (overlay.prototypes/overlay-props-prototype overlay-id overlay-props)]
            [overlay overlay-id overlay-props]))))
