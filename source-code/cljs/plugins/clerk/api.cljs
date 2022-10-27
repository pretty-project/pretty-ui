
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.clerk.api
    (:require [plugins.clerk.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.clerk.side-effects
(def initialize!    side-effects/initialize!)
(def navigate-page! side-effects/navigate-page!)
(def after-render!  side-effects/after-render!)
