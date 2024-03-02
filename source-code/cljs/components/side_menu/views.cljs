
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
  ; {:content (multitype-content)
  ;  :threshold (px)(opt)}
  [menu-id {:keys [content threshold] :as menu-props}]
  (if (or (-> threshold not)
          (-> threshold window-observer/viewport-width-min?))
      [:div.c-side-menu (side-menu.helpers/menu-attributes menu-id menu-props)
                        [:div.c-side-menu--inner (side-menu.helpers/menu-inner-attributes menu-id menu-props)
                                                 [metamorphic-content/compose content]]]))

(defn view
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (multitype-content)
  ;  :fill-color (keyword or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :min-width (keyword, px or string)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
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
   [view (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   ; @note (tutorials#parameterizing)
   (fn [_ menu-props]
       (let [menu-props (side-menu.prototypes/menu-props-prototype menu-props)]
            [side-menu menu-id menu-props]))))
