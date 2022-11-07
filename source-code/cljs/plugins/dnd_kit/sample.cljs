
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.dnd-kit.sample
    (:require [plugins.dnd-kit.api :as dnd-kit]
              [re-frame.api        :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :my-order-changed
  (fn [_ [_ reordered-items]]))
      ; ...

(defn my-item-element
  [sortable-id item-dex item {:keys [handle-attributes item-attributes dragging?] :as drag-props}]
  [:div [:div.my-drag-handle handle-attributes (str "Grab me!")]
        [:div.my-item        item-attributes   (str item)]])

(defn my-sortable
  []
  [dnd-kit/body {:item-element #'my-item-element
                 :items ["My item" "Your item"]
                 :on-order-changed [:my-order-changed]}])
