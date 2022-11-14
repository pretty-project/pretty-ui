
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.components.api
    (:require [x.components.content.views :as content.views]
              [x.components.delayer.views :as delayer.views]
              [x.components.querier.views :as querier.views]
              [x.components.stated        :as stated]
              [x.components.subscriber    :as subscriber]
              [x.components.value.subs    :as value.subs]
              [x.components.value.views   :as value.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.components.content.views
(def content content.views/component)

; x.components.delayer.views
(def delayer delayer.views/component)

; x.components.querier
(def querier querier.views/component)

; x.components.stated
(def stated stated/component)

; x.components.subscriber
(def subscriber subscriber/component)

; x.components.value.subs
(def get-metamorphic-value value.subs/get-metamorphic-value)

; x.components.value.views
(def value value.views/component)
