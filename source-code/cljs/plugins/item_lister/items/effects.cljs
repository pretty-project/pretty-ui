
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
  ;  {:memory-mode? (boolean)(opt)
  ;   :on-click (metamorphic-event)
  ;   :on-select (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:item-lister/item-clicked :my-lister 0 {...}]
  (fn [{:keys [db]} [_ lister-id item-dex {:keys [memory-mode? on-click on-select]}]]
      ; A) ...
      ;
      ; B) ...
      ;    TEMP
      ;    Az egyes listaelemekre való kattintáskor az item-lister plugin {:memory-mode? true}
      ;    állapotba lép és megjegyzi a lista keresési és rendezési beállításait, így a listaelemekre
      ;    történő kattintás következményeként elhagyott item-lister a következő betöltődéskor
      ;    az előző beállításokkal fog elindulni.
      ;    TEMP
      ;
      ; C) ...
      (cond ; A)
            (r items.subs/toggle-item-selection? db lister-id item-dex)
            {:db       (r items.events/toggle-item-selection! db lister-id item-dex)
             :dispatch on-select}
            ; B)
            memory-mode?
            {:db (r core.events/set-memory-mode! db lister-id)
             :dispatch on-click}
            ; C)
            :else on-click)))
