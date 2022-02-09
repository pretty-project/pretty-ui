
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.24
; Description:
; Version: v1.0.6
; Compatibility: x4.5.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.router-handler
    (:require [reitit.ring                      :as reitit-ring]
              [x.server-core.event-handler      :as event-handler]
              [x.server-core.middleware-handler :refer [middleware]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [default-routes           @(event-handler/subscribe [:router/get-default-routes])
        resource-handler-options @(event-handler/subscribe [:core/get-resource-handler-options])]
       (reitit-ring/routes (reitit-ring/create-resource-handler resource-handler-options)
                           (reitit-ring/create-default-handler  default-routes))))

(defn router
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [ordered-routes @(event-handler/subscribe [:router/get-ordered-routes])]
      ; Disable route conflicts handling:
      ;(reitit-ring/router ordered-routes)

      ; Enable route conflicts handling:
       (reitit-ring/router ordered-routes {:conflicts nil})))

(defn ring-handler
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (reitit-ring/ring-handler (router)
                            (options)
                            (middleware)))
