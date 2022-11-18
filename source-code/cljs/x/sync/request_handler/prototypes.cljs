
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.sync.request-handler.prototypes
    (:require [candy.api                     :refer [param]]
              [re-frame.api                  :as r]
              [time.api                      :as time]
              [x.sync.request-handler.config :as request-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request-props
  ;
  ; @return (map)
  ;  {:error-handler-f (function)
  ;   :idle-timeout (ms)
  ;   :progress-handler-f (function)
  ;   :response-handler-f (function)
  ;   :sent-time (string)
  ;   :timeout (ms)}
  [db [_ request-props]]
  (let [request-props (merge {:idle-timeout request-handler.config/DEFAULT-IDLE-TIMEOUT
                              :sent-time    (time/timestamp-string)
                              :timeout      request-handler.config/DEFAULT-REQUEST-TIMEOUT}
                             (param request-props))
        error-handler-f    (fn [request-id server-response]  (r/dispatch [:x.sync/request-failured      request-id request-props server-response]))
        response-handler-f (fn [request-id server-response]  (r/dispatch [:x.sync/request-successed     request-id request-props server-response]))
        progress-handler-f (fn [request-id process-progress] (r/dispatch [:x.core/set-process-progress! request-id process-progress]))]
       (merge request-props {:error-handler-f error-handler-f :progress-handler-f progress-handler-f :response-handler-f response-handler-f})))