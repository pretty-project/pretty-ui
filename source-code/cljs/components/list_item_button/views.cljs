
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
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:background-color (keyword)(opt)
  ;   Default: :highlight
  ;  :hover-color (keyword)(opt)
  ;   Default: :highlight
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
  ;   W/ {:icon ...}
  ;  :icon-position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;   W/ {:icon ...}
  ;  :label (metamorphic-content)
  ;  :on-click (metamorphic-event)}
  ;
  ; @usage
  ; [list-item-button {...}]
  ;
  ; @usage
  ; [list-item-button :my-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (list-item-button.prototypes/button-props-prototype button-props)]
        [list-item-button button-id button-props])))
