
(ns components.list-item-icon.views
    (:require [fruits.random.api   :as random]
              [pretty-elements.api :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-icon
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  [icon-id icon-props]
  [:div {:class :c-list-item-icon}
        [pretty-elements/icon icon-id icon-props]])

(defn view
  ; @note
  ; For more information, check out the documentation of the ['icon'](/pretty-ui/cljs/pretty-elements/api.html#icon) element.
  ;
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ;
  ; @usage
  ; [list-item-icon {...}]
  ;
  ; @usage
  ; [list-item-icon :my-list-item-icon {...}]
  ([icon-props]
   [view (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   ; @note (tutorials#parametering)
   (fn [_ icon-props]
       [list-item-icon icon-id icon-props])))
