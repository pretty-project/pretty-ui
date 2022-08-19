

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.cache-handler.helpers
    (:require [server-fruits.http               :as http]
              [x.mid-core.cache-handler.helpers :as cache-handler.helpers]
              [x.server-core.event-handler      :as event-handler :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.cache-handler.helpers
(def cache-control-uri cache-handler.helpers/cache-control-uri)



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
