
(ns app-extensions.storage.media-browser.directory-creator
    (:require [mid-fruits.io        :as io]
              [mid-fruits.keyword   :as keyword]
              [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-create-directory-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ creator-id directory-name]]
  (let [destination-id (get-in db [:storage :directory-creator/meta-items :destination-id])]
       [:debug `(storage/create-directory! ~{:destination-id destination-id :alias directory-name})]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-creator-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) creator-id
  ; @param (map) creator-props
  ;
  ; @return (map)
  [db [_ _ creator-props]]
  (assoc-in db [:storage :directory-creator/meta-items] creator-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/create-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id directory-name]]
      [:sync/send-query! (keyword/add-namespace :storage creator-id)
                         {:query (r get-create-directory-query db creator-id directory-name)
                          :on-success [:item-lister/reload-lister! :storage :media]}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-browser/render-directory-name-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id]]
      [:value-editor/load-editor! :storage :directory-name
                                  {:label :directory-name :save-button-label :create!
                                   :initial-value (r dictionary/look-up db :new-directory)
                                   :on-save       [:storage/create-directory! creator-id]
                                   :validator {:f               io/directory-name-valid?
                                               :invalid-message :invalid-directory-name
                                               :pre-validate?   true}}]))

(a/reg-event-fx
  :storage.media-browser/load-directory-creator!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) creator-id
  ; @param (map) creator-props
  ;  {:destination-id (string)}
  ;
  ; @usage
  ;  [:storage.media-browser/load-directory-creator! {...}]
  ;
  ; @usage
  ;  [:storage.media-browser/load-directory-creator! :my-uploader {...}]
  ;
  ; @usage
  ;  [:storage.media-browser/load-directory-creator! {:destination-id "..."}]
  (fn [{:keys [db]} event-vector]
      (let [creator-id    (a/event-vector->second-id   event-vector)
            creator-props (a/event-vector->first-props event-vector)]
           {:db (r store-creator-props! db creator-id creator-props)
            :dispatch [:storage.media-browser/render-directory-name-dialog! creator-id]})))
