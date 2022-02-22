
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.9.6
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.events
    (:require [x.app-core.api :as a]
              [x.app-db.api   :as db]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-header-title!
  ; @param (metamorphic-content) header-title
  ;
  ; @usage
  ;  (r ui/set-header-title! "My title")
  ;
  ; @return (map)
  [db [_ header-title]]
  (assoc-in db (db/path :ui/header :header-title) header-title))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/set-header-title! "My title"]
(a/reg-event-db :ui/set-header-title! set-header-title!)
