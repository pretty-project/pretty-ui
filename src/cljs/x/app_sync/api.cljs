
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.3.6
; Compatibility: x4.2.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.api
    (:require [x.app-sync.query-handler    :as query-handler]
              [x.app-sync.request-handler  :as request-handler]
              [x.app-sync.response-handler :as response-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-sync.query-handler
(def get-queries      query-handler/get-queries)
(def get-query-answer query-handler/get-query-answer)

; x.app-sync.request-handler
(def get-requests-history  request-handler/get-requests-history)
(def get-request-history   request-handler/get-request-history)
(def get-requests          request-handler/get-requests)
(def get-request-status    request-handler/get-request-status)
(def get-request-activity  request-handler/get-request-activity)
(def get-request-progress  request-handler/get-request-progress)
(def request-successed?    request-handler/request-successed?)
(def request-failured?     request-handler/request-failured?)
(def listening-to-request? request-handler/listening-to-request?)
(def get-request-state     request-handler/get-request-state)
(def clear-request!        request-handler/clear-request!)

; x.app-sync.response-handler
(def get-responses-history response-handler/get-responses-history)
(def get-response-history  response-handler/get-response-history)
(def get-responses         response-handler/get-responses)
(def get-request-response  response-handler/get-request-response)
