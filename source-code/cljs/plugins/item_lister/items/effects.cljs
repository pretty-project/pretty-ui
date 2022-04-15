
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.effects
    (:require [plugins.item-lister.core.events  :as core.events]
              [plugins.item-lister.items.events :as items.events]
              [plugins.item-lister.items.subs   :as items.subs]
              [x.app-core.api                   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/item-clicked
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:on-click (metamorphic-event)
  ;   :on-select (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:item-lister/item-clicked :my-lister 0 {...}]
  (fn [{:keys [db]} [_ lister-id item-dex {:keys [on-click on-select]}]]
      (println (str (r items.subs/toggle-item-selection? db lister-id item-dex)))
      ; A) ...
      ;
      ; B) ...
      (if (r items.subs/toggle-item-selection? db lister-id item-dex)
          ; A)
          {:db (r items.events/toggle-item-selection! db lister-id item-dex)
           :dispatch on-select}
          ; B)
          {:db (r core.events/set-memory-mode! db lister-id)
           :dispatch on-click})))
