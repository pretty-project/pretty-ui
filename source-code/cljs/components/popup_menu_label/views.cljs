
(ns components.popup-menu-label.views
    (:require [components.popup-menu-label.prototypes :as popup-menu-label.prototypes]
              [fruits.random.api                      :as random]
              [pretty-elements.api                    :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @note
  ; For more information, check out the documentation of the ['label'](/pretty-ui/cljs/pretty-elements/api.html#label) element.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:color (keyword or string)(opt)
  ;   Default: :muted
  ;  :gap (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :icon-size (keyword, px or string)(opt)
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xxs :vertical :s}}
  ;
  ; @usage
  ; [popup-menu-label {...}]
  ;
  ; @usage
  ; [popup-menu-label :my-popup-menu-label {...}]
  ([label-props]
   [view (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parameterizing)
   (fn [_ label-props]
       (let [label-props (popup-menu-label.prototypes/label-props-prototype label-props)]))))
            ;[pretty-elements/label label-id label-props]))))
