
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.media.core.installer
    (:require [plugins.git.api :as git]
              [x.core.api      :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore! "environment/media/temp/*"       "x.media")
  (git/ignore! "environment/media/storage/*"    "x.media")
  (git/ignore! "environment/media/thumbnails/*" "x.media"))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
