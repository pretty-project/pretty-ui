
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.effects
    (:require [plugins.item-editor.body.events   :as body.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.backup.events :as backup.events]
              [plugins.item-editor.backup.subs   :as backup.subs]
              [reagent.api                       :as reagent]
              [x.app-core.api                    :as a :refer [r]]))



;; -- Body lifecycles effects -------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ editor-id body-props]]
      {:db       (r body.events/body-did-mount db editor-id body-props)
       :dispatch [:item-editor/load-editor! editor-id]}))

(a/reg-event-fx
  :item-editor/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      ; Ha az item-editor plugin elhagyásakor, az elem el nem mentett változtatásokat tartalmaz,
      ; és a plugin nem "Új elem hozzáadása" módban futott ...
      ; ... megjelenít egy értesítést az el nem mentett változtatások visszavonásának lehetőségével.
      ; ... eltárolja az elem változtatásait, ami alapján az elem visszaállítása elvégezhető.
      ;
      ; BUG#5161
      ; Az x4.7.0 változat óta, ha a plugin "Új elem hozzáadása" módban fut, a current-item-id
      ; értéke NIL, ezért nem lehetséges az új elemről másolatot készíteni kilépéskor.
      ;
      ; + A body-will-unmount esemény megtörténésekor a new-item? függvény már használható,
      ;   mert az útvonal megváltozása után történik meg az esemény.
      {:db (r body.events/body-will-unmount db editor-id)}))
      ;(let [current-item-id (r core.subs/get-current-item-id db editor-id)
      ;      item-changed?   (r backup.subs/item-changed?     db editor-id)
      ;      new-item?       (r core.subs/new-item?           db editor-id))}))
      ;     (if (or new-item? (not item-changed?))
      ;         {:db (as-> db % (r body.events/body-will-unmount             % editor-id))}
      ;         {:db (as-> db % (r backup.events/store-current-item-changes! % editor-id)
      ;                         (r body.events/body-will-unmount             % editor-id))
      ;          :dispatch [:item-editor/render-changes-discarded-dialog! editor-id current-item-id]])))

(a/reg-event-fx
  :item-editor/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (?) %
  (fn [{:keys [db]} [_ editor-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db (r body.events/body-did-update db editor-id body-props)})))
