
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.6.8
; Compatibility: x4.4.6



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.api
    (:require [x.app-components.content.views :as content.views]
              [x.app-components.delayer.views :as delayer.views]
              [x.app-components.querier.views :as querier.views]
              [x.app-components.stated        :as stated]
              [x.app-components.subscriber    :as subscriber]
              [x.app-components.value         :as value]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-components.content.views
(def content content.views/component)

; x.app-components.delayer.views
(def delayer delayer.views/component)

; x.app-components.querier
(def querier querier.views/component)

; x.app-components.stated
(def stated stated/component)

; x.app-components.subscriber
(def subscriber subscriber/component)

; x.app-components.value
(def get-metamorphic-value value/get-metamorphic-value)
(def value                 value/component)
