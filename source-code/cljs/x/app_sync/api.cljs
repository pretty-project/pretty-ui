
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.api
    (:require [x.app-sync.request-handler.effects]
              [x.app-sync.request-handler.events]
              [x.app-sync.response-handler.events]
              [x.app-sync.request-handler.subs    :as request-handler.subs]
              [x.app-sync.response-handler.events :as response-handler.events]
              [x.app-sync.response-handler.subs   :as response-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-sync.request-handler.subs
(def get-request-status    request-handler.subs/get-request-status)
(def get-request-sent-time request-handler.subs/get-request-sent-time)
(def get-request-activity  request-handler.subs/get-request-activity)
(def request-active?       request-handler.subs/request-active?)
(def get-request-progress  request-handler.subs/get-request-progress)
(def request-sent?         request-handler.subs/request-sent?)
(def request-successed?    request-handler.subs/request-successed?)
(def request-failured?     request-handler.subs/request-failured?)
(def request-aborted?      request-handler.subs/request-aborted?)
(def request-resent?       request-handler.subs/request-resent?)
(def listening-to-request? request-handler.subs/listening-to-request?)

; x.app-sync.response-handler.events
(def clear-request-response! response-handler.events/clear-request-response!)

; x.app-sync.response-handler.subs
(def get-request-response response-handler.subs/get-request-response)
