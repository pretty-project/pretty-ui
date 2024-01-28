
(ns pretty-website.sidebar.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.api               :as pretty-elements]
              [pretty-engine.api                 :as pretty-engine]
              [pretty-presets.api                :as pretty-presets]
              [pretty-website.sidebar.attributes :as sidebar.attributes]
              [pretty-website.sidebar.prototypes :as sidebar.prototypes]
              [pretty-website.sidebar.state      :as sidebar.state]
              [react.api                         :as react]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [sidebar-id {:keys [content placeholder] :as sidebar-props}]
  [:<> [pretty-elements/icon-button (sidebar.prototypes/menu-button-props-prototype sidebar-id sidebar-props)]
       [react/mount-animation {:mounted? (= sidebar-id @sidebar.state/VISIBLE-SIDEBAR)}
                              [:div (sidebar.attributes/sidebar-attributes sidebar-id sidebar-props)
                                    [:div (sidebar.attributes/sidebar-cover-attributes sidebar-id sidebar-props)]
                                    [:div (sidebar.attributes/sidebar-body-attributes sidebar-id sidebar-props)
                                          [:div {:class :pw-sidebar--content}
                                                [metamorphic-content/compose content placeholder]]]]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- component-lifecycles
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  [sidebar-id sidebar-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-engine/element-did-mount    sidebar-id sidebar-props))
                       :component-will-unmount (fn [_ _] (pretty-engine/element-will-unmount sidebar-id sidebar-props))
                       :reagent-render         (fn [_ sidebar-props] [sidebar sidebar-id sidebar-props])}))

(defn component
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :left, :right
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :cover-color (keyword or string)(opt)
  ;   Default: :black
  ;  :fill-color (keyword or string)(opt)
  ;   Default: :white
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [sidebar {...}]
  ;
  ; @usage
  ; [sidebar :my-sidebar {...}]
  ([sidebar-props]
   [component (random/generate-keyword) sidebar-props])

  ([sidebar-id sidebar-props]
   ; @note (tutorials#parametering)
   (fn [_ sidebar-props]
       (let [sidebar-props (pretty-presets/apply-preset                sidebar-props)
             sidebar-props (sidebar.prototypes/sidebar-props-prototype sidebar-props)]
            [component-lifecycles sidebar-id sidebar-props]))))
