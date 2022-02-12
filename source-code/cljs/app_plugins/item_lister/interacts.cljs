
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v1.1.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-ui.api      :as ui]
              [x.app-environment.api           :as environment]
              [app-plugins.item-lister.dialogs :as dialogs]
              [app-plugins.item-lister.engine  :as engine]
              [app-plugins.item-lister.queries :as queries]
              [app-plugins.item-lister.subs    :as subs]
              [mid-plugins.item-lister.events  :as events]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- items-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-ids]]
  (r backup-items! db extension-id item-namespace item-ids))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/delete-selected-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/delete-selected-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (as-> db % (r disable-selected-items! % extension-id item-namespace)
                      (r ui/fake-process! % 5))
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {;:display-progress? true
                                     :on-success [:item-lister/items-deleted           extension-id item-namespace]
                                     :on-failure [:item-lister/items-deleting-failured extension-id item-namespace]
                                     :query      (r queries/get-delete-selected-items-query db extension-id item-namespace)}]}))

(a/reg-event-fx
  :item-lister/duplicate-selected-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/duplicate-selected-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {;:display-progress? true
                          :on-success [:item-lister/items-duplicated extension-id item-namespace]
                          :on-failure [:ui/blow-bubble! {:body :failed-to-duplicate}]
                          :query      (r queries/get-duplicate-selected-items-query db extension-id item-namespace)}]))

(a/reg-event-fx
  :item-lister/undo-delete-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [{:keys [db]} [_ extension-id item-namespace item-ids]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {;:display-progress? true
                          :on-success [:item-lister/reload-items! extension-id item-namespace]
                          :on-failure [:ui/blow-bubble! {:body :failed-to-undo-delete}]
                          :query      (r queries/get-undo-delete-items-query db extension-id item-namespace item-ids)}]))

(a/reg-event-fx
  :item-lister/undo-duplicate-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  (fn [{:keys [db]} [_ extension-id item-namespace copy-ids]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {;:display-progress? true
                          :on-success [:item-lister/reload-items! extension-id item-namespace]
                          :on-failure [:ui/blow-bubble! {:body :failed-to-undo-duplicate}]
                          :query      (r queries/get-undo-duplicate-items-query db extension-id item-namespace copy-ids)}]))

(a/reg-event-fx
  :item-lister/items-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace server-response]]
      (let [item-ids (engine/server-response->deleted-item-ids extension-id item-namespace server-response)]
           {:db (r items-deleted db extension-id item-namespace item-ids)
            :dispatch-n [(r dialogs/render-items-deleted-dialog! cofx extension-id item-namespace item-ids)
                         [:item-lister/reload-items!                  extension-id item-namespace]]})))

(a/reg-event-fx
  :item-lister/items-deleting-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {;:db (r ui/stop-faking-process! db)
       :dispatch [:ui/blow-bubble! {:body :failed-to-delete}]}))

(a/reg-event-fx
  :item-lister/items-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace server-response]]
      (let [copy-ids (engine/server-response->duplicated-item-ids extension-id item-namespace server-response)]
           {:db (r reset-selections! db extension-id item-namespace)
            :dispatch-n [(r dialogs/render-items-duplicated-dialog! cofx extension-id item-namespace copy-ids)
                         [:item-lister/reload-items!                     extension-id item-namespace]]})))

(a/reg-event-fx
  :item-lister/item-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) item
  (fn [{:keys [db]} [_ extension-id item-namespace item-dex item]]
      ; XXX#5660
      ; A SHIFT billentyű lenyomása közben az elemre kattintva az elem, hozzáadódik a kijelölt elemek listájához.
      (if (r environment/key-pressed? db 16)
          (if-not (r subs/lister-disabled? db extension-id item-namespace)
                  {:db (r toggle-item-selection! db extension-id item-namespace item-dex)})
          (engine/item-clicked-event extension-id item-namespace item-dex item))))

(a/reg-event-fx
  :item-lister/item-right-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ; @param (map) item
  (fn [{:keys [db]} [_ extension-id item-namespace item-dex item]]
      (engine/item-right-clicked-event extension-id item-namespace item-dex item)))
