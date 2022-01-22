
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.4.2
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.api
    (:require [x.app-sync.query-handler    :as query-handler]
              [x.app-sync.request-handler  :as request-handler]
              [x.app-sync.response-handler :as response-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-sync.query-handler
(def get-query-answer query-handler/get-query-answer)

; x.app-sync.request-handler
(def get-request-status    request-handler/get-request-status)
(def get-request-activity  request-handler/get-request-activity)
(def request-active?       request-handler/request-active?)
(def get-request-progress  request-handler/get-request-progress)
(def request-sent?         request-handler/request-sent?)
(def request-successed?    request-handler/request-successed?)
(def request-failured?     request-handler/request-failured?)
(def request-aborted?      request-handler/request-aborted?)
(def listening-to-request? request-handler/listening-to-request?)
(def get-request-state     request-handler/get-request-state)

; x.app-sync.response-handler
(def get-request-response  response-handler/get-request-response)
