
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.cache-handler.helpers
    (:require [mid.x.core.cache-handler.helpers :as cache-handler.helpers]
              [re-frame.api                     :as r :refer [r]]
              [server-fruits.http               :as http]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.core.cache-handler.helpers
(def cache-control-uri cache-handler.helpers/cache-control-uri)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->app-cached?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [cached-version   (http/request->cookie request "x-app-build")
        current-version @(r/subscribe [:core/get-app-config-item :app-build])]
       (= cached-version current-version)))
