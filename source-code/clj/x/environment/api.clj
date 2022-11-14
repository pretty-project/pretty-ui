
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.api
    (:require [x.environment.crawler-handler.lifecycles]
              [x.environment.crawler-handler.helpers :as crawler-handler.helpers]
              [x.environment.css-handler.events      :as css-handler.events]
              [x.environment.css-handler.subs        :as css-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.environment.crawler-handler.helpers
(def crawler-rules crawler-handler.helpers/crawler-rules)

; x.environment.css-handler.events
(def add-css! css-handler.events/add-css!)

; x.environment.css-handler.subs
(def get-css-paths css-handler.subs/get-css-paths)
