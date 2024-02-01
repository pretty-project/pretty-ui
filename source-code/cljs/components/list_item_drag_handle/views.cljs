
(ns components.list-item-drag-handle.views
    (:require [components.list-item-drag-handle.prototypes :as list-item-drag-handle.prototypes]
              [fruits.random.api                           :as random]
              [pretty-elements.api                         :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-drag-handle
  ; @param (keyword) handle-id
  ; @param (map) handle-props
  ; {:drag-attributes (map)}
  [_ {:keys [drag-attributes]}]
  [:div.c-list-item-drag-handle drag-attributes [pretty-elements/icon {:icon :drag_handle}]])

(defn view
  ; @param (keyword)(opt) handle-id
  ; @param (map)(opt) handle-props
  ; {:drag-attributes (map)}
  ;
  ; @usage
  ; [list-item-drag-handle {...}]
  ;
  ; @usage
  ; [list-item-drag-handle :my-handle {...}]
  ([handle-props]
   [view (random/generate-keyword) handle-props])

  ([handle-id handle-props]
   ; @note (tutorials#parametering)
   (fn [_ handle-props]
       (let [] ; handle-props (list-item-drag-handle.prototypes/handle-props-prototype handle-props)
            [list-item-drag-handle handle-id handle-props]))))
