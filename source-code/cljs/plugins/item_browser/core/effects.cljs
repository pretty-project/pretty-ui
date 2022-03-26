
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Ha az [:item-browser/handle-route! ...] esemény megtörténésekor a body komponens ...
      ; A) ... a React-fába van csatolva, akkor szükséges az infinite-loader komponenst újratölteni,
      ;        és az aktuálisan böngészett elem adatait letölteni.
      ;
      ; B) ... NINCS a React-fába csatolva, akkor
      (let [on-route    (r transfer.subs/get-transfer-item db extension-id item-namespace :on-route)
            route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
           (if (r mount.subs/body-did-mount? db extension-id item-namespace)
               ; A)
               {:db (r core.events/handle-route! db extension-id item-namespace)
                :dispatch-n [on-route [:tools/reload-infinite-loader! extension-id]
                                      [:item-browser/request-item! extension-id item-namespace]]}
               ; B)
               {:db (r core.events/handle-route! db extension-id item-namespace)
                :dispatch-n [on-route (if route-title [:ui/set-title! route-title])]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      ;
      (if-let [item-route (r routes.subs/get-item-route db extension-id item-namespace item-id)]
              ; A)
              [:router/go-to! item-route])))

(a/reg-event-fx
  :item-browser/go-home!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-home! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [root-item-id (r mount.subs/get-body-prop db extension-id item-namespace :root-item-id)]
           [:item-browser/browse-item! extension-id item-namespace root-item-id])))

(a/reg-event-fx
  :item-browser/go-up!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-up! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-item-id (r core.subs/get-parent-item-id db extension-id item-namespace)]
           [:item-browser/browse-item! extension-id item-namespace parent-item-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/use-filter!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-browser/use-filter! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace filter-pattern]]
      {:db (r core.events/use-filter! db extension-id item-namespace filter-pattern)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))
