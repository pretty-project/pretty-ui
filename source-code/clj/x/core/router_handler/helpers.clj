
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.router-handler.helpers
    (:require [re-frame.api                      :as r]
              [reitit.ring                       :as reitit-ring]
              [x.core.middleware-handler.helpers :refer [middleware]]
              [x.core.resource-handler.helpers   :as resource-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [default-routes @(r/subscribe [:x.router/get-default-routes])]
       (apply reitit-ring/routes (conj (resource-handler.helpers/create-resource-handlers)
                                       (reitit-ring/create-default-handler default-routes)))))

(defn router
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [ordered-routes @(r/subscribe [:x.router/get-ordered-routes])]
       ; Disable route conflicts handling:
       ;(reitit-ring/router ordered-routes)

       ; Enable route conflicts handling:
       ; XXX#4006 (source-code/cljs/x/app_router/route_handler/subs.cljs)
       (reitit-ring/router ordered-routes {:conflicts nil})))

(defn ring-handler
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (reitit-ring/ring-handler (router)
                            (options)
                            (middleware)))
