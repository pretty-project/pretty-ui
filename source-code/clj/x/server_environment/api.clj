
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.api
    (:require [x.server-environment.crawler-handler.lifecycles]
              [x.server-environment.crawler-handler.helpers :as crawler-handler.helpers]
              [x.server-environment.css-handler.events      :as css-handler.events]
              [x.server-environment.css-handler.subs        :as css-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-environment.crawler-handler.helpers
(def crawler-rules crawler-handler.helpers/crawler-rules)

; x.server-environment.css-handler.events
(def add-css! css-handler.events/add-css!)

; x.server-environment.css-handler.subs
(def get-css-paths css-handler.subs/get-css-paths)
