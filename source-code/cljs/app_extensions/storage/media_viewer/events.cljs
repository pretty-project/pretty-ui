
(ns app-extensions.storage.media-viewer.events
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn viewer-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) viewer-props
  ;
  ; @return (map)
  [viewer-id {:keys [] :as viewer-props}]
  (merge {}
         (param viewer-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) viewer-props
  ;
  ; @return (map)
  [db [_ viewer-id viewer-props]]
  (assoc-in db [:storage :media-viewer/meta-items] viewer-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-viewer/load-viewer!
  ; @param (keyword)(opt) viewer-id
  ; @param (map) viewer-props
  ;  {:items-path (item-path vector)}
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! {...}]
  ;
  ; @usage
  ;  [:storage.media-viewer/load-viewer! :my-viewer {...}]
  ;
  ; @usage
  ;  (def db {:my-items [""]})
  ;  [:storage.media-viewer/load-viewer! {:item-paths [:my-items]}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ viewer-id viewer-props]]
      (let [viewer-props (viewer-props-prototype viewer-id viewer-props)]
           {:db (r load-viewer! db viewer-id viewer-props)
            :dispatch [:storage.media-viewer/render-viewer! viewer-id]})))
