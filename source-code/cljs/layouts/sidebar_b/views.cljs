
(ns layouts.sidebar-b.views
    (:require [layouts.sidebar-b.helpers    :as sidebar-b.helpers]
              [layouts.sidebar-b.prototypes :as sidebar-b.prototypes]
              [re-frame.api                 :as r]
              [reagent.api                  :as reagent]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:content (metamorphic-content)}
  [sidebar-id {:keys [content] :as sidebar-props}]
  [:div#l-sidebar-b--wrapper [:div#l-sidebar-b--body (sidebar-b.helpers/sidebar-body-attributes sidebar-id sidebar-props)
                                                     [x.components/content                      sidebar-id content]]])

(defn- sidebar-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:threshold (px)}
  [sidebar-id {:keys [threshold] :as sidebar-props}]
  (if @(r/subscribe [:x.environment/viewport-min? threshold])
       [:div#l-sidebar-b (sidebar-b.helpers/sidebar-attributes sidebar-id sidebar-props)
                         [:div#l-sidebar-b--sensor (sidebar-b.helpers/sidebar-sensor-attributes sidebar-id sidebar-props)]
                         [sidebar-body sidebar-id sidebar-props]]))

(defn layout
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
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
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :indent (map)(opt)
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :style (map)(opt)
  ;  :threshold (px)(opt)
  ;   Default: 720}
  ;
  ; @usage
  ; [sidebar-b :my-sidebar {...}]
  [sidebar-id {:keys [on-mount on-unmount] :as sidebar-props}]
  (let [sidebar-props (sidebar-b.prototypes/sidebar-props-prototype sidebar-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [sidebar-b sidebar-id sidebar-props])})))
