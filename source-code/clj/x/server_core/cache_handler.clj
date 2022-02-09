
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v0.5.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.cache-handler
    (:require [server-fruits.http          :as http]
              [x.mid-core.cache-handler    :as cache-handler]
              [x.server-core.event-handler :as event-handler :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.cache-handler
(def cache-control-uri cache-handler/cache-control-uri)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->app-cached?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [cached-version   (http/request->cookie request "x-app-build")
        current-version @(event-handler/subscribe [:core/get-app-config-item :app-build])]
       (= cached-version current-version)))
