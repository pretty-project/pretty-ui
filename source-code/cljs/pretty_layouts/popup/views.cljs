
(ns pretty-layouts.popup.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [metamorphic-content.api                :as metamorphic-content]
              [pretty-layouts.engine.api              :as pretty-layouts.engine]
              [pretty-layouts.popup.attributes :as popup.attributes]
              [pretty-layouts.popup.prototypes :as popup.prototypes]
              [pretty-layouts.popup.utils      :as popup.utils]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [re-frame.api                           :as r]
              [reagent.core :as reagent]
              [scroll-lock.api                        :as scroll-lock]
              [pretty-accessories.api :as pretty-accessories]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-footer
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer] :as popup-props}]
  [:div (popup.attributes/popup-footer-attributes popup-id popup-props)
        [:div {:class :pl-popup--footer-content}
              [metamorphic-content/compose footer]]])

(defn- popup-footer-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)(opt)}
  [popup-id {:keys [footer] :as popup-props}]
  ; @note (tutorials#parameterizing)
  (if footer (reagent/create-class {:component-did-mount    (fn [_ _] (popup.utils/footer-did-mount-f    popup-id))
                                    :component-will-unmount (fn [_ _] (popup.utils/footer-will-unmount-f popup-id))
                                    :reagent-render         (fn [_ popup-props] [popup-footer popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-header
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)}
  [popup-id {:keys [header] :as popup-props}]
  [:div (popup.attributes/popup-header-attributes popup-id popup-props)
        [:div {:class :pl-popup--header-content}
              [metamorphic-content/compose header]]])

(defn- popup-header-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)(opt)}
  [popup-id {:keys [header] :as popup-props}]
  ; @note (tutorials#parameterizing)
  (if header (reagent/create-class {:component-did-mount    (fn [_ _] (popup.utils/header-did-mount-f    popup-id))
                                    :component-will-unmount (fn [_ _] (popup.utils/header-will-unmount-f popup-id))
                                    :reagent-render         (fn [_ popup-props] [popup-header popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-body
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:body (metamorphic-content)(opt)
  ;  :footer (metamorphic-content)(opt)
  ;  :header (metamorphic-content)(opt)}
  [popup-id {:keys [body footer header]}]
  ; The header sensor must be placed at the beginning of the body content ...
  ; The footer sensor must be placed at the end of the body content ...
  ; ... because the sensors have to be scrolled with the body content.
  ;
  ; The body content element must be stretched vertically to its wrapper
  ; (to the body element) to provide the center alignment ability for the content.
  [:div {:class :pl-popup--body :data-scroll-axis :y}
        [:div {:class :pl-popup--body-content}
              (if header [:div {:id (hiccup/value popup-id "header-sensor")}])
              (if body   [metamorphic-content/compose body])
              (if footer [:div {:id (hiccup/value popup-id "footer-sensor")}])]])

(defn- popup-body-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:body (metamorphic-content)(opt)}
  [popup-id {:keys [body] :as popup-props}]
  ; @note (tutorials#parameterizing)
  (if body (reagent/create-class {:reagent-render (fn [_ popup-props] [popup-body popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:cover (map)(opt)
  ;  ...}
  [popup-id {:keys [cover] :as popup-props}]
  [:div (popup.attributes/popup-attributes popup-id popup-props)
        (if cover [pretty-accessories/cover popup-id cover])
        [:div (popup.attributes/popup-wrapper-attributes popup-id popup-props)
              [:div (popup.attributes/popup-structure-attributes popup-id popup-props)
                    [:div {:class :pl-popup--hack}
                          [popup-body-lifecycles   popup-id popup-props]
                          [popup-header-lifecycles popup-id popup-props]]
                    [popup-footer-lifecycles popup-id popup-props]]]])

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
  ; @links Implemented accessories
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Structure properties](pretty-core/cljs/pretty-properties/api.html#structure-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; {:body (metamorphic-content)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;   Default: :xxs
  ;  :cover-color (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :footer (metamorphic-content)(opt)
  ;  :header (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :lock-scroll? (boolean)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-cover (Re-Frame metamorphic-event)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;   :both, :horizontal, :vertical
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [popup {...}]
  ;
  ; @usage
  ; [popup :my-popup {...}]
  ([popup-props]
   [view (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   ; @note (tutorials#parameterizing)
   (fn [_ popup-props]
       (let [popup-props (pretty-presets.engine/apply-preset     popup-id popup-props)
             popup-props (popup.prototypes/popup-props-prototype popup-id popup-props)]
            [view-lifecycles popup-id popup-props]))))
