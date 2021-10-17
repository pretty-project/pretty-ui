
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.17
; Description:
; Version: v0.2.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-specials.api
    (:require [x.app-components.image-loader    :as image-loader]
              [x.app-components.image-preloader :as image-preloader]
              [x.app-components.position-signal :as position-signal]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def image-loader    image-loader/view)
(def image-preloader image-preloader/view)
(def position-signal position-signal/view)
