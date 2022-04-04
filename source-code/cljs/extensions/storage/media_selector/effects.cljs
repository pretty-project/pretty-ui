
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.effects
    (:require [extensions.storage.media-selector.events :as media-selector.events]
              [extensions.storage.media-selector.subs   :as media-selector.subs]
              [extensions.storage.media-selector.views  :as media-selector.views]
              [plugins.item-browser.api                 :as item-browser]
              [x.app-core.api                           :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-selector/load-selector!
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ;  {:extensions (strings in vector)(opt)
  ;   :multiple? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [:storage.media-selector/load-selector! {...}]
  ;
  ; @usage
  ;  [:storage.media-selector/load-selector! :my-selector {...}]
  [a/event-vector<-id]
  ; A selector-id azonosító nincs felhasználva sehol, kizárólag az *-id & *-props formula
  ; egységes használata miatt adható meg.
  (fn [{:keys [db]} [_ selector-id selector-props]]
      {:db (r media-selector.events/load-selector! db selector-id selector-props)
       :dispatch [:storage.media-selector/render-selector!]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-selector/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-selector)
            load-props     {:browser-id :storage.media-selector :destination-id destination-id}]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    load-props]
                                 :create-directory! [:storage.directory-creator/load-creator! load-props]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-selector/save-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r media-selector.events/save-selected-items! db)
       :dispatch [:ui/close-popup! :storage.media-selector/view]}))

(a/reg-event-fx
  :storage.media-selector/file-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ file-item]]
      (let [db (r media-selector.events/toggle-file-selection! db file-item)]
           {:db db :dispatch-if [(r media-selector.subs/save-selected-items? db file-item)
                                 [:storage.media-selector/save-selected-items!]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-selector/render-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ selector-id]]
      [:ui/add-popup! :storage.media-selector/view
                      {:header [media-selector.views/header selector-id]
                       :body   [media-selector.views/body   selector-id]
                       :footer [media-selector.views/footer selector-id]
                       :stretch-orientation :vertical}]))
