
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns ajax.side-effects
   (:require [ajax.core       :as core]
             [ajax.prototypes :as prototypes]
             [ajax.state      :as state]
             [x.app-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-request!
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:error-handler-f (function)(opt)
  ;   :handler-f (function)(opt)
  ;   :method (keyword)
  ;    :get, :post
  ;   :params (map)(opt)
  ;    Only w/ {:method :post}
  ;   :progress-handler-f (function)(opt)
  ;    Only w/ {:method :post}
  ;   :timeout (ms)(opt)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (defn my-handler-f [request-id server-response])
  ;  (http/send-request! :my-request {:handler-f my-handler-f
  ;                                   :method :post
  ;                                   :uri "/my-uri"})
  [request-id {:keys [method uri] :as request-props}]
  (let [reference (case method :get  (core/GET  uri (prototypes/GET-request-props-prototype  request-id request-props))
                               :post (core/POST uri (prototypes/POST-request-props-prototype request-id request-props)))]
       (swap! state/REQUESTS assoc request-id reference)))

(defn abort-request!
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (http/abort-request! :my-request)
  [request-id]
  (let [reference (get @state/REQUESTS request-id)]
       (core/abort reference)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:http/send-request! :my-request {...}]
(a/reg-fx :http/send-request! send-request!)

; @usage
;  [:http/abort-request! :my-request]
(a/reg-fx :http/abort-request! abort-request!)
