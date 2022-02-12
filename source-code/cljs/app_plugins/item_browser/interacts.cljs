
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.interacts
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.item-browser.engine  :as engine]
              [app-plugins.item-browser.queries :as queries]
              [app-plugins.item-browser.subs    :as subs]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  :item-browser/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys []} [_ extension-id item-namespace item-id]]))
