
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.19
; Description:
; Version: v1.0.8
; Compatibility: x4.2.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.http
   (:require [ajax.core        :refer [GET POST]]
             [mid-fruits.candy :refer [param]]
             [mid-fruits.math  :as math]))



;; -- Helpers --------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- progress-event->request-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (object) progress-event
  ;
  ; @return (integer)
  [progress-event]
  (let [loaded (.-loaded progress-event)
        total  (.-total  progress-event)]
       (math/percent total loaded)))

(defn- request-handlers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:error-handler (function)(opt)
  ;   :handler (function)(opt)
  ;   :progress-handler (function)(opt)}
  ;
  ; @return (map)
  ;  {:error-handler (function)
  ;   :handler (function)
  ;   :progress-handler (function)}
  [request-id {:keys [error-handler handler progress-handler]}]
  (cond-> {} (some? error-handler)
             (assoc :error-handler    (fn [server-response]
                                          (error-handler request-id server-response)))
             (some? handler)
             (assoc :handler          (fn [server-response]
                                          (handler request-id server-response)))
             (some? progress-handler)
             (assoc :progress-handler (fn [progress-event]
                                          (let [request-progress (progress-event->request-progress progress-event)]
                                               (progress-handler request-id request-progress))))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:uri (string)}
  ;
  ; @return (map)
  ;  {:error-handler (function)
  ;   :handler (function)
  ;   :progress-handler (function)
  ;   :uri (string)}
  [request-id request-props]
  (merge (request-handlers request-id request-props)
         (select-keys      request-props [:uri])))

(defn- post-request-props-prototype
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
  ;   :error-handler (function)
  ;   :handler (function)
  ;   :params (map)
  ;   :progress-handler (function)
  ;   :uri (string)}
  [request-id request-props]
  (merge (request-handlers request-id request-props)
         (select-keys      request-props [:body :params :uri])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-request!
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:error-handler (function)(opt)
  ;   :handler (function)(opt)
  ;   :method (keyword)
  ;    :get, :post
  ;   :params (map)(opt)
  ;    Only w/ {:method :post}
  ;   :progress-handler (function)(opt)
  ;    Only w/ {:method :post}
  ;   :timeout (ms)(opt)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (defn my-handler [request-id server-response])
  ;  (http/send-request! :my-request {:handler my-handler
  ;                                   :method :post
  ;                                   :uri "/my-uri"})
  [request-id {:keys [method uri] :as request-props}]
  (if (= method :get)
      (GET  uri (get-request-props-prototype  request-id request-props))
      (POST uri (post-request-props-prototype request-id request-props))))
