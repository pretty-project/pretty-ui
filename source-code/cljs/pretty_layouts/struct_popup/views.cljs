
(ns pretty-layouts.struct-popup.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [metamorphic-content.api                :as metamorphic-content]
              [pretty-layouts.struct-popup.attributes :as struct-popup.attributes]
              [pretty-layouts.struct-popup.prototypes :as struct-popup.prototypes]
              [pretty-layouts.struct-popup.utils      :as struct-popup.utils]
              [pretty-presets.api                     :as pretty-presets]
              [re-frame.api                           :as r]
              [pretty-engine.api :as pretty-engine]
              [reagent.api :as reagent]
              [scroll-lock.api                        :as scroll-lock]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- struct-popup-footer
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer] :as popup-props}]
  [:div (struct-popup.attributes/popup-footer-attributes popup-id popup-props)
        [:div {:class :pl-struct-popup--footer-content}
              [metamorphic-content/compose footer]]])

(defn- struct-popup-footer-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)(opt)}
  [popup-id {:keys [footer] :as popup-props}]
  ; @note (tutorials#parametering)
  (if footer (reagent/lifecycles {:component-did-mount    (fn [_ _] (struct-popup.utils/footer-did-mount-f    popup-id))
                                  :component-will-unmount (fn [_ _] (struct-popup.utils/footer-will-unmount-f popup-id))
                                  :reagent-render         (fn [_ popup-props] [struct-popup-footer popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- struct-popup-header
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)}
  [popup-id {:keys [header] :as popup-props}]
  [:div (struct-popup.attributes/popup-header-attributes popup-id popup-props)
        [:div {:class :pl-struct-popup--header-content}
              [metamorphic-content/compose header]]])

(defn- struct-popup-header-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)(opt)}
  [popup-id {:keys [header] :as popup-props}]
  ; @note (tutorials#parametering)
  (if header (reagent/lifecycles {:component-did-mount    (fn [_ _] (struct-popup.utils/header-did-mount-f    popup-id))
                                  :component-will-unmount (fn [_ _] (struct-popup.utils/header-will-unmount-f popup-id))
                                  :reagent-render         (fn [_ popup-props] [struct-popup-header popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- struct-popup-body
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
  [:div {:class :pl-struct-popup--body :data-scroll-axis :y}
        [:div {:class :pl-struct-popup--body-content}
              (if header [:div {:id (hiccup/value popup-id "header-sensor")}])
              (if body   [metamorphic-content/compose body])
              (if footer [:div {:id (hiccup/value popup-id "footer-sensor")}])]])

(defn- struct-popup-body-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:body (metamorphic-content)(opt)}
  [popup-id {:keys [body] :as popup-props}]
  ; @note (tutorials#parametering)
  (if body (reagent/lifecycles {:reagent-render (fn [_ popup-props] [struct-popup-body popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- struct-popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:cover-color (keyword or string)(opt)}
  [popup-id {:keys [cover-color] :as popup-props}]
  [:div (struct-popup.attributes/popup-attributes popup-id popup-props)
        (if cover-color [:div (struct-popup.attributes/popup-cover-attributes popup-id popup-props)])
        [:div (struct-popup.attributes/popup-wrapper-attributes popup-id popup-props)
              [:div (struct-popup.attributes/popup-structure-attributes popup-id popup-props)
                    [:div {:class :pl-struct-popup--hack}
                          [struct-popup-body-lifecycles   popup-id popup-props]
                          [struct-popup-header-lifecycles popup-id popup-props]]
                    [struct-popup-footer-lifecycles popup-id popup-props]]]])

(defn- struct-popup-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  [popup-id {:keys [lock-scroll? on-mount on-unmount] :as popup-props}]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (if lock-scroll? (scroll-lock/add-scroll-prohibition! popup-id))
                                                         (if on-mount     (r/dispatch on-mount)))
                       :component-will-unmount (fn [_ _] (if lock-scroll? (scroll-lock/remove-scroll-prohibition! popup-id))
                                                         (if on-unmount   (r/dispatch on-unmount)))
                       :reagent-render         (fn [_ popup-props] [struct-popup popup-id popup-props])}))

(defn layout
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; {:body (metamorphic-content)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;   Default: :xxs
  ;  :cover-color (keyword or string)(opt)
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
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;   :both, :horizontal, :vertical
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [struct-popup {...}]
  ;
  ; @usage
  ; [struct-popup :my-struct-popup {...}]
  ([popup-props]
   [layout (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   ; @note (tutorials#parametering)
   (fn [_ popup-props]
       (let [popup-props (pretty-presets/apply-preset                   popup-props)
             popup-props (struct-popup.prototypes/popup-props-prototype popup-props)]
            [struct-popup-lifecycles popup-id popup-props]))))
