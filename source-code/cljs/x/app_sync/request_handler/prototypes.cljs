
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.prototypes
    (:require [mid-fruits.candy                  :refer [param]]
              [mid-fruits.time                   :as time]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-sync.request-handler.config :as request-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request-props
  ;
  ; @return (map)
  ;  {:error-handler-f (function)
  ;   :handler-f (function)
  ;   :idle-timeout (ms)
  ;   :progress-handler-f (function)
  ;   :response-action (keyword)
  ;   :sent-time (string)
  ;   :timeout (ms)}
  [db [_ {:keys [response-action source-path] :as request-props}]]
  ; Ha a (POST) request tartalmazza a :source-path tulajdonságot, akkor hozzáfűzi a request-hez
  ; paraméterként a {:source-path [...]} Re-Frame adatbázis útvonalon található adatot.
  (let [request-props (merge {:idle-timeout    request-handler.config/DEFAULT-IDLE-TIMEOUT
                              :params          (if source-path {:source (get-in db source-path)})
                              :response-action :store
                              :sent-time       (time/timestamp-string)
                              :timeout         request-handler.config/DEFAULT-REQUEST-TIMEOUT}
                             (param request-props))
        error-handler-f    (fn [request-id server-response] (a/dispatch [:sync/request-failured      request-id request-props server-response]))
        handler-f          (fn [request-id server-response] (a/dispatch [:sync/request-successed     request-id request-props server-response]))
        progress-handler-f (fn [request-id server-response] (a/dispatch [:core/set-process-progress! request-id request-props server-response]))]
       (merge request-props {:error-handler-f error-handler-f :handler-f handler-f :progress-handler-f progress-handler-f})))
