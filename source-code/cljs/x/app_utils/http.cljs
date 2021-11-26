
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.13
; Description:
; Version: v1.2.4
; Compatibility: x4.2.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-utils.http
   (:require [app-fruits.http :as http]
             [mid-fruits.map  :as map]
             [x.app-core.api  :as a]))



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
  (cond-> (map/inherit request-props [:body :method :params :timeout :uri])
          (some? error-handler-event)
          (assoc :error-handler
                 (fn [request-id server-response]
                     (a/dispatch [error-handler-event request-id server-response])))
          (some? handler-event)
          (assoc :handler
                 (fn [request-id server-response]
                     (a/dispatch [handler-event request-id server-response])))
          (some? progress-handler-event)
          (assoc :progress-handler
                 (fn [request-id server-response]
                     (a/dispatch [progress-handler-event request-id server-response])))))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-request!
  ; @param (keyword)(opt) request-id
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
  ;
  ; @usage
  ;  (http/send-request! {...})
  ;
  ; @usage
  ;  (http/send-request! :my-request {...})
  ;
  ; @usage
  ;  (http/send-request! {:method :get
  ;                       :uri    "/sample-uri"})
  ([request-props]
   (send-request! nil request-props))

  ([request-id request-props]
   (let [request-id    (a/id   request-id)
         request-props (a/prot request-id request-props request-props-prototype)]
        (http/send-request! request-id request-props))))

; @usage
;  [:http/send-request! {...}]
;
; @usage
;  [:http/send-request! :my-request {...}]
;
; @usage
;  (a/reg-event-fx :my-error-handler    (fn [_ [_ request-id server-response]]))
;  (a/reg-event-fx :my-handler          (fn [_ [_ request-id server-response]]))
;  (a/reg-event-fx :my-progress-handler (fn [_ [_ request-id request-progress]]))
;  [:http/send-request! {:error-handler-event    :my-error-handler
;                        :handler-event          :my-handler
;                        :method                 :post
;                        :progress-handler-event :my-progress-handler
;                        :uri                    "/my-uri"}]
(a/reg-handled-fx :http/send-request! send-request!)
