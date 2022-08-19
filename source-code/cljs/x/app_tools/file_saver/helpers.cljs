
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.helpers
    (:require [dom.api :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (-> "x-file-saver" dom/get-element-by-id .click))
