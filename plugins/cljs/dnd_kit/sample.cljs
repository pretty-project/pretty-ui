
(ns dnd-kit.sample
    (:require [dnd-kit.api  :as dnd-kit]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :my-order-changed
  ; @param (vector) reordered-items
  (fn [_ [_ reordered-items]]))
      ; ...

(defn my-item-element
  ; @param (keyword) sortable-id
  ; @param (integer) item-id
  ; @param (*) item
  ; @param (map) drag-props
  ; {:dragging? (boolean)
  ;  :handle-attributes (map)
  ;  :item-attributes (map)}
  [sortable-id item-dex item {:keys [dragging? handle-attributes item-attributes] :as drag-props}]
  [:div [:div.my-drag-handle handle-attributes (str "Grab me!")]
        [:div.my-item        item-attributes   (str item)]])

(defn my-sortable
  []
  [dnd-kit/body {:item-element #'my-item-element
                 :items ["My item" "Your item"]
                 :on-order-changed [:my-order-changed]}])
