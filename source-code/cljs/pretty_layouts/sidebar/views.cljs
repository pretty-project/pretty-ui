
(ns pretty-layouts.sidebar.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-engine.api                 :as pretty-engine]
              [pretty-layouts.sidebar.attributes :as sidebar.attributes]
              [pretty-layouts.sidebar.prototypes :as sidebar.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]
              [window-observer.api               :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:content (metamorphic-content)(opt)
  ;  :threshold (px)}
  [sidebar-id {:keys [content threshold] :as sidebar-props}]
  (if (window-observer/viewport-width-min? threshold)
      [:div (sidebar.attributes/sidebar-attributes sidebar-id sidebar-props)
            [:div (sidebar.attributes/sidebar-sensor-attributes sidebar-id sidebar-props)]
            [:div {:class :pl-sidebar--wrapper}
                  [:div (sidebar.attributes/sidebar-body-attributes sidebar-id sidebar-props)
                        [metamorphic-content/compose content]]]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-lifecycles
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {}
  [sidebar-id {:keys [on-mount on-unmount] :as sidebar-props}]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                       :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                       :reagent-render         (fn [_ sidebar-props] [sidebar sidebar-id sidebar-props])}))

(defn layout
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;   Default: :xxs
  ;  :content (metamorphic-content)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
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
   ; @note (tutorials#parametering)
   (fn [_ sidebar-props]
       (let [sidebar-props (pretty-presets/apply-preset                sidebar-props)
             sidebar-props (sidebar.prototypes/sidebar-props-prototype sidebar-props)]
            [layout-lifecycles sidebar-id sidebar-props]))))
