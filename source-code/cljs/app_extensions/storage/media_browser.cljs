
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

;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/add-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ selected-option]]
      (let [directory-id (r item-browser/get-current-item-id db :storage)]
           (case selected-option :upload-files!     [:storage/load-file-uploader! {:directory-id directory-id}]
                                 :create-directory! []))))



;; -- File-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias] :as item-props}]
  [:div "" alias])

(defn- file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;  {:filename (string)
  ;   :object-url (string)}
  ;
  ; @return (component)
  [_ {:keys [alias filename]}]
  (if (io/filename->image? alias)
      [:div.storage--file-uploader--file-preview {:style {:background-image (-> filename media/filename->media-storage-uri css/url)}}]
      [:div.storage--file-uploader--file-preview [elements/icon {:icon :insert_drive_file}]]))

(defn- file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;  {:filename (string)
  ;   :filesize (B)}
  ;
  ; @return (component)
  [_ {:keys [alias filesize]}]
  [:div.storage--file-uploader--file-details
    [elements/label {:content (str alias)
                     :layout :fit :selectable? true  :color :default}]
    [elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
                     :layout :fit :selectable? false :color :muted}]])

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item-props]
  [elements/row {:content [:<> [file-item-preview item-dex item-props]
                               [file-item-details item-dex item-props]]}])
                              ;[file-item-actions item-dex item-props]

(defn- media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [mime-type] :as item-props}]
  (case mime-type :storage/directory [directory-item item-dex item-props]
                                     [file-item      item-dex item-props]))



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
  {:dispatch-n [[:ui/set-surface! ::view {:view {:content #'view}}]
                [:sync/send-query! :storage/synchronize-media-browser!
                                   {:query      [:debug `(:storage/download-capacity-details ~{})]
                                    :on-success [:storage/receive-server-response!]}]]})
