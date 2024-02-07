
(ns pretty-layouts.box-popup.views
    (:require [fruits.random.api                   :as random]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-layouts.box-popup.attributes :as box-popup.attributes]
              [pretty-layouts.box-popup.prototypes :as box-popup.prototypes]
              [pretty-layouts.engine.api           :as pretty-layouts.engine]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [re-frame.api                        :as r]
              [reagent.api                         :as reagent]
              [scroll-lock.api                     :as scroll-lock]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- box-popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  [popup-id {:keys [content cover-color] :as popup-props}]
  [:div (box-popup.attributes/popup-attributes popup-id popup-props)
        (if cover-color [:div (box-popup.attributes/popup-cover-attributes popup-id popup-props)])
        [:div (box-popup.attributes/popup-wrapper-attributes popup-id popup-props)
              [:div (box-popup.attributes/popup-structure-attributes popup-id popup-props)
                    [:div {:class :pl-box-popup--content}
                          [metamorphic-content/compose content]]]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  [popup-id {:keys [lock-scroll? on-mount on-unmount] :as popup-props}]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (if lock-scroll? (scroll-lock/add-scroll-prohibition! popup-id))
                                                         (if on-mount     (r/dispatch on-mount)))
                       :component-will-unmount (fn [_ _] (if lock-scroll? (scroll-lock/remove-scroll-prohibition! popup-id))
                                                         (if on-unmount   (r/dispatch on-unmount)))
                       :reagent-render         (fn [_ popup-props] [box-popup popup-id popup-props])}))

(defn view
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;   Default: :xxs
  ;  :content (metamorphic-content)
  ;  :cover-color (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
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
  ; [box-popup {...}]
  ;
  ; @usage
  ; [box-popup :my-box-popup {...}]
  ([popup-props]
   [view (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   ; @note (tutorials#parameterizing)
   (fn [_ popup-props]
       (let [popup-props (pretty-presets.engine/apply-preset         popup-id popup-props)
             popup-props (box-popup.prototypes/popup-props-prototype popup-id popup-props)]
            [view-lifecycles popup-id popup-props]))))
