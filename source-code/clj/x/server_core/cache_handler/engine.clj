
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.cache-handler.engine
    (:require [server-fruits.http              :as http]
              [x.mid-core.cache-handler.engine :as cache-handler.engine]
              [x.server-core.event-handler     :as event-handler :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.cache-handler.engine
(def cache-control-uri cache-handler.engine/cache-control-uri)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->app-cached?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [cached-version   (http/request->cookie request "x-app-build")
        current-version @(event-handler/subscribe [:core/get-app-config-item :app-build])]
       (= cached-version current-version)))
