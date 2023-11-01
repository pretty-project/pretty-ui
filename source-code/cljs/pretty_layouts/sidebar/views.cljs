
(ns pretty-layouts.sidebar.views
    (:require [metamorphic-content.api           :as metamorphic-content]
              [pretty-layouts.sidebar.attributes :as sidebar.attributes]
              [pretty-layouts.sidebar.prototypes :as sidebar.prototypes]
              [random.api                        :as random]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]
              [window-observer.api               :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-structure
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:content (metamorphic-content)
  ;  :threshold (px)}
  [sidebar-id {:keys [content threshold] :as sidebar-props}]
  (if (window-observer/viewport-width-min? threshold)
      [:div (sidebar.attributes/sidebar-attributes sidebar-id sidebar-props)
            [:div (sidebar.attributes/sidebar-sensor-attributes sidebar-id sidebar-props)]
            [:div {:class :pl-sidebar--wrapper}
                  [:div (sidebar.attributes/sidebar-body-attributes sidebar-id sidebar-props)
                        [metamorphic-content/compose content]]]]))

(defn- sidebar
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {}
  [sidebar-id {:keys [on-mount on-unmount] :as sidebar-props}]
  ; XXX#0106 (README.md#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                       :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                       :reagent-render         (fn [_ sidebar-props] [sidebar-structure sidebar-id sidebar-props])}))

(defn layout
  ; @param (keyword)(opt) sidebar-id
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
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :style (map)(opt)
  ;  :threshold (px)(opt)
  ;   Default: 720}
  ;
  ; @usage
  ; [sidebar {...}]
  ;
  ; @usage
  ; [sidebar :my-sidebar {...}]
  ([sidebar-props]
   [layout (random/generate-keyword) sidebar-props])

  ([sidebar-id sidebar-props]
   (fn [_ sidebar-props] ; XXX#0106 (README.md#parametering)
       (let [sidebar-props (sidebar.prototypes/sidebar-props-prototype sidebar-props)]
            [sidebar sidebar-id sidebar-props]))))
