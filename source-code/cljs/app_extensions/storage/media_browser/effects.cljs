
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-browser.effects
    (:require [app-fruits.window :as window]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.io     :as io]
              [x.app-core.api    :as a :refer [r]]
              [x.app-media.api   :as media]
              [x.app-router.api  :as router]
              [x.app-ui.api      :as ui]
              [app-extensions.storage.media-browser.queries :as media-browser.queries]
              [app-extensions.storage.media-browser.subs    :as media-browser.subs]
              [app-extensions.storage.media-browser.views   :as media-browser.views]
              [app-plugins.item-browser.api                 :as item-browser]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r media-browser.subs/media-browser-mode? db) ; XXX#7157
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

(a/reg-event-fx
  :storage.media-browser/delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [;[:ui/close-popup! :storage.media-browser/media-menu]
                    [:item-browser/delete-item! :storage :media id]]}))

(a/reg-event-fx
  :storage.media-browser/duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [id]}]]
      {:dispatch-n [;[:ui/close-popup! :storage.media-browser/media-menu]
                    [:item-browser/duplicate-item! :storage :media id]]}))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/item-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ item-dex {:keys [id mime-type] :as media-item}]]
      (if (r item-browser/toggle-item-selection? db :storage :media item-dex)
          {:db (r item-browser/toggle-item-selection! db :storage :media item-dex)}
          (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                              [:storage.media-browser/render-file-menu! media-item]))))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :storage.media-browser/render-browser!
  [:ui/set-surface! :storage.media-browser/view
                    {:view #'media-browser.views/view}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/render-rename-directory-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [alias] :as directory-item}]]
      [:value-editor/load-editor! :storage :file-name
                                  {:label :directory-name :save-button-label :rename! :initial-value alias
                                   :on-save   [:storage.media-browser/update-item-alias! directory-item]
                                   :validator {:f io/directory-name-valid?
                                               :invalid-message :invalid-directory-name
                                               :pre-validate?   true}}]))

(a/reg-event-fx
  :storage.media-browser/render-rename-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ {:keys [alias] :as file-item}]]
      [:value-editor/load-editor! :storage :file-name
                                  {:label :file-name :save-button-label :rename! :initial-value alias
                                   :on-save   [:storage.media-browser/update-item-alias! file-item]
                                   :validator {:f io/filename-valid?
                                               :invalid-message :invalid-file-name
                                               :pre-validate?   true}}]))

(a/reg-event-fx
  :storage.media-browser/render-directory-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ directory-item]]
      [:ui/add-popup! :storage.media-browser/media-menu
                      {:body   [media-browser.views/directory-menu-body directory-item]
                       :header [media-browser.views/media-menu-header   directory-item]
                       :horizontal-align :left
                       :min-width        :xs}]))

(a/reg-event-fx
  :storage.media-browser/render-file-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ file-item]]
      [:ui/add-popup! :storage.media-browser/media-menu
                      {:body   [media-browser.views/file-menu-body    file-item]
                       :header [media-browser.views/media-menu-header file-item]
                       :horizontal-align :left
                       :min-width        :xs}]))
