
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.body.effects
    (:require [engines.item-handler.body.events   :as body.events]
              [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.backup.events :as backup.events]
              [engines.item-handler.backup.subs   :as backup.subs]
              [reagent.api                        :as reagent]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ handler-id body-props]]
      {:db       (r body.events/body-did-mount db handler-id body-props)
       :dispatch [:item-handler/load-handler! handler-id]}))

(r/reg-event-fx :item-handler/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      ; Ha az item-handler engine elhagyásakor, az elem el nem mentett változtatásokat tartalmaz,
      ; és az engine nem "Új elem hozzáadása" módban futott ...
      ; ... megjelenít egy értesítést az el nem mentett változtatások visszavonásának lehetőségével.
      ; ... eltárolja az elem változtatásait, ami alapján az elem visszaállítása elvégezhető.
      ;
      ; BUG#5161
      ; Az x4.7.0 változat óta, ha az engine "Új elem hozzáadása" módban fut, a current-item-id
      ; értéke NIL, ezért nem lehetséges az új elemről másolatot készíteni kilépéskor.
      ;
      ; + A body-will-unmount esemény megtörténésekor a new-item? függvény már használható,
      ;   mert az útvonal megváltozása után történik meg az esemény.
      {:db (r body.events/body-will-unmount db handler-id)}))
      ;(let [current-item-id (r core.subs/get-current-item-id db handler-id)
      ;      item-changed?   (r backup.subs/item-changed?     db handler-id)
      ;      new-item?       (r core.subs/new-item?           db handler-id))}))
      ;     (if (or new-item? (not item-changed?))
      ;         {:db (as-> db % (r body.events/body-will-unmount             % handler-id))}
      ;         {:db (as-> db % (r backup.events/store-current-item-changes! % handler-id)
      ;                         (r body.events/body-will-unmount             % handler-id))
      ;          :dispatch [:item-handler/render-changes-discarded-dialog! handler-id current-item-id]])))

(r/reg-event-fx :item-handler/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (?) %
  (fn [{:keys [db]} [_ handler-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db (r body.events/body-did-update db handler-id body-props)})))
