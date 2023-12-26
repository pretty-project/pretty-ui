
(ns pretty-website.sidebar.views
    (:require [fruits.random.api                 :as random]
              [metamorphic-content.api           :as metamorphic-content]
              [pretty-elements.api               :as pretty-elements]
              [pretty-presets.api                :as pretty-presets]
              [pretty-website.sidebar.attributes :as sidebar.attributes]
              [pretty-website.sidebar.prototypes :as sidebar.prototypes]
              [pretty-website.sidebar.state      :as sidebar.state]
              [react.api                         :as react]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:content (metamorphic-content)}
  [sidebar-id {:keys [content] :as sidebar-props}]
  [:<> [pretty-elements/icon-button (sidebar.prototypes/menu-button-props-prototype sidebar-id sidebar-props)]
       [react/mount-animation {:mounted? (= sidebar-id @sidebar.state/VISIBLE-SIDEBAR)}
                              [:div (sidebar.attributes/sidebar-attributes sidebar-id sidebar-props)
                                    [:div (sidebar.attributes/sidebar-cover-attributes sidebar-id sidebar-props)]
                                    [:div (sidebar.attributes/sidebar-body-attributes sidebar-id sidebar-props)
                                          [:div {:class :pw-sidebar--content}
                                                [metamorphic-content/compose content]]]]]])

(defn component
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :left, :right
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :cover-color (keyword or string)(opt)
  ;   Default: :black
  ;  :fill-color (keyword or string)(opt)
  ;   Default: :white
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
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
            [sidebar sidebar-id sidebar-props]))))
