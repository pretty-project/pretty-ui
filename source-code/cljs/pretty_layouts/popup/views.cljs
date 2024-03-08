
(ns pretty-layouts.popup.views
    (:require [fruits.random.api               :as random]
              [pretty-accessories.api          :as pretty-accessories]
              [pretty-layouts.body.views       :as body.views]
              [pretty-layouts.engine.api       :as pretty-layouts.engine]
              [pretty-layouts.footer.views     :as footer.views]
              [pretty-layouts.header.views     :as header.views]
              [pretty-layouts.popup.attributes :as popup.attributes]
              [pretty-layouts.popup.prototypes :as popup.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:body (map)(opt)
  ;  :footer (map)(opt)
  ;  :header (map)(opt)
  ;  :overlay (map)(opt)
  ;  ...}
  [id {:keys [body footer header overlay] :as props}]
  [:div (popup.attributes/outer-attributes id props)
        (if overlay [pretty-accessories/overlay (pretty-subitems/subitem-id id :overlay) overlay])
        [:div (popup.attributes/inner-attributes id props)
              (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
              (if body   [:div (popup.attributes/content-attributes id props)
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
                         :reagent-render         (fn [_ props] [popup id props])}))

(defn view
  ; @description
  ; Popup style layout.
  ;
  ; @links Implemented accessories
  ; [Overlay](pretty-ui/cljs/pretty-accessories/api.html#overlay)
  ;
  ; @links Implemented layouts
  ; [Body](pretty-ui/cljs/pretty-layouts/api.html#body)
  ; [Footer](pretty-ui/cljs/pretty-layouts/api.html#footer)
  ; [Header](pretty-ui/cljs/pretty-layouts/api.html#header)
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Fullscreen properties](pretty-core/cljs/pretty-properties/api.html#fullscreen-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Keypress control properties](pretty-core/cljs/pretty-properties/api.html#keypress-control-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented layouts.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/popup.png)
  ; [popup {:body          {:content "My body" :fill-color :highlight :outer-height :parent}
  ;         :header        {:content "My header"}
  ;         :footer        {:content "My footer"}
  ;         :overlay       {:fill-color :invert}
  ;         :border-radius {:all :m}
  ;         :fill-color    :default
  ;         :inner-height  :xxs
  ;         :inner-width   :xxs
  ;         :outer-height  :parent
  ;         :outer-width   :parent}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [popup "My content"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset id props)
             props (popup.prototypes/props-prototype   id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
