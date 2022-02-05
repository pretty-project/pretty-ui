
(ns app-extensions.storage.media-browser.events
    (:require [app-fruits.window :as window]
              [mid-fruits.candy  :refer [param return]]
              [x.app-core.api    :as a :refer [r]]
              [x.app-media.api   :as media]
              [x.app-ui.api      :as ui]
              [app-extensions.storage.media-browser.dialogs :as dialogs]
              [app-extensions.storage.media-browser.subs    :as subs]
              [app-plugins.item-browser.api                 :as item-browser]))




; MONGO-DB / group stage (mappák után fájlok)





;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage)]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    {:destination-id destination-id}]
                                 :create-directory! [:storage.directory-creator/load-creator! {:destination-id destination-id}]))))

(a/reg-event-fx
  :storage.media-browser/copy-file-link!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [filename]}]]
      (let [file-uri (media/filename->media-storage-uri filename)
            uri-base (window/get-uri-base)]
           {:dispatch-n [[:ui/close-popup! :storage.media-browser/file-menu]
                         [:tools/copy-to-clipboard! (str uri-base file-uri)]]})))

(a/reg-event-fx
  :storage.media-browser/preview-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ {:keys [filename]}]]
      (let [directory-id (r item-browser/get-current-item-id db :storage)]
           [:storage.media-viewer/load-viewer! {:directory-id directory-id
                                                :current-item filename}])))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-lister/->item-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db] :as cofx} [_ item-dex {:keys [id mime-type] :as item}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          (if (r subs/media-browser-mode? db)
                                              (r dialogs/render-file-menu!     cofx item)
                                              [:storage.media-picker/->file-clicked item-dex item]))))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r subs/media-browser-mode? db) ; XXX#7157
          (if-not (r ui/element-rendered? db :surface :storage.media-browser/view)
                  [:storage.media-browser/render-browser!])
          (if-not (r ui/element-rendered? db :popups :storage.media-picker/view)
                  [:storage.media-picker/render-picker!]))))
