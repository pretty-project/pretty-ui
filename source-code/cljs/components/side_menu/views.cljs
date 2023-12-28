
(ns components.side-menu.views
    (:require [components.side-menu.helpers    :as side-menu.helpers]
              [components.side-menu.prototypes :as side-menu.prototypes]
              [fruits.random.api               :as random]
              [metamorphic-content.api         :as metamorphic-content]
              [pretty-elements.api             :as pretty-elements]
              [window-observer.api             :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- side-menu
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:content (metamorphic-content)}
  [menu-id {:keys [content] :as menu-props}]
  [:div.c-side-menu (side-menu.helpers/menu-attributes menu-id menu-props)
                    [:div.c-side-menu--body (side-menu.helpers/menu-body-attributes menu-id menu-props)
                                            [metamorphic-content/compose content]]])

(defn component
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :style (map)(opt)
  ;  :threshold (px)(opt)}
  ;
  ; @usage
  ; [side-menu {...}]
  ;
  ; @usage
  ; [side-menu :my-side-menu {...}]
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id {:keys [threshold] :as menu-props}]
   ; @note (tutorials#parametering)
   (fn [_ menu-props]
       (let [menu-props (side-menu.prototypes/menu-props-prototype menu-props)]
            (if (or (not                                 threshold)
                    (window-observer/viewport-width-min? threshold))
                [side-menu menu-id menu-props])))))
