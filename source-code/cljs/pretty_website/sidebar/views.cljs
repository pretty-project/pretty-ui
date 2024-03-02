
(ns pretty-website.sidebar.views
    (:require [fruits.random.api                 :as random]
              [multitype-content.api           :as multitype-content]
              [pretty-elements.api               :as pretty-elements]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [pretty-website.sidebar.attributes :as sidebar.attributes]
              [pretty-website.sidebar.prototypes :as sidebar.prototypes]
              [pretty-website.sidebar.state      :as sidebar.state]
              ;[react.api                         :as react]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:content (multitype-content)(opt)
  ;  :placeholder (multitype-content)(opt)}
  [sidebar-id {:keys [content placeholder] :as sidebar-props}]
  [:<> [pretty-elements/icon-button (sidebar.prototypes/menu-button-props-prototype sidebar-id sidebar-props)]])

       ; surface element replaces deprecated mount-animation component
       ;[react/mount-animation {:mounted? (= sidebar-id @sidebar.state/VISIBLE-SIDEBAR)}
        ;                      [:div (sidebar.attributes/sidebar-attributes sidebar-id sidebar-props)
        ;                            [:div (sidebar.attributes/sidebar-cover-attributes sidebar-id sidebar-props)]
        ;                            [:div (sidebar.attributes/sidebar-inner-attributes sidebar-id sidebar-props)
        ;                                  [:div {:class :pw-sidebar--content}
        ;                                        [multitype-content/compose content placeholder])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  [sidebar-id sidebar-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    sidebar-id sidebar-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount sidebar-id sidebar-props))
                         :reagent-render         (fn [_ sidebar-props] [sidebar sidebar-id sidebar-props])}))

(defn view
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :left, :right
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (multitype-content)(opt)
  ;  :cover-color (keyword or string)(opt)
  ;   Default: :black
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   Default: :white
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (multitype-content)(opt)
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
   [view (random/generate-keyword) sidebar-props])

  ([sidebar-id sidebar-props]
   ; @note (tutorials#parameterizing)
   (fn [_ sidebar-props]
       (let [sidebar-props (pretty-presets.engine/apply-preset         sidebar-id sidebar-props)
             sidebar-props (sidebar.prototypes/sidebar-props-prototype sidebar-id sidebar-props)]
            [view-lifecycles sidebar-id sidebar-props]))))
