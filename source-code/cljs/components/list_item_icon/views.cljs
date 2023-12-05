
(ns components.list-item-icon.views
    (:require [pretty-elements.api :as pretty-elements]
              [random.api          :as random]))

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
  ; For more information check out the documentation of the 'icon' element.
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
   (fn [_ icon-props] ; XXX#0106 (README.md#parametering)
       [list-item-icon icon-id icon-props])))
