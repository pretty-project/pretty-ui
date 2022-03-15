
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.prototypes
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.time                   :as time]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-sync.request-handler.config :as request-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-props<-source-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Ha a (POST) request tartalmazza a :source-path tulajdonságot, akkor
  ; hozzáfűzi a request-hez paraméterként a :source-path Re-Frame adatbázis
  ; útvonalon található adatot.
  ;
  ; @param (map) request-props
  ;  {:source-path (vector)(opt)}
  ;
  ; @return (map)
  ;  {:params (map)
  ;   {:source (*)}}
  [db [_ {:keys [source-path] :as request-props}]]
  (if source-path (assoc-in request-props [:params :source] (get-in db source-path))
                  (return   request-props)))

(defn request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request-props
  ;
  ; @return (map)
  ;  {:response-action (keyword)
  ;   :sent-time (object)
  ;   :timeout (integer)}
  [db [_ {:keys [response-action] :as request-props}]]
  (merge {:response-action :store
          :error-handler-f    (fn [request-id server-response] (a/dispatch [:sync/request-failured      request-id server-response]))
          :handler-f          (fn [request-id server-response] (a/dispatch [:sync/request-successed     request-id server-response]))
          :progress-handler-f (fn [request-id server-response] (a/dispatch [:core/set-process-progress! request-id server-response]))
          :sent-time          (time/timestamp-string)
          :timeout request-handler.config/DEFAULT-REQUEST-TIMEOUT}
         (r request-props<-source-data db request-props)))
