
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.5.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.events
    (:require [app-plugins.item-lister.events]
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.validator :as validator]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [app-plugins.item-browser.engine  :as engine]
              [app-plugins.item-browser.queries :as queries]
              [app-plugins.item-browser.subs    :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-lister.events
(def set-error-mode! app-plugins.item-lister.events/set-error-mode!)
(def use-filter!     app-plugins.item-lister.events/use-filter!)
(def load-lister!    app-plugins.item-lister.events/load-lister!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [extension-id :item-lister/meta-items :error-mode?]))

(defn derive-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (assoc-in db [extension-id :item-browser/meta-items :item-id]
               (or (r subs/get-derived-item-id db extension-id item-namespace)
                   (r subs/get-root-item-id    db extension-id item-namespace))))

(defn use-root-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (assoc-in db [extension-id :item-browser/meta-items :item-id]
               (r subs/get-root-item-id db extension-id item-namespace)))

(defn set-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (assoc-in db [extension-id :item-browser/meta-items :item-id] item-id))

(defn store-current-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:item-id (string)(opt)}}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (if (r subs/route-handled?     db extension-id item-namespace)
      (r derive-current-item-id! db extension-id item-namespace)
      (if-let [item-id (get browser-props :item-id)]
              (r set-current-item-id! db extension-id item-namespace item-id)
              (r use-root-item-id!    db extension-id item-namespace))))

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
           (r set-error-mode! db extension-id item-namespace))))

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
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (as-> db % (r reset-browser!         % extension-id item-namespace)
             (r store-current-item-id! % extension-id item-namespace browser-props)
             (r load-lister!           % extension-id item-namespace)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/load-browser!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) browser-props
  ;  {:item-id (string)(opt)}
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type {:item-id "my-item"}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      (let [browser-label (r subs/get-meta-item db extension-id item-namespace :label)]
           {:db (r load-browser! db extension-id item-namespace browser-props)
            :dispatch-n [; XXX#5660
                         [:environment/reg-keypress-listener! :item-browser/keypress-listener]
                         ; XXX#3237
                         (if (r subs/set-title? db extension-id item-namespace)
                             [:ui/set-title! browser-label])
                         [:item-browser/request-item! extension-id item-namespace]
                         (engine/load-extension-event extension-id item-namespace)
                         ; Ha az [:item-browser/load-browser! ...] esemény megtörténése előtt is
                         ; meg volt jelenítve az item-browser/body komponens és az infinite-loader
                         ; komponens a viewport területén volt, akkor szükséges az infinite-loader
                         ; komponenst újratölteni, hogy a megváltozott beállításokkal újratöltse
                         ; az adatokat.
                         [:tools/reload-infinite-loader! extension-id]]})))

(a/reg-event-fx
  :item-browser/use-filter!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-browser/use-filter! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace filter-pattern]]
      {:db (r use-filter! db extension-id item-namespace filter-pattern)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      ; - Az [:item-browser/browse-item! ...] esemény nem vizsglja, hogy az item-browser plugin
      ;   útvonala létezik-e.
      ; - Ha az aktuális útvonal az item-browser plugin útvonala, akkor átirányít a böngészendő
      ;   elem útvonalára.
      ; - Ha az aktuális útvonal NEM az item-browser plugin útvonala, akkor útvonal használata
      ;   nélkül indítja el az item-browser plugint, ezért lehetséges a plugint útvonalak
      ;   használata nélkül is elindítani, akkor is ha az item-browser plugin útvonalai léteznek.
      ;   Pl.: A plugin popup elemen való megjelenítése, útvonalak használata nélkül ...
      (if (r subs/route-handled? db extension-id item-namespace)
          ; If handled by route ...
          (let [browser-uri (engine/browser-uri extension-id item-namespace item-id)]
               [:router/go-to! browser-uri])
          ; If NOT handled by route ...
          [:item-browser/load-browser! extension-id item-namespace {:item-id item-id}])))

(a/reg-event-fx
  :item-browser/update-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ extension-id item-namespace]]))
      ;[:sync/send-query! :storage.media-browser/update-item!
      ;                   {:on-success [:item-lister/reload-items! :storage :media]
      ;                    :on-failure [:ui/blow-bubble! {:body :failed-to-rename}]
      ;                    :query (r queries/get-update-item-alias-query db media-item item-alias)]]))

(a/reg-event-fx
  :item-browser/delete-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/delete-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      [:sync/send-query! (engine/request-id extension-id item-namespace)
                         {;:display-progress? true
                          :on-success [:item-browser/item-deleted extension-id item-namespace item-id]
                          :on-failure [:ui/blow-bubble! {:body :failed-to-delete}]
                          :query      (r queries/get-delete-item-query db extension-id item-namespace item-id)}]))

(a/reg-event-fx
  :item-browser/duplicate-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/duplicate-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]))

(a/reg-event-fx
  :item-browser/move-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (string) destination-id
  ;
  ; @usage
  ;  [:item-browser/move-item! :my-extension :my-type "my-item" "your-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id destination-id]]))

(a/reg-event-fx
  :item-browser/go-home!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [root-item-id (r subs/get-root-item-id db extension-id item-namespace)]
           [:item-browser/browse-item! extension-id item-namespace root-item-id])))

(a/reg-event-fx
  :item-browser/go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-item-id (r subs/get-parent-item-id db extension-id item-namespace)]
           [:item-browser/browse-item! extension-id item-namespace parent-item-id])))

(a/reg-event-fx
  :item-browser/reload-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:dispatch-n [[:item-lister/reload-items!  extension-id item-namespace]
                    [:item-browser/request-item! extension-id item-namespace]]}))

(a/reg-event-fx
  :item-browser/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [db (r receive-item! db extension-id item-namespace server-response)]
           (if-let [item-label (r subs/get-item-label db extension-id item-namespace)]
                   {:db db :dispatch-if [(r subs/set-title? db extension-id item-namespace)
                                         [:ui/set-title! item-label]]}
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
  :item-browser/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys []} [_ extension-id item-namespace item-id]]))
