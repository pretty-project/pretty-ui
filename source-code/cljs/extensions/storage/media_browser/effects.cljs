
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-browser.effects
    (:require [app-fruits.window                      :as window]
              [extensions.storage.media-browser.views :as media-browser.views]
              [plugins.item-browser.api               :as item-browser]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-media.api                        :as media]
              [x.app-router.api                       :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:storage.media-browser/render-browser!])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/create-directory!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-browser)]
           [:storage.directory-creator/load-creator! {:browser-id :storage.media-browser :destination-id destination-id}])))

(a/reg-event-fx
  :storage.media-browser/upload-files!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-browser)]
           [:storage.file-uploader/load-uploader! {:browser-id :storage.media-browser :destination-id destination-id}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-browser)
            load-props     {:browser-id :storage.media-browser :destination-id destination-id}]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    load-props]
                                 :create-directory! [:storage.directory-creator/load-creator! load-props]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                    [:item-browser/delete-item! :storage.media-browser id]]}))

(a/reg-event-fx
  :storage.media-browser/duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                    [:item-browser/duplicate-item! :storage.media-browser id]]}))

(a/reg-event-fx
  :storage.media-browser/rename-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ media-item]]
      {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                    [:storage.alias-editor/load-editor! media-item]]}))

(a/reg-event-fx
  :storage.media-browser/move-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ media-item]]
      {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]]}))



;; -- Directory-item effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/open-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                    [:item-browser/browse-item! :storage.media-browser id]]}))

(a/reg-event-fx
  :storage.media-browser/copy-directory-link!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ {:keys [id]}]]
      (let [directory-uri (r item-browser/get-item-route db :storage.media-browser id)
            directory-uri (r router/use-app-home         db directory-uri)
            uri-base      (window/get-uri-base)]
           {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                         [:tools/copy-to-clipboard! (str uri-base directory-uri)]]})))



;; -- File-item effect events -------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/preview-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ {:keys [filename]}]]
      (let [directory-id (r item-browser/get-current-item-id db :storage)]
           {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                         [:storage.media-viewer/load-viewer! {:directory-id directory-id :current-item filename}]]})))

(a/reg-event-fx
  :storage.media-browser/download-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ {:keys [alias filename]}]]
      {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                    [:tools/save-file! {:filename alias :uri (media/filename->media-storage-uri filename)}]]}))

(a/reg-event-fx
  :storage.media-browser/copy-file-link!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [filename]}]]
      (let [file-uri (media/filename->media-storage-uri filename)
            uri-base (window/get-uri-base)]
           {:dispatch-n [[:ui/close-popup! :storage.media-menu/view]
                         [:tools/copy-to-clipboard! (str uri-base file-uri)]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :storage.media-browser/render-browser!
  [:ui/render-surface! :storage.media-browser/view
                       {:content #'media-browser.views/view}])
