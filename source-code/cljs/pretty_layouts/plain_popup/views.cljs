
(ns pretty-layouts.plain-popup.views
    (:require [fruits.random.api                     :as random]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-layouts.engine.api                     :as pretty-layouts.engine]
              [pretty-layouts.plain-popup.attributes :as plain-popup.attributes]
              [pretty-layouts.plain-popup.prototypes :as plain-popup.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [re-frame.api                          :as r]
              [reagent.api                           :as reagent]
              [scroll-lock.api                       :as scroll-lock]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:content (metamorphic-content)(opt)}
  [popup-id {:keys [content cover-color] :as popup-props}]
  [:div (plain-popup.attributes/popup-attributes popup-id popup-props)
        (if cover-color [:div (plain-popup.attributes/popup-cover-attributes popup-id popup-props)])
        (if content     [:div {:class :pl-plain-popup--content}
                              [metamorphic-content/compose content]])])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-lifecycles
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
                       :reagent-render         (fn [_ popup-props] [plain-popup popup-id popup-props])}))

(defn layout
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; {:content (metamorphic-content)
  ;  :cover-color (keyword or string)(opt)
  ;  :lock-scroll? (boolean)(opt)
  ;  :on-cover (Re-Frame metamorphic-event)(opt)
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [plain-popup {...}]
  ;
  ; @usage
  ; [plain-popup :my-plain-popup {...}]
  ([popup-props]
   [layout (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   ; @note (tutorials#parametering)
   (fn [_ popup-props]
       (let [popup-props (pretty-presets.engine/apply-preset           popup-id popup-props)
             popup-props (plain-popup.prototypes/popup-props-prototype popup-id popup-props)]
            [layout-lifecycles popup-id popup-props]))))
