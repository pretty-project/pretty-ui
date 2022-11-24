
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.git.api
    (:require [plugins.git.side-effects :as side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.git.side-effects
(def get-gitignore side-effects/get-gitignore)
(def ignored?      side-effects/ignored?)
(def ignore!       side-effects/ignore!)
