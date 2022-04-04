
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.effects
    (:require [extensions.storage.media-picker.events :as media-picker.events]
              [extensions.storage.media-picker.subs   :as media-picker.subs]
              [extensions.storage.media-picker.views  :as media-picker.views]
              [plugins.item-browser.api               :as item-browser]
              [x.app-core.api                         :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/load-picker!
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:extensions (strings in vector)(opt)
  ;   :multiple? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [:storage.media-picker/load-picker! {...}]
  ;
  ; @usage
  ;  [:storage.media-picker/load-picker! :my-picker {...}]
  [a/event-vector<-id]
  ; A picker-id azonosító nincs felhasználva sehol, kizárólag az *-id & *-props formula
  ; egységes használata miatt adható meg.
  (fn [{:keys [db]} [_ picker-id picker-props]]
      {:db (r media-picker.events/load-picker! db picker-id picker-props)
       :dispatch [:storage.media-picker/render-picker!]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-picker)
            load-props     {:browser-id :storage.media-picker :destination-id destination-id}]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    load-props]
                                 :create-directory! [:storage.directory-creator/load-creator! load-props]))))



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
                      {:header [media-picker.views/header picker-id]
                       :body   [media-picker.views/body   picker-id]
                       :footer [media-picker.views/footer picker-id]
                       :stretch-orientation :vertical}]))
