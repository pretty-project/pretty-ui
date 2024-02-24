
(ns pretty-layouts.popup.views
    (:require [fruits.random.api                      :as random]
              [pretty-layouts.engine.api              :as pretty-layouts.engine]
              [pretty-layouts.popup.attributes :as popup.attributes]
              [pretty-layouts.popup.prototypes :as popup.prototypes]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-layouts.footer.views :as footer.views]
              [pretty-layouts.body.views :as body.views]
              [pretty-layouts.header.views :as header.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-header
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  (let [header-id    (pretty-layouts.engine/layout-id->subitem-id popup-id :header)
        header-props (popup.prototypes/header-props-prototype     popup-id popup-props)]
       [header.views/view header-id header-props]))

(defn- popup-body
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; @param (map) popup-props
  ; {:footer (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [popup-id {:keys [footer header] :as popup-props}]
  (let [body-id    (pretty-layouts.engine/layout-id->subitem-id popup-id :body)
        body-props (popup.prototypes/body-props-prototype       popup-id popup-props)]
       [:div (popup.attributes/popup-content-attributes popup-id popup-props)
             (when header  [pretty-layouts.engine/layout-header-sensor popup-id popup-props])
             (when :always [body.views/view                            body-id  body-props])
             (when footer  [pretty-layouts.engine/layout-footer-sensor popup-id popup-props])]))

(defn- popup-footer
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  (let [footer-id    (pretty-layouts.engine/layout-id->subitem-id popup-id :footer)
        footer-props (popup.prototypes/footer-props-prototype     popup-id popup-props)]
       [footer.views/view footer-id footer-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:body (map)(opt)
  ;  :footer (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [popup-id {:keys [body footer header] :as popup-props}]
  [:div (popup.attributes/popup-attributes popup-id popup-props)
        [:div (popup.attributes/popup-inner-attributes popup-id popup-props)
              (if header [popup-header popup-id popup-props])
              (if body   [popup-body   popup-id popup-props])
              (if footer [popup-footer popup-id popup-props])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    popup-id popup-props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount popup-id popup-props))
                         :reagent-render         (fn [_ popup-props] [popup popup-id popup-props])}))

(defn view
  ; @description
  ; Popup style layout.
  ;
  ; @links Implemented layouts
  ; [Body](pretty-ui/cljs/pretty-layouts/api.html#body)
  ; [Footer](pretty-ui/cljs/pretty-layouts/api.html#footer)
  ; [Header](pretty-ui/cljs/pretty-layouts/api.html#header)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Fullscreen properties](pretty-core/cljs/pretty-properties/api.html#fullscreen-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Overlay properties](pretty-core/cljs/pretty-properties/api.html#overlay-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Structure properties](pretty-core/cljs/pretty-properties/api.html#structure-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; Check out the implemented layouts.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/popup.png)
  ; [popup {:body          {:content "My body" :fill-color :highlight}
  ;         :header        {:content "My header"}
  ;         :footer        {:content "My footer"}
  ;         :border-radius {:all :m}
  ;         :fill-color    :default
  ;         :inner-height  :xxs
  ;         :inner-width   :xxs
  ;         :outer-height  :parent
  ;         :outer-width   :parent
  ;         :overlay-color :invert}]
  ([popup-props]
   [view (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   ; @note (tutorials#parameterizing)
   (fn [_ popup-props]
       (let [popup-props (pretty-presets.engine/apply-preset     popup-id popup-props)
             popup-props (popup.prototypes/popup-props-prototype popup-id popup-props)]
            [view-lifecycles popup-id popup-props]))))
