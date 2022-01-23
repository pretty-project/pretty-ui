
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.events
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :as map]
              [mid-fruits.validator :as validator]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [app-plugins.item-browser.engine  :as engine]
              [app-plugins.item-browser.queries :as queries]
              [app-plugins.item-browser.subs    :as subs]
              [mid-plugins.item-browser.events  :as events]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-current-item-id!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/set-current-item-id! db :my-extension :my-type "my-item")
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  ; XXX#4031
  ; Az aktuálisan böngészett elem azonosítóját lehetséges a set-current-item-id! függvény
  ; használatával is beállítani, így az :item-id forrása nem kizárólag az aktuális útvonalból
  ; kiolvasott érték lehet.
  (assoc-in db [extension-id :item-browser/meta-items :item-id] item-id))

; @usage
;  [:item-browser/set-current-item-id! :my-extension :my-type "my-item"]
(a/reg-event-db :item-browser/set-current-item-id! set-current-item-id!)

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (r app-plugins.item-lister.events/set-error-mode! db extension-id))

(defn store-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [derived-item-id (r subs/get-derived-item-id db extension-id item-namespace)]
       (assoc-in db [extension-id :item-browser/meta-items :item-id] derived-item-id)))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (engine/resolver-id extension-id item-namespace :get)
        document    (get server-response resolver-id)]
       (if (validator/data-valid? document)
           ; XXX#3907
           ; Az item-lister pluginnal megegyezően az item-browser plugin is névtér nélkül tárolja
           ; a letöltött dokumentumot
           (let [document    (->  document validator/clean-validated-data db/document->non-namespaced-document)
                 document-id (get document :id)]
                (assoc-in db [extension-id :item-browser/data-items document-id] document))
           ; If the received document is NOT valid ...
           (r set-error-mode! db extension-id))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (r store-downloaded-item! db extension-id item-namespace server-response))

(defn load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (r store-current-item-id! db extension-id item-namespace))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-extension :my-type "my-item"]
  (fn [_ [_ extension-id item-namespace item-id]]
      (let [browser-uri (engine/browser-uri extension-id item-namespace item-id)]
           [:router/go-to! browser-uri])))

(a/reg-event-fx
  :item-browser/go-home!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-home! :my-extension :my-type]
  (fn [_ [_ extension-id item-namespace item-id]]
      (let [browser-uri (engine/browser-uri extension-id item-namespace)]
           [:router/go-to! browser-uri])))

(a/reg-event-fx
  :item-browser/go-up!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-up! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [parent-id   (r subs/get-parent-id db extension-id item-namespace)
            browser-uri (engine/browser-uri      extension-id item-namespace parent-id)]
           [:router/go-to! browser-uri])))

(a/reg-event-fx
  :item-browser/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [db (r receive-item! db extension-id item-namespace server-response)]
           (if-let [item-label (r subs/get-item-label db extension-id item-namespace)]
                   {:dispatch-n [[:ui/set-header-title! item-label]
                                 [:ui/set-window-title! item-label]]
                    :db db}
                   {:db db}))))

(a/reg-event-fx
  :item-browser/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {:on-success [:item-browser/receive-item!         extension-id item-namespace]
                          :query      (r queries/get-request-item-query db extension-id item-namespace)}]))

(a/reg-event-fx
  :item-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [browser-label (r subs/get-meta-item db extension-id item-namespace :label)]
           {:db (r load-browser! db extension-id item-namespace)

            :dispatch-n [; XXX#5660
                         [:environment/reg-keypress-listener! :item-browser/keypress-listener]
                         [:ui/set-header-title! browser-label]
                         [:ui/set-window-title! browser-label]
                         [:item-browser/request-item! extension-id item-namespace]
                         (engine/load-extension-event extension-id item-namespace)

                         ; Nem tudom itt-e a helye.
                         ; Amikor böngészel és a viewport-ban volt az inf-loader, és megnyitsz
                         ; egy elemet, akkor nem kerül ki és vissza a viewportba a loader, ezért
                         ; manuál kell ujrainditani
                         [:tools/reload-infinite-loader! extension-id]]})))
