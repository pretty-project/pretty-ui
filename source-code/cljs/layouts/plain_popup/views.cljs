
(ns layouts.plain-popup.views
    (:require [layouts.plain-popup.attributes :as plain-popup.attributes]
              [layouts.plain-popup.prototypes :as plain-popup.prototypes]
              [random.api                     :as random]
              [re-frame.api                   :as r]
              [reagent.api                    :as reagent]
              [x.components.api               :as x.components]))

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
        (if content     [:div {:class :l-plain-popup--content}
                              [x.components/content popup-id content]])])

(defn- plain-popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  [popup-id {:keys [on-mount on-unmount] :as popup-props}]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                       :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                       :reagent-render         (fn [_ _] [plain-popup-structure popup-id popup-props])}))

(defn layout
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; {:content (metamorphic-content)(opt)
  ;  :cover-color (keyword or string)(opt)
  ;  :on-cover (metamorphic-event)(opt)
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
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
   (let [popup-props (plain-popup.prototypes/popup-props-prototype popup-props)]
        [plain-popup popup-id popup-props])))
