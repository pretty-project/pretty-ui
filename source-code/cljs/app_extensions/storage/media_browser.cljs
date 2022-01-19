
(ns app-extensions.storage.media-browser
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.css     :as css]
              [mid-fruits.format  :as format]
              [mid-fruits.io      :as io]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-media.api    :as media]
              [app-plugins.item-browser.api :as item-browser]))




;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage)]
           (case selected-option :upload-files!
                                 [:storage/load-file-uploader!     :storage/media-browser {:destination-id destination-id}]
                                 :create-directory!
                                 [:storage/load-directory-creator! :storage/media-browser {:destination-id destination-id}]))))



;; -- Media-item components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [items]}]
  (let [icon-family (if (vector/nonempty? items) :material-icons-filled :material-icons-outlined)]
       [:div.storage--media-item--header [elements/icon {:icon :folder
                                                         :icon-family icon-family
                                                         :size :xxl}]]))

(defn- directory-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [alias content-size]}]
  [:div.storage--media-item--details
    [elements/label {:content (str alias)
                     :layout :fit :selectable? true  :color :default}]
    [elements/label {:content (-> content-size io/B->MB format/decimals (str " MB"))
                     :layout :fit :selectable? false :color :muted
                     :font-size :xs}]
    [elements/label {:content "0 elem" :font-size :xs
                     :layout :fit :selectable? false :color :muted}]])

(defn- directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item-props]
  [:div.storage--media-item [directory-item-header  item-dex item-props]
                            [directory-item-details item-dex item-props]])

(defn- file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [filename]}]
  (let [preview-uri (media/filename->media-thumbnail-uri filename)]
       [:div.storage--media-item--preview {:style {:background-image (css/url preview-uri)}}]))

(defn- file-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias] :as item-props}]
  [:div.storage--media-item--header [elements/icon {:icon :insert_drive_file}]
                                    (if (io/filename->image? alias)
                                        [file-item-preview item-dex item-props])])

(defn- file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [alias filesize]}]
  [:div.storage--media-item--details
    [elements/label {:content (str alias)
                     :layout :fit :selectable? true  :color :default}]
    [elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
                     :layout :fit :selectable? false :color :muted :font-size :xs}]
    [elements/label {:content ""
                     :layout :fit :selectable? false :color :muted :font-size :xs}]])

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item-props]
  [:div.storage--media-item [file-item-header  item-dex item-props]
                            [file-item-details item-dex item-props]])
                           ;[file-item-actions item-dex item-props]

(defn- media-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [mime-type] :as item-props}]
  (case mime-type "storage/directory" [directory-item item-dex item-props]
                                      [file-item      item-dex item-props]))

(defn- media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item-props]
  [media-item-structure item-dex item-props])

(a/reg-event-fx
  :storage/->media-item-clicked
  (fn [{:keys [db]} [_ item-dex {:keys [id mime-type] :as item-props}]]
      (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                          [:storage/->file-clicked item-dex item-props])))

(a/reg-event-fx
  :storage/->file-clicked
  (fn [_ _]
      [:ui/add-popup! :xxxx
                      {:body [:div [:div "Fájl letöltése"]
                                   ; Ha több van kijelölve akkor:
                                   ; És mivel nincs tömörítönk ezért most még disabled lesz
                                   [:div "Kijelölt fájlok letöltése"]]
                       :min-width :xs}]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-browser/view :storage :media {:list-element     #'media-item
                                      :new-item-options [:upload-files! :create-directory!]
                                      :on-click         [:storage/->media-item-clicked]}])


  ;[app-extensions.storage.file-picker/view :my-picker {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :storage/render-media-browser!
  [:ui/set-surface! ::view {:view #'view}])

(a/reg-event-fx
  :storage/load-media-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n [[:storage/render-media-browser!]]}))
