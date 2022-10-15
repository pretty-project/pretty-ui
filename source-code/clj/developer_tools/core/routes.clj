
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.routes
    (:require [developer-tools.core.views :as core.views]
              [server-fruits.http         :as http]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/html-wrap {:body (core.views/view request)}))
