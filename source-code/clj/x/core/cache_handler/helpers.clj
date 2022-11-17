
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.cache-handler.helpers
    (:require [http.api                         :as http]
              [mid.x.core.cache-handler.helpers :as cache-handler.helpers]
              [re-frame.api                     :as r :refer [r]]))



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
        current-version @(r/subscribe [:x.core/get-app-config-item :app-build])]
       (= cached-version current-version)))
