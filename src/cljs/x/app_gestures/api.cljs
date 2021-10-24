
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.10
; Description:
; Version: v0.3.2
; Compatibility: x3.9.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.api
    (:require [x.app-gestures.select-handler :as select-handler]
              [x.app-gestures.step-handler   :as step-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-gestures.select-handler
(def get-selected-item-ids select-handler/get-selected-item-ids)
(def selector-nonempty?    select-handler/selector-nonempty?)
(def selector-empty?       select-handler/selector-empty?)
(def selector-enabled?     select-handler/selector-enabled?)
(def selector-disabled?    select-handler/selector-disabled?)
(def item-selected?        select-handler/item-selected?)

(def init-selector!    select-handler/init-selector!)
(def empty-selector!   select-handler/empty-selector!)
(def enable-selector!  select-handler/enable-selector!)
(def disable-selector! select-handler/disable-selector!)
(def select-item!      select-handler/select-item!)

; x.app-gestures.step-handler
(def extended-props->stepper-props step-handler/extended-props->stepper-props)
(def get-steps                     step-handler/get-steps)
(def get-step                      step-handler/get-step)
(def get-current-dex               step-handler/get-current-dex)
(def get-prev-dex                  step-handler/get-prev-dex)
(def get-next-dex                  step-handler/get-next-dex)
(def get-max-dex                   step-handler/get-max-dex)
(def get-current-step              step-handler/get-current-step)
(def max-dex-reached?              step-handler/max-dex-reached?)
(def get-prev-step                 step-handler/get-prev-step)
(def get-next-step                 step-handler/get-next-step)
(def stepper-paused?               step-handler/stepper-paused?)
(def get-step-count                step-handler/get-step-count)
(def get-stepper-state             step-handler/get-stepper-state)
