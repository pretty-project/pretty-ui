
(ns app-extensions.storage.media-viewer.events
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]
              [app-extensions.storage.media-viewer.queries :as queries]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn step-bwd!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]])

(defn step-fwd!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]])

(defn store-directory-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id {:media/keys [id] :as directory-item}]]
  (assoc-in db [:storage :media-viewer/data-items id] directory-item))

(defn receive-directory-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id server-response]]
  (let [directory-item (get server-response :storage.media-viewer/get-directory-item)]
       (r store-directory-item! db viewer-id directory-item)))

(a/reg-event-db :storage.media-viewer/receive-directory-item! receive-directory-item!)

(defn load-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id viewer-props]]
  (assoc-in db [:storage :media-viewer/meta-items] viewer-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-viewer/request-directory-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ viewer-id]]
      [:sync/send-query! :storage.media-viewer/request-directory-item!
                         {:display-progress? true
                          :on-success [:storage.media-viewer/receive-directory-item! viewer-id]
                          :query      (r queries/get-request-directory-item-query db viewer-id)}]))

(a/reg-event-fx
  :storage.media-viewer/load-viewer!
  ; @param (keyword)(opt) viewer-id
  ; @param (map) viewer-props
  ;  {:current-item (string)(opt)
  ;   :directory-id (string)}
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! {...}]
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! :my-viewer {...}]
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! {:current-item "my-image.png"
  ;                                       :directory-id "my-directory"}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ viewer-id viewer-props]]
      {:db (r load-viewer! db viewer-id viewer-props)
       :dispatch-n [[:storage.media-viewer/render-viewer!          viewer-id]
                    [:storage.media-viewer/request-directory-item! viewer-id]]}))
