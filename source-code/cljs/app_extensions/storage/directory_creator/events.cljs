
(ns app-extensions.storage.directory-creator.events
    (:require [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a :refer [r]]
              [app-extensions.storage.directory-creator.queries :as queries]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-creator-props!
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
  :storage.directory-creator/create-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id directory-name]]
      [:sync/send-query! :storage.directory-creator/create-directory!
                         {:query (r queries/get-create-directory-query db creator-id directory-name)
                          :on-success [:item-lister/reload-items! :storage :media]}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/load-creator!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) creator-id
  ; @param (map) creator-props
  ;  {:destination-id (string)}
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! {...}]
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! :my-creator {...}]
  ;
  ; @usage
  ;  [:storage.directory-creator/load-creator! {:destination-id "..."}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ creator-id creator-props]]
      {:db (r store-creator-props! db creator-id creator-props)
       :dispatch [:storage.directory-creator/render-dialog! creator-id]}))
