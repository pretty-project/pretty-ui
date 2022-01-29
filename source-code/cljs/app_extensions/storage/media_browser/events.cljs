
(ns app-extensions.storage.media-browser.events
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-ui.api     :as ui]
              [app-extensions.storage.media-browser.subs :as subs]
              [app-plugins.item-browser.api              :as item-browser]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage)]
           (case selected-option :upload-files!     [:storage.file-uploader/load-uploader!    {:destination-id destination-id}]
                                 :create-directory! [:storage.directory-creator/load-creator! {:destination-id destination-id}]))))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->file-clicked
  [_ _]
  [:ui/add-popup! :xxxx
                  {:body [:div [:div "Fájl letöltése"]
                               ; Ha több van kijelölve akkor:
                               ; És mivel nincs tömörítönk ezért most még disabled lesz
                               [:div "Kijelölt fájlok letöltése"]]
                   :min-width :xs}])

(a/reg-event-fx
  :storage.media-lister/->item-clicked
  (fn [{:keys [db] :as cofx} [_ item-dex {:keys [id mime-type] :as item}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          (if (r subs/media-browser-mode? db)
                                              (r ->file-clicked                 cofx item-dex item)
                                              [:storage.media-picker/->file-clicked  item-dex item]))))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/load-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r subs/media-browser-mode? db)
          (if-not (r ui/element-rendered? db :surface :storage.media-browser/view)
                  [:storage.media-browser/render-browser!])
          (if-not (r ui/element-rendered? db :popups :storage.media-picker/view)
                  [:storage.media-picker/render-picker!]))))
