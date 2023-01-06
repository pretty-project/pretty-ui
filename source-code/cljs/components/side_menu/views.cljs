
(ns components.side-menu.views
    (:require [components.side-menu.helpers    :as side-menu.helpers]
              [components.side-menu.prototypes :as side-menu.prototypes]
              [elements.api                    :as elements]
              [random.api                      :as random]
              [re-frame.api                    :as r]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- side-menu
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:content (metamorphic-content)}
  [menu-id {:keys [content] :as menu-props}]
  [:div.c-side-menu (side-menu.helpers/menu-attributes menu-id menu-props)
                    [:div.c-side-menu--body (side-menu.helpers/menu-body-attributes menu-id menu-props)
                                            [x.components/content menu-id content]]])

(defn component
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :indent (map)(opt)
  ;  :outdent (map)(opt)
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
   (let [menu-props (side-menu.prototypes/menu-props-prototype menu-props)]
        (if (or (not threshold) @(r/subscribe [:x.environment/viewport-min? threshold]))
            [side-menu menu-id menu-props]))))
