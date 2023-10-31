
(ns components.list-item-button.views
    (:require [components.list-item-button.prototypes :as list-item-button.prototypes]
              [css.api                                :as css]
              [elements.api                           :as elements]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-button
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  [button-id {:keys [] :as button-props}]
  [:div.c-list-item-button [:div.c-list-item-button--body [elements/button button-id button-props]]])

(defn component
  ; @info
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The 'list-menu-button' component is based on the 'button' element.
  ; For more information check out the documentation of the 'button' element.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:fill-color (keyword)(opt)
  ;   Default: :highlight
  ;  :font-size (keyword)(opt)
  ;   Default: :xs
  ;  :hover-color (keyword)(opt)
  ;   Default: :highlight}
  ;
  ; @usage
  ; [list-item-button {...}]
  ;
  ; @usage
  ; [list-item-button :my-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props] ; XXX#0106 (README.md#parametering)
       (let [button-props (list-item-button.prototypes/button-props-prototype button-props)]
            [list-item-button button-id button-props]))))
