
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.core.effects
    (:require [plugins.item-viewer.body.subs   :as body.subs]
              [plugins.item-viewer.core.events :as core.events]
              ;[plugins.item-viewer.core.subs   :as core.subs]
              [plugins.item-viewer.routes.subs :as routes.subs]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/view-item!
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-viewer/view-item! :my-viewer "my-item"]
  (fn [{:keys [db]} [_ viewer-id item-id]]
      ; A) Ha az item-viewer plugin útvonal-vezérelt, ...
      ;    ... akkor elkészíti az elem megtekintéséhez az útvonalat és átirányít arra.
      ;
      ; B) Ha az item-viewer plugin NEM útvonal-vezérelt, ...
      ;    ... akkor beállítja az item-id paraméter értékét az aktuálisan megtekintett
      ;        elem azonosítójaként.
      ;    ... meghívja az [:item-viewer/load-viewer! ...] eseményt.
      (if-let [route-handled? (r routes.subs/route-handled? db viewer-id)]
              ; A)
              (let [item-route (r routes.subs/get-item-route db viewer-id item-id)]
                   {:dispatch [:router/go-to! item-route]})
              ; B)
              {:db       (r core.events/set-item-id! db viewer-id item-id)
               :dispatch [:item-viewer/load-viewer! viewer-id]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/load-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      {:db       (r core.events/load-viewer! db viewer-id)
       :dispatch [:item-viewer/request-item! viewer-id]}))
