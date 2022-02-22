
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: 0.2.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.css-handler.api
    (:require [x.app-environment.css-handler.engine]
              [x.app-environment.css-handler.side-effects :as side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-environment.css-handler.side-effects
(def add-external-css! side-effects/add-external-css!)
(def add-css!          side-effects/add-css!)
(def remove-css!       side-effects/remove-css!)
