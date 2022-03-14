
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns ajax.prototypes
    (:require [ajax.helpers :as helpers]
              [ajax.state   :as state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-handler-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:error-handler-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [error-handler-f]}]
  (if error-handler-f (fn [server-response]
                          (swap! state/REQUESTS dissoc request-id)
                          (error-handler-f request-id server-response))))

(defn handler-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:handler-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [handler-f]}]
  (if handler-f (fn [server-response]
                    (swap! state/REQUESTS dissoc request-id)
                    (handler-f request-id server-response))))

(defn progress-handler-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:progress-handler-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [progress-handler-f]}]
  (if progress-handler-f (fn [progress-event]
                             (let [request-progress (helpers/progress-event->request-progress progress-event)]
                                  (progress-handler-f request-id request-progress)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-handlers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;
  ; @return (map)
  ;  {:error-handler-f (function)
  ;   :handler-f (function)
  ;   :progress-handler-f (function)}
  [request-id request-props]
  {:error-handler-f    (error-handler-f    request-id request-props)
   :handler-f          (handler-f          request-id request-props)
   :progress-handler-f (progress-handler-f request-id request-props)})

(defn GET-request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:uri (string)}
  ;
  ; @return (map)
  ;  {:error-handler-f (function)
  ;   :handler-f (function)
  ;   :progress-handler-f (function)
  ;   :uri (string)}
  [request-id request-props]
  (merge (request-handlers request-id request-props)
         (select-keys request-props [:uri])))

(defn POST-request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:body (*)
  ;   :params (map)
  ;   :uri (string)}
  ;
  ; @return (map)
  ;  {:body (*)
  ;   :error-handler-f (function)
  ;   :handler-f (function)
  ;   :params (map)
  ;   :progress-handler-f (function)
  ;   :uri (string)}
  [request-id request-props]
  (merge (request-handlers request-id request-props)
         (select-keys request-props [:body :params :uri])))
