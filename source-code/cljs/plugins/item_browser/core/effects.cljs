
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.effects
    (:require [plugins.item-browser.core.events  :as core.events]
              [plugins.item-browser.core.subs    :as core.subs]
              [plugins.item-browser.items.events :as items.events]
              [plugins.item-browser.items.subs   :as items.subs]
              [plugins.item-browser.mount.subs   :as mount.subs]
              [plugins.item-browser.routes.subs  :as routes.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id]]
      ; Az item-browser pluginban az aktuálisan böngészett elem többféleképpen változhat meg:
      ; - Az [:item-browser/browse-item! ...] esemény A) kimenetele átirányít az elemhez készített
      ;   útvonalra, majd az [:item-browser/handle-route! ...] esemény újratölti az infinite-loader
      ;   komponenst és letölti az aktuálisan böngészett elem adatait.
      ;
      ; - Az [:item-browser/browse-item! ...] esemény B) kimenetele újratölti az infinite-loader
      ;   komponenst és letölti az aktuálisan böngészett elem adatait.
      ;
      ; - Az [:item-browser/handle-route! ...] esemény az [:item-browser/browse-item! ...] eseménytől
      ;   függetlenül megtörténik és ...
      ;   Pl.: Ha a felhasználó a böngésző "Vissza" gombjára kattint az item-browser plugin hanszálata
      ;        közben.
      ;
      ; A) ...
      ;
      ; B) ...
      (if-let [item-route (r routes.subs/get-item-route db browser-id item-id)]
              ; A)
              [:router/go-to! item-route]
              ; B)
              {:db         (r core.events/browse-item! db browser-id item-id)
               :dispatch-n [[:tools/reload-infinite-loader! browser-id]
                            [:item-browser/request-item!    browser-id]]})))

(a/reg-event-fx
  :item-browser/go-home!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  [:item-browser/go-home! :my-browser]
  (fn [{:keys [db]} [_ browser-id]]
      (let [root-item-id (r mount.subs/get-body-prop db browser-id :root-item-id)]
           [:item-browser/browse-item! browser-id root-item-id])))

(a/reg-event-fx
  :item-browser/go-up!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  [:item-browser/go-up! :my-browser]
  (fn [{:keys [db]} [_ browser-id]]
      (let [parent-item-id (r core.subs/get-parent-item-id db browser-id)]
           [:item-browser/browse-item! browser-id parent-item-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/filter-items!
  ; @param (keyword) browser-id
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-browser/filter-items! :my-browser {...}]
  (fn [{:keys [db]} [_ browser-id filter-pattern]]
      {:db       (r core.events/filter-items! db browser-id filter-pattern)
       :dispatch [:tools/reload-infinite-loader! browser-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/item-clicked
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) options
  ;  {:on-click (metamorphic-event)}
  ;
  ; @usage
  ;  [:item-browser/item-clicked :my-browser 0 {...}]
  (fn [{:keys [db]} [_ browser-id item-dex {:keys [on-click]}]]
      ; A) ...
      ;
      ; B) ...
      (if (r items.subs/toggle-item-selection? db browser-id item-dex)
          ; A)
          {:db (r items.events/toggle-item-selection! db browser-id item-dex)}
          ; B)
          {:dispatch on-click})))
