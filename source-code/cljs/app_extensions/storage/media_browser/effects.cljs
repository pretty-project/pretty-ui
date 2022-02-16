
(ns app-extensions.storage.media-browser.effects
    (:require [app-fruits.window :as window]
              [mid-fruits.candy  :refer [param return]]
              [x.app-core.api    :as a :refer [r]]
              [x.app-media.api   :as media]
              [x.app-router.api  :as router]
              [x.app-ui.api      :as ui]
              [app-extensions.storage.media-browser.queries :as queries]
              [app-extensions.storage.media-browser.subs    :as subs]
              [app-plugins.item-browser.api                 :as item-browser]))



;; ----------------------------------------------------------------------------
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



;; -- Media-item effect events ------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage)]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    {:destination-id destination-id}]
                                 :create-directory! [:storage.directory-creator/load-creator! {:destination-id destination-id}]))))



;; -- Directory-item effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/open-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                    [:item-browser/browse-item! :storage :media id]]}))

(a/reg-event-fx
  :storage.media-browser/copy-directory-link!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ {:keys [id]}]]
      (let [directory-uri (item-browser/browser-uri :storage :media id)
            directory-uri (r router/get-resolved-uri db directory-uri)
            uri-base      (window/get-uri-base)]
           {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                         [:tools/copy-to-clipboard! (str uri-base directory-uri)]]})))

(a/reg-event-fx
  :storage.media-browser/rename-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ directory-item]]
      {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                    [:storage.media-browser/render-rename-directory-dialog! directory-item]]}))

(a/reg-event-fx
  :storage.media-browser/delete-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [cofx [_ {:keys [id]}]]
      {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                    [:item-browser/delete-item! :storage :media id]]}))



;; -- File-item effect events -------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/preview-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ {:keys [filename]}]]
      (let [directory-id (r item-browser/get-current-item-id db :storage)]
           {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                         [:storage.media-viewer/load-viewer! {:directory-id directory-id :current-item filename}]]})))

(a/reg-event-fx
  :storage.media-browser/download-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ {:keys [alias filename]}]]
      {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                    [:tools/save-file! {:filename alias :uri (media/filename->media-storage-uri filename)}]]}))

(a/reg-event-fx
  :storage.media-browser/copy-file-link!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [filename]}]]
      (let [file-uri (media/filename->media-storage-uri filename)
            uri-base (window/get-uri-base)]
           {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                         [:tools/copy-to-clipboard! (str uri-base file-uri)]]})))

(a/reg-event-fx
  :storage.media-browser/rename-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ file-item]]
      {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                    [:storage.media-browser/render-rename-file-dialog! file-item]]}))

(a/reg-event-fx
  :storage.media-browser/move-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ file-item]]
      {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                    [:storage.media-browser/render-target-selector!]]}))

(a/reg-event-fx
  :storage.media-browser/duplicate-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ file-item]]
      {:dispatch-n [[:ui/close-popup! :storage.media-browser/media-menu]
                    [:sync/send-query! :storage.media-browser/duplicate-media-item!
                                       {;:display-progress? true
                                        :on-success [:item-browser/reload-items! :storage :media]
                                        :on-failure [:ui/blow-bubble! {:body :failed-to-copy}]}]]}))
                                        ;:query (r queries/get-duplicate-item-query db file-item)}]]}))

(a/reg-event-fx
  :storage.media-lister/item-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ _ {:keys [id mime-type] :as media-item}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          (if (r subs/media-browser-mode? db)
                                              [:storage.media-browser/render-file-menu! media-item]
                                              [:storage.media-picker/file-clicked       media-item]))))

(a/reg-event-fx
  :storage.media-lister/item-right-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ _ {:keys [mime-type] :as media-item}]]
      (if (r subs/media-browser-mode? db)
          (case mime-type "storage/directory" [:storage.media-browser/render-directory-menu! media-item]
                                              [:storage.media-browser/render-file-menu!      media-item]))))
