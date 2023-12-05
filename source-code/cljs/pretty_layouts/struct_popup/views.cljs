
(ns pretty-layouts.struct-popup.views
    (:require [metamorphic-content.api                :as metamorphic-content]
              [pretty-layouts.struct-popup.attributes :as struct-popup.attributes]
              [pretty-layouts.struct-popup.prototypes :as struct-popup.prototypes]
              [pretty-layouts.struct-popup.utils      :as struct-popup.utils]
              [pretty-presets.api                        :as pretty-presets]
              [hiccup.api                             :as hiccup]
              [random.api                             :as random]
              [re-frame.api                           :as r]
              [reagent.api                            :as reagent]
              [scroll-lock.api                        :as scroll-lock]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer-structure
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer] :as popup-props}]
  [:div (struct-popup.attributes/popup-footer-attributes popup-id popup-props)
        [:div {:class :pl-struct-popup--footer-content}
              [metamorphic-content/compose footer]]])

(defn- footer
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)(opt)}
  [popup-id {:keys [footer] :as popup-props}]
  ; XXX#0106 (README.md#parametering)
  (if footer (reagent/lifecycles {:component-did-mount    (fn [_ _] (struct-popup.utils/footer-did-mount-f    popup-id))
                                  :component-will-unmount (fn [_ _] (struct-popup.utils/footer-will-unmount-f popup-id))
                                  :reagent-render         (fn [_ popup-props] [footer-structure popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-structure
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)}
  [popup-id {:keys [header] :as popup-props}]
  [:div (struct-popup.attributes/popup-header-attributes popup-id popup-props)
        [:div {:class :pl-struct-popup--header-content}
              [metamorphic-content/compose header]]])

(defn- header
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)(opt)}
  [popup-id {:keys [header] :as popup-props}]
  ; XXX#0106 (README.md#parametering)
  (if header (reagent/lifecycles {:component-did-mount    (fn [_ _] (struct-popup.utils/header-did-mount-f    popup-id))
                                  :component-will-unmount (fn [_ _] (struct-popup.utils/header-will-unmount-f popup-id))
                                  :reagent-render         (fn [_ popup-props] [header-structure popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:body (metamorphic-content)
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
              [metamorphic-content/compose body]
              (if footer [:div {:id (hiccup/value popup-id "footer-sensor")}])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- struct-popup-structure
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
                          [body   popup-id popup-props]
                          [header popup-id popup-props]]
                    [footer popup-id popup-props]]]])

(defn- struct-popup
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
                       :reagent-render         (fn [_ popup-props] [struct-popup-structure popup-id popup-props])}))

(defn layout
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ; {:body (metamorphic-content)
  ;  :border-color (keyword)(opt)
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
  ;  :cover-color (keyword or string)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :footer (metamorphic-content)(opt)
  ;  :header (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
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
  ;   Same as the :indent property.
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
   (fn [_ popup-props] ; XXX#0106 (README.md#parametering)
       (let [popup-props (pretty-presets/apply-preset                   popup-props)
             popup-props (struct-popup.prototypes/popup-props-prototype popup-props)]
            [struct-popup popup-id popup-props]))))
