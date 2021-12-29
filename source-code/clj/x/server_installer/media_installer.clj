
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-installer.media-installer
    (:require [mid-fruits.candy          :refer [param return]]
              [mongo-db.api              :as mongo-db]
              [x.app-details             :as details]
              [x.server-core.api         :as a :refer [r]]
              [x.server-installer.engine :as engine]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :installer/install-media!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-codename "installing module: media")
      {:db (r engine/->module-installed db :media)}))