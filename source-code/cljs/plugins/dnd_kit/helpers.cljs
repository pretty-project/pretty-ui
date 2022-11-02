
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.dnd-kit.helpers
    (:require [mid-fruits.vector     :as vector]
              [plugins.dnd-kit.state :as state]
              [re-frame.api          :as r]
              [reagent.api           :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sortable-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:items (vector)}
  [sortable-id {:keys [items]}]
  (swap! state/SORTABLE-ITEMS assoc sortable-id items))

(defn sortable-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id _]
  (swap! state/SORTABLE-ITEMS dissoc sortable-id))

(defn sortable-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (?) %
  [sortable-id %]
  (let [[_ {:keys [items]}] (reagent/arguments %)]
       (swap! state/SORTABLE-ITEMS assoc sortable-id items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event->origin-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {}
  ; @param (?) event
  ;
  ; @return (integer)
  [_ {:keys [item-id-f items]} event]
  (vector/get-first-match-item-dex items #(= (item-id-f %)
                                             (aget event "active" "id"))))

(defn event->target-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {}
  ; @param (?) event
  ;
  ; @return (integer)
  [_ {:keys [item-id-f items]} event]
  (vector/get-first-match-item-dex items #(= (item-id-f %)
                                             (aget event "over" "id"))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn drag-start-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ; @param (?) event
  [sortable-id _ event]
  (let [grabbed-item-dex (get-in (js->clj (aget event "active")) ["data" "current" "sortable" "index"])]
       (swap! state/GRABBED-ITEM assoc sortable-id grabbed-item-dex)))

(defn drag-end-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:on-drag-end (metamorphic-event)(opt)
  ;   :on-order-changed (metamorphic-event)(opt)}
  ; @param (?) event
  [sortable-id {:keys [on-drag-end on-order-changed] :as sortable-props} event]
  (let [origin-dex (event->origin-dex sortable-id sortable-props event)
        target-dex (event->target-dex sortable-id sortable-props event)]
    ; Check if dragged element is moved if so than reset the items order.
    (if (not= origin-dex target-dex)
        (let [reordered-items (vector/move-item (get @state/SORTABLE-ITEMS sortable-id) origin-dex target-dex)]
             (swap! state/SORTABLE-ITEMS assoc sortable-id reordered-items)
             (if on-order-changed (let [on-order-changed (r/metamorphic-event<-params on-order-changed reordered-items)]
                                       (r/dispatch-sync on-order-changed)))))
    (swap! state/GRABBED-ITEM dissoc sortable-id)
    (if on-drag-end (r/dispatch-sync on-drag-end))))
