
(ns plugins.dnd-kit.helpers
    (:require [plugins.dnd-kit.state :as state]
              [plugins.dnd-kit.utils :refer [to-clj-map reorder index-of]]
              [re-frame.api :as r]
              [reagent.api  :as reagent]))



;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

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



;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn drag-start-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ; @param (?) event
  [sortable-id _ event]
  (swap! state/GRABBED-ITEM assoc sortable-id (get-in (js->clj (aget event "active")) ["data" "current" "sortable" "index"])))

(defn drag-end-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:on-drag-end (metamorphic-event)(opt)
  ;   :on-order-changed (metamorphic-event)(opt)}
  ; @param (?) event
  [sortable-id {:keys [on-drag-end on-order-changed]} event]
  (let [{:keys [active over]} (to-clj-map event)
        origin-dex (index-of (get @state/SORTABLE-ITEMS sortable-id) active)
        taget-dex  (index-of (get @state/SORTABLE-ITEMS sortable-id) over)]
    ;;check if dragged element is moved if so than reset the items order
    (if (not= origin-dex taget-dex)
        (let [reordered-items (reorder (get @state/SORTABLE-ITEMS sortable-id) origin-dex taget-dex)]
             (swap! state/SORTABLE-ITEMS assoc sortable-id reordered-items)
             (if on-order-changed (r/dispatch-sync (r/metamorphic-event<-params on-order-changed reordered-items)))))
    (swap! state/GRABBED-ITEM dissoc sortable-id)
    (if on-drag-end (r/dispatch-sync on-drag-end))))
