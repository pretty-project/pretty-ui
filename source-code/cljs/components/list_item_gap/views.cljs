
(ns components.list-item-gap.views
    (:require [fruits.css.api    :as css]
              [fruits.random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-gap
  ; @param (keyword) gap-id
  ; @param (map) gap-props
  ; {:width (px)}
  [_ {:keys [width]}]
  [:div.c-list-item-gap {}])

(defn view
  ; @param (keyword)(opt) gap-id
  ; @param (map) gap-props
  ; {}
  ;
  ; @usage
  ; [list-item-gap {...}]
  ;
  ; @usage
  ; [list-item-gap :my-gap {...}]
  ([gap-props]
   [view (random/generate-keyword) gap-props])

  ([gap-id gap-props]
   ; @note (tutorials#parameterizing)
   (fn [_ gap-props]
       (let [] ; gap-props (list-item-gap.prototypes/gap-props-prototype gap-props)
            [list-item-gap gap-id gap-props]))))
