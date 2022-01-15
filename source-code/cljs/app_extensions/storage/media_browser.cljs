
(ns app-extensions.storage.media-browser
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
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

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias id] :as item-props}]
  [:div (str alias)])



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
