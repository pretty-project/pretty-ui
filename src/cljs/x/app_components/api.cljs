
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.6.8
; Compatibility: x4.2.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.api
    (:require [x.app-components.content     :as content]
              [x.app-components.listener    :as listener]
              [x.app-components.stated      :as stated]
              [x.app-components.subscriber  :as subscriber]
              [x.app-components.transmitter :as transmitter]
              [x.app-components.value       :as value]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-components.content
(def extended-props->content-props content/extended-props->content-props)
(def content                       content/view)

; x.app-components.listener
(def extended-props->listener-props listener/extended-props->listener-props)
(def listener                       listener/view)

; x.app-components.stated
(def extended-props->stated-props  stated/extended-props->stated-props)
(def extended-props->stated-props? stated/extended-props->stated-props?)
(def stated                        stated/view)

; x.app-components.subscriber
(def extended-props->subscriber-props subscriber/extended-props->subscriber-props)
(def subscriber                       subscriber/view)

; x.app-components.transmitter
(def transmitter transmitter/view)

; x.app-components.value
(def extended-props->value-props value/extended-props->value-props)
(def get-metamorphic-value       value/get-metamorphic-value)
(def value                       value/view)
