
(ns app-extensions.storage.media-viewer.effects
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.media-viewer.events  :as events]
              [app-extensions.storage.media-viewer.queries :as queries]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
      {:db (r events/load-viewer! db viewer-id viewer-props)
       :dispatch-n [[:storage.media-viewer/render-viewer!          viewer-id]
                    [:storage.media-viewer/request-directory-item! viewer-id]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-viewer/request-directory-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ viewer-id]]
      [:sync/send-query! :storage.media-viewer/request-directory-item!
                         {:display-progress? true
                          :on-success [:storage.media-viewer/receive-directory-item! viewer-id]
                          :query      (r queries/get-request-directory-item-query db viewer-id)}]))
