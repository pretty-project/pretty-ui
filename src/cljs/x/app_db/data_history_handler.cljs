
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.08
; Description:
; Version: v0.2.4
; Compatibility: x4.4.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.data-history-handler
    (:require [x.mid-db.data-history-handler :as data-history-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.data-history-handler
(def get-partition-history      data-history-handler/get-partition-history)
(def get-data-history           data-history-handler/get-data-history)
(def get-last-data-history-item data-history-handler/get-last-data-history-item)
(def get-data-history-result    data-history-handler/get-data-history-result)
(def clear-data-history!        data-history-handler/clear-data-history!)
(def update-data-history!       data-history-handler/update-data-history!)
