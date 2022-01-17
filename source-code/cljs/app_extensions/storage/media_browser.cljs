
(ns app-extensions.storage.media-browser
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.css     :as css]
              [mid-fruits.format  :as format]
              [mid-fruits.io      :as io]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-media.api    :as media]
              [app-plugins.item-browser.api :as item-browser]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-load-media-browser-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [directory-id (r item-browser/get-current-item-id db :storage)]
       [:debug `(:storage/download-capacity-details  ~{})
               `(:storage/download-directory-details ~{:directory-id directory-id})]))



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
  [_ _]
  [:div.storage--media-item--header [elements/icon {:icon :folder}]])

(defn- directory-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [alias content-size]}]
  [:div.storage--media-item--details
    [elements/label {:content (str alias)
                     :layout :fit :selectable? true  :color :default}]
    [elements/label {:content (-> content-size io/B->MB format/decimals (str " MB"))
                     :layout :fit :selectable? false :color :muted}]])

(defn- directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item-props]
  [:div.storage--media-item [directory-item-header  item-dex item-props]
                            [directory-item-details item-dex item-props]])

(defn- file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [filename]}]
  (let [preview-uri (media/filename->media-storage-uri filename)]
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
                     :layout :fit :selectable? false :color :muted}]])

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
  [item-dex {:keys [id mime-type] :as item-props}]
  [elements/toggle {:content [media-item-structure item-dex item-props]
                    :hover-color :highlight
                    :on-click (case mime-type "storage/directory" [:item-browser/browse-item! :storage :media id]
                                                                  [:xx])}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-browser/view :storage :media {:list-element     #'media-item
                                      :new-item-options [:upload-files! :create-directory!]}])


  ;[app-extensions.storage.file-picker/view :my-picker {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/load-media-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-n [[:ui/set-surface! ::view {:view {:content #'view}}]
                    [:sync/send-query! (item-browser/request-id :storage :media)
                                       {:query      (r get-load-media-browser-query db)
                                        :on-success [:storage/receive-server-response!]}]]}))
