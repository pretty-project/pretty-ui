
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.10
; Description:
; Version: v0.3.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.api
    (:require [x.app-gestures.select-handler :as select-handler]
              [x.app-gestures.step-handler   :as step-handler]
              [x.app-gestures.view-handler   :as view-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-gestures.select-handler
(def get-selected-item-ids    select-handler/get-selected-item-ids)
(def select-handler-nonempty? select-handler/select-handler-nonempty?)
(def select-handler-empty?    select-handler/select-handler-empty?)
(def select-handler-enabled?  select-handler/select-handler-enabled?)
(def select-handler-disabled? select-handler/select-handler-disabled?)
(def item-selected?           select-handler/item-selected?)
(def init-select-handler!     select-handler/init-select-handler!)
(def empty-select-handler!    select-handler/empty-select-handler!)
(def enable-select-handler!   select-handler/enable-select-handler!)
(def disable-select-handler!  select-handler/disable-select-handler!)
(def select-item!             select-handler/select-item!)

; x.app-gestures.step-handler
(def extended-props->step-handler-props step-handler/extended-props->step-handler-props)
(def get-steps              step-handler/get-steps)
(def get-step               step-handler/get-step)
(def get-current-dex        step-handler/get-current-dex)
(def get-prev-dex           step-handler/get-prev-dex)
(def get-next-dex           step-handler/get-next-dex)
(def get-max-dex            step-handler/get-max-dex)
(def get-current-step       step-handler/get-current-step)
(def max-dex-reached?       step-handler/max-dex-reached?)
(def get-prev-step          step-handler/get-prev-step)
(def get-next-step          step-handler/get-next-step)
(def stepping-paused?       step-handler/stepping-paused?)
(def get-step-count         step-handler/get-step-count)
(def get-step-handler-state step-handler/get-step-handler-state)

; x.app-gestures.view-handler
(def get-selected-view-id view-handler/get-selected-view-id)
(def init-view-handler!   view-handler/init-view-handler!)
(def change-view!         view-handler/change-view!)
