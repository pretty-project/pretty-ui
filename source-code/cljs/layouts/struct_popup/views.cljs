
(ns layouts.struct-popup.views
    (:require [layouts.struct-popup.attributes :as struct-popup.attributes]
              [layouts.struct-popup.helpers    :as struct-popup.helpers]
              [layouts.struct-popup.prototypes :as struct-popup.prototypes]
              [hiccup.api                      :as hiccup]
              [random.api                      :as random]
              [re-frame.api                    :as r]
              [reagent.api                     :as reagent]
              [x.components.api                :as x.components]))

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
        [:div {:class :l-struct-popup--footer-content}
              [x.components/content popup-id footer]]])

(defn- footer
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)(opt)}
  [popup-id {:keys [footer] :as popup-props}]
  (if footer (reagent/lifecycles {:component-did-mount    (fn [] (struct-popup.helpers/footer-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (struct-popup.helpers/footer-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [footer-structure popup-id popup-props])})))

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
        [:div {:class :l-struct-popup--header-content}
              [x.components/content popup-id header]]])

(defn- header
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)(opt)}
  [popup-id {:keys [header] :as popup-props}]
  (if header (reagent/lifecycles {:component-did-mount    (fn [] (struct-popup.helpers/header-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (struct-popup.helpers/header-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [header-structure popup-id popup-props])})))

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
  ; The header sensor has to be placed at the beginning of the body content ...
  ; The footer sensor has to be placed at the end of the body content ...
  ; ... because the sensors have to be scrolled with the body content.
  ;
  ; The body content element has to be stretched vertically to its wrapper
  ; (to the body element) to provide the center alignment ability for the content.
  [:div {:class :l-struct-popup--body :data-scroll-axis :y}
        [:div {:class :l-struct-popup--body-content}
              (if header [:div {:id (hiccup/value popup-id "header-sensor")}])
              [x.components/content popup-id body]
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
                    [:div {:class :l-struct-popup--hack}
                          [body   popup-id popup-props]
                          [header popup-id popup-props]]
                    [footer popup-id popup-props]]]])

(defn- struct-popup
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  [popup-id {:keys [on-mount on-unmount] :as popup-props}]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                       :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                       :reagent-render         (fn [_ _] [struct-popup-structure popup-id popup-props])}))

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
  ;  :max-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :max-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-cover (metamorphic-event)(opt)
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :outdent (map)(opt)
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
   (let [popup-props (struct-popup.prototypes/popup-props-prototype popup-props)]
        [struct-popup popup-id popup-props])))
