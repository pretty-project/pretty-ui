
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-picker.effects
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.media-picker.events :as media-picker.events]
              [app-extensions.storage.media-picker.subs   :as media-picker.subs]
              [app-extensions.storage.media-picker.views  :as media-picker.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/load-picker!
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:value-path (item-path vector)}
  ;
  ; @usage
  ;  [:storage.media-picker/load-picker! {...}]
  ;
  ; @usage
  ;  [:storage.media-picker/load-picker! :my-picker {...}]
  [a/event-vector<-id]
  ; - A picker-id azonosító nincs felhasználva sehol, kizárólag az *-id & *-props formula
  ;   egységes használata miatt adható meg.
  ; - XXX#7157
  ;   A media-picker egy popup elemen megjelenített átalakított media-browser, aminek az indítása
  ;   az [:item-browser/load-browser! ...] eseménnyel történik, ami elindítja a media-browser eszközt,
  ;   ami felismeri, hogy nem egy útvonal alapján lett elindítva és ezért a media-picker eszközt
  ;   rendereli ki.
  (fn [{:keys [db]} [_ picker-id picker-props]]
      {:db (r media-picker.events/load-picker! db picker-id picker-props)
       :dispatch [:item-browser/load-browser! :storage :media]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/save-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r media-picker.events/save-selected-items! db)
       :dispatch [:ui/close-popup! :storage.media-picker/view]}))

(a/reg-event-fx
  :storage.media-picker/file-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ file-item]]
      (let [db (r media-picker.events/toggle-file-selection! db file-item)]
           {:db db :dispatch-if [(r media-picker.subs/save-selected-items? db file-item)
                                 [:storage.media-picker/save-selected-items!]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/render-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ picker-id]]
      [:ui/add-popup! :storage.media-picker/view
                      {:body   [media-picker.views/body   picker-id]
                       :header [media-picker.views/header picker-id]
                       :stretch-orientation :vertical}]))
