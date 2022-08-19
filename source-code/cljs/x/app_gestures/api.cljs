

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.api
    (:require [x.app-gestures.step-handler.effects]
              [x.app-gestures.step-handler.events]
              [x.app-gestures.select-handler.events :as select-handler.events]
              [x.app-gestures.select-handler.subs   :as select-handler.subs]
              [x.app-gestures.step-handler.subs     :as step-handler.subs]
              [x.app-gestures.view-handler.events   :as view-handler.events]
              [x.app-gestures.view-handler.subs     :as view-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-gestures.select-handler.events
(def init-select-handler!     select-handler.events/init-select-handler!)
(def empty-select-handler!    select-handler.events/empty-select-handler!)
(def enable-select-handler!   select-handler.events/enable-select-handler!)
(def disable-select-handler!  select-handler.events/disable-select-handler!)
(def select-item!             select-handler.events/select-item!)

; x.app-gestures.select-handler.subs
(def get-selected-item-ids    select-handler.subs/get-selected-item-ids)
(def select-handler-nonempty? select-handler.subs/select-handler-nonempty?)
(def select-handler-empty?    select-handler.subs/select-handler-empty?)
(def select-handler-enabled?  select-handler.subs/select-handler-enabled?)
(def select-handler-disabled? select-handler.subs/select-handler-disabled?)
(def item-selected?           select-handler.subs/item-selected?)

; x.app-gestures.step-handler.subs
(def get-steps        step-handler.subs/get-steps)
(def get-step         step-handler.subs/get-step)
(def get-current-dex  step-handler.subs/get-current-dex)
(def get-prev-dex     step-handler.subs/get-prev-dex)
(def get-next-dex     step-handler.subs/get-next-dex)
(def get-max-dex      step-handler.subs/get-max-dex)
(def get-current-step step-handler.subs/get-current-step)
(def max-dex-reached? step-handler.subs/max-dex-reached?)
(def get-prev-step    step-handler.subs/get-prev-step)
(def get-next-step    step-handler.subs/get-next-step)
(def stepping-paused? step-handler.subs/stepping-paused?)
(def get-step-count   step-handler.subs/get-step-count)

; x.app-gestures.view-handler.events
(def init-view-handler! view-handler.events/init-view-handler!)
(def change-view!       view-handler.events/change-view!)

; x.app-gestures.view-handler.subs
(def get-current-view-id view-handler.subs/get-current-view-id)
