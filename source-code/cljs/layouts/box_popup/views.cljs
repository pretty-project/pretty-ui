
(ns layouts.box-popup.views
    (:require [layouts.box-popup.attributes :as box-popup.attributes]
              [layouts.box-popup.prototypes :as box-popup.prototypes]
              [random.api                   :as random]
              [re-frame.api                 :as r]
              [reagent.api                  :as reagent]
              [scroll-lock.api              :as scroll-lock]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- box-popup-structure
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
                    [:div {:class :l-box-popup--content}
                          [x.components/content popup-id content]]]]])

(defn- box-popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  [popup-id {:keys [lock-scroll? on-mount on-unmount] :as popup-props}]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (if lock-scroll? (scroll-lock/add-scroll-prohibition! popup-id))
                                                         (if on-mount     (r/dispatch on-mount)))
                       :component-will-unmount (fn [_ _] (if lock-scroll? (scroll-lock/remove-scroll-prohibition! popup-id))
                                                         (if on-unmount   (r/dispatch on-unmount)))
                       :reagent-render         (fn [_ _] [box-popup-structure popup-id popup-props])}))

(defn layout
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; {:border-color (keyword)(opt)
  ;   :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxs
  ;  :content (metamorphic-content)
  ;  :cover-color (keyword or string)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :indent (map)(opt)
  ;  :lock-scroll? (boolean)(opt)
  ;  :max-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :max-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-cover (Re-Frame metamorphic-event)(opt)
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;   :both, :horizontal, :vertical
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [box-popup {...}]
  ;
  ; @usage
  ; [box-popup :my-box-popup {...}]
  ([popup-props]
   [layout (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   (let [popup-props (box-popup.prototypes/popup-props-prototype popup-props)]
        [box-popup popup-id popup-props])))
