
(ns plugins.dnd-kit.sample
    (:require [plugins.dnd-kit.api :as dnd-kit]
              [re-frame.api        :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :my-order-changed
  (fn [_ [_ reordered-items]]))
      ; ...

(defn my-item-element
  [sortable-id item-dex item {:keys [attributes listeners isDragging] :as dnd-kit-props}]
  [:div [:div.my-drag-handle (merge attributes listeners)]
        [:div.my-item        (str   item)]])

(defn my-sortable
  []
  [dnd-kit/body {:item-element #'my-item-element
                 :items ["My item" "Your item"]
                 :on-order-changed [:my-order-changed]}])
