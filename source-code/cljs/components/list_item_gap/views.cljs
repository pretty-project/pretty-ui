
(ns components.list-item-gap.views
    (:require [css.api    :as css]
              [random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-gap
  ; @param (keyword) gap-id
  ; @param (map) gap-props
  ; {:width (px)}
  [_ {:keys [width]}]
  [:div.c-list-item-gap {}])

(defn component
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
   [component (random/generate-keyword) gap-props])

  ([gap-id gap-props]
   (fn [_ gap-props] ; XXX#0106 (README.md#parametering)
       (let [] ; gap-props (list-item-gap.prototypes/gap-props-prototype gap-props)
            [list-item-gap gap-id gap-props]))))
