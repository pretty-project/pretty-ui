
(ns app-extensions.storage.directory-browser
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
  (fn [_ [_ selected-option]]
      (case selected-option :upload-files!     [:storage/load-file-uploader!]
                            :create-directory! [:storage/render-new-directory-name-dialog!])))



;; -- File-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [id] :as file-props}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-browser/view :storage :directory {:list-element     #'file-item
                                          :new-item-options [:upload-files! :create-directory!]}])


  ;[app-extensions.storage.file-picker/view :my-picker {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/load-directory-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/set-surface! ::view {:view {:content #'view}}]
                [:sync/send-query! :storage/synchronize-directory-browser!
                                   {:query      [:debug `(:storage/download-capacity-details ~{})]
                                    :on-success [:storage/receive-server-response!]}]]})
