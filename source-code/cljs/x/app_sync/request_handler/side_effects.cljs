
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.side-effects
   (:require [app-fruits.http :as http]
             [x.app-core.api  :as a]
             [x.app-sync.request-handler.engine :as request-handler.engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:error-handler-event (event-id)(opt)
  ;   :handler-event (event-id)(opt)
  ;   :progress-handler-event (event-id)(opt)}
  ;
  ; @return (map)
  ;  {:error-handler (function)
  ;   :handler (function)
  ;   :progress-handler (function)}
  [request-id {:keys [error-handler-event handler-event progress-handler-event] :as request-props}]
  (letfn [(error-handler-f    [request-id server-response] (a/dispatch [error-handler-event    request-id server-response]))
          (handler-f          [request-id server-response] (a/dispatch [handler-event          request-id server-response]))
          (progress-handler-f [request-id server-response] (a/dispatch [progress-handler-event request-id server-response]))]
         (cond-> (select-keys request-props [:body :method :params :timeout :uri])
                 error-handler-event    (assoc :error-handler    error-handler-f)
                 handler-event          (assoc :handler          handler-f)
                 progress-handler-event (assoc :progress-handler progress-handler-f))))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- send-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:body (*)(opt)
  ;   :method (keyword)
  ;    :post, :get
  ;   :error-handler-event (event-id)(opt)
  ;   :handler-event (event-id)(opt)
  ;   :params (map)(opt)
  ;    Only w/ {:method :post}
  ;   :progress-handler-event (event-id)(opt)
  ;    Only w/ {:method :post}
  ;   :timeout (ms)(opt)
  ;   :uri (string)}
  [request-id request-props]
  (let [request-props (request-props-prototype request-id request-props)
        reference     (http/send-request!      request-id request-props)]
       (swap! request-handler.engine/REFERENCES assoc request-id reference)))

(defn- abort-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  [request-id]
  (let [reference (get @request-handler.engine/REFERENCES request-id)]
       (http/abort-request! reference)))

(defn- remove-reference!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  [request-id]
  (swap! request-handler.engine/REFERENCES dissoc request-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :sync/send-request! send-request!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :sync/abort-request! abort-request!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :sync/remove-reference! remove-reference!)
