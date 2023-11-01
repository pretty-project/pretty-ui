
(ns pretty-layouts.plain-popup.views
    (:require [metamorphic-content.api               :as metamorphic-content]
              [pretty-layouts.plain-popup.attributes :as plain-popup.attributes]
              [pretty-layouts.plain-popup.prototypes :as plain-popup.prototypes]
              [random.api                            :as random]
              [re-frame.api                          :as r]
              [reagent.api                           :as reagent]
              [scroll-lock.api                       :as scroll-lock]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-popup-structure
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:content (metamorphic-content)}
  [popup-id {:keys [content cover-color] :as popup-props}]
  [:div (plain-popup.attributes/popup-attributes popup-id popup-props)
        (if cover-color [:div (plain-popup.attributes/popup-cover-attributes popup-id popup-props)])
        (if content     [:div {:class :pl-plain-popup--content}
                              [metamorphic-content/compose content]])])

(defn- plain-popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  [popup-id {:keys [lock-scroll? on-mount on-unmount] :as popup-props}]
  ; XXX#0106 (README.md#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (if lock-scroll? (scroll-lock/add-scroll-prohibition! popup-id))
                                                         (if on-mount     (r/dispatch on-mount)))
                       :component-will-unmount (fn [_ _] (if lock-scroll? (scroll-lock/remove-scroll-prohibition! popup-id))
                                                         (if on-unmount   (r/dispatch on-unmount)))
                       :reagent-render         (fn [_ popup-props] [plain-popup-structure popup-id popup-props])}))

(defn layout
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; {:content (metamorphic-content)(opt)
  ;  :cover-color (keyword or string)(opt)
  ;  :lock-scroll? (boolean)(opt)
  ;  :on-cover (Re-Frame metamorphic-event)(opt)
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [plain-popup {...}]
  ;
  ; @usage
  ; [plain-popup :my-plain-popup {...}]
  ([popup-props]
   [layout (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   (fn [_ popup-props] ; XXX#0106 (README.md#parametering)
       (let [popup-props (plain-popup.prototypes/popup-props-prototype popup-props)]
            [plain-popup popup-id popup-props]))))
