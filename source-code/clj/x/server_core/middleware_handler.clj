
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.24
; Description:
; Version: v0.8.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.middleware-handler
    (:require [buddy.auth.backends.session      :refer [session-backend]]
              [buddy.auth.middleware            :refer [wrap-authentication wrap-authorization]]
              [ring.middleware.defaults         :refer [site-defaults wrap-defaults]]
              [ring.middleware.keyword-params   :refer [wrap-keyword-params]]
              [ring.middleware.multipart-params :refer [wrap-multipart-params]]
              [ring.middleware.params           :refer [wrap-params]]
              [ring.middleware.reload           :refer [wrap-reload]]
              [ring.middleware.transit          :refer [wrap-transit-params]]
              [x.server-core.engine             :as engine]))




;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (?)
;  For the buddy authenticating services
(def BACKEND (session-backend))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- site-defaults-prototype
  ; @param (map) site-defaults
  ;
  ; @return (map)
  ;  {:security (map)
  ;    {:anti-forgery (boolean)}}
  [site-defaults]
  (assoc-in site-defaults [:security :anti-forgery] false))



;; -- Middleware --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn middleware
  ; @return (map)
  ;  {:middleware (vector)}
  []
  (let [site-defaults (engine/prot site-defaults site-defaults-prototype)]
       {:middleware [#(wrap-reload           %)
                     #(wrap-keyword-params   %)
                     #(wrap-params           %)
                     #(wrap-transit-params   % {:opts {}})
                     #(wrap-multipart-params %)
                     #(wrap-authentication   % BACKEND)
                     #(wrap-authorization    % BACKEND)
                     #(wrap-defaults         % site-defaults)]}))
