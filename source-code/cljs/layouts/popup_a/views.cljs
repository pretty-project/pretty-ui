
(ns layouts.popup-a.views
    (:require [layouts.popup-a.helpers    :as popup-a.helpers]
              [layouts.popup-a.prototypes :as popup-a.prototypes]
              [layouts.popup-a.state      :as popup-a.state]
              [hiccup.api                 :as hiccup]
              [re-frame.api               :as r]
              [react.api                  :as react]
              [reagent.api                :as reagent]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer]}]
  [:div.l-popup-a--footer (if (popup-id @popup-a.state/FOOTER-SHADOW-VISIBLE?)
                              {:data-shadow-position :top :data-shadow-strength :s})
                          [:div.l-popup-a--footer-content [x.components/content popup-id footer]]])

(defn- footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:footer (metamorphic-content)}
  [popup-id {:keys [footer] :as popup-props}]
  (if footer (reagent/lifecycles {:component-did-mount    (fn [] (popup-a.helpers/footer-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (popup-a.helpers/footer-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [footer-structure popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)}
  [popup-id {:keys [header]}]
  [:div.l-popup-a--header (if (popup-id @popup-a.state/HEADER-SHADOW-VISIBLE?)
                              {:data-shadow-position :bottom :data-shadow-strength :s})
                          [:div.l-popup-a--header-content [x.components/content popup-id header]]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:header (metamorphic-content)(opt)}
  [popup-id {:keys [header] :as popup-props}]
  (if header (reagent/lifecycles {:component-did-mount    (fn [] (popup-a.helpers/header-did-mount-f    popup-id))
                                  :component-will-unmount (fn [] (popup-a.helpers/header-will-unmount-f popup-id))
                                  :reagent-render         (fn [] [header-structure popup-id popup-props])})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:body (metamorphic-content)
  ;  :header (metamorphic-content)(opt)}
  [popup-id {:keys [body header]}]
  ; The header sensor has to be placed at the beginning of the body content ...
  ; The footer sensor has to be placed at the end of the body content ...
  ; ... because the sensors have to be scrolled with the body content.
  ;
  ; The body content element has to be stretched vertically to its wrapper
  ; (to the body element) to provide the center alignment ability for the content.
  [:div.l-popup-a--body {:data-scroll-axis :y}
                        [:div.l-popup-a--body-content (if header [:div {:id (hiccup/value popup-id "header-sensor")}])
                                                      [x.components/content popup-id body]
                                                      (if footer [:div {:id (hiccup/value popup-id "footer-sensor")}])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  [:div.l-popup-a--structure (popup-a.helpers/popup-structure-attributes popup-id popup-props)
                             [:div.l-popup-a--hack [body   popup-id popup-props]
                                                   [header popup-id popup-props]]
                             [footer popup-id popup-props]])

(defn- popup-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:close-by-cover? (boolean)(opt)}
  ;  :stretch-orientation (keyword)(opt)}
  [popup-id {:keys [close-by-cover? stretch-orientation style] :as popup-props}]
  [:div.l-popup-a {:data-stretch-orientation stretch-orientation}
                  [:div.l-popup-a--cover (if close-by-cover? {:on-click #(r/dispatch [:x.ui/remove-popup! popup-id])})]
                  [popup-structure popup-id popup-props]])

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
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
  ;  :close-by-cover? (boolean)(opt)
  ;   Default: true
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :footer (metamorphic-content)(opt)
  ;  :header (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :stretch-orientation (keyword)(opt)
  ;  :horizontal, :vertical, :both
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [popup-a :my-popup {...}]
  [popup-id {:keys [on-mount on-unmount] :as popup-props}]
  (let [popup-props (popup-a.prototypes/popup-props-prototype popup-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [popup-a popup-id popup-props])})))
