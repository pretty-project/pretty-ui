
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

(defn component
  ; @info
  ; XXX#0709 (source-code/cljs/pretty_elements/icon/views.cljs)
  ; The 'list-item-icon' component is based on the 'icon' element.
  ; For more information, check out the documentation of the 'icon' element.
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
   [component (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   ; @note (tutorials#parametering)
   (fn [_ icon-props]
       [list-item-icon icon-id icon-props])))
