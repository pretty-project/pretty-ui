
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.effects
    (:require [plugins.item-browser.core.events   :as core.events]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.mount.subs    :as mount.subs]
              [plugins.item-browser.routes.subs   :as routes.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      ; Ha az [:item-browser/handle-route! ...] esemény megtörténésekor a body komponens ...
      ; A) ... a React-fába van csatolva, akkor szükséges az infinite-loader komponenst újratölteni,
      ;        és az aktuálisan böngészett elem adatait letölteni.
      ;
      ; B) ... NINCS a React-fába csatolva, akkor
      (let [on-route    (r transfer.subs/get-transfer-item db browser-id :on-route)
            route-title (r transfer.subs/get-transfer-item db browser-id :route-title)]
           (if (r mount.subs/body-did-mount? db browser-id)
               ; A)
               {:db (r core.events/handle-route! db browser-id)
                :dispatch-n [on-route [:tools/reload-infinite-loader! browser-id]
                                      [:item-browser/request-item!    browser-id]]}
               ; B)
               {:db (r core.events/handle-route! db browser-id)
                :dispatch-n [on-route (if route-title [:ui/set-title! route-title])]}))))



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
      ;   Pl.: a felhasználó a böngésző "Vissza" gombjára kattint az item-browser plugin hanszálata
      ;        közben.
      ;
      ; A) ...
      ;
      ; B) ...
      (if-let [item-route (r routes.subs/get-item-route db browser-id item-id)]
              ; A)
              [:router/go-to! item-route]
              ; B)
              {:db (r core.events/browse-item! db browser-id item-id)
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
  :item-browser/use-filter!
  ; @param (keyword) browser-id
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-browser/use-filter! :my-browser {...}]
  (fn [{:keys [db]} [_ browser-id filter-pattern]]
      {:db (r core.events/use-filter! db browser-id filter-pattern)
       :dispatch [:tools/reload-infinite-loader! browser-id]}))
