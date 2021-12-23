
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.17
; Description:
; Version: v0.2.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.engine
    (:require [mid-fruits.string :as string]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn variable-route-string?
  ; @param (string) route-string
  ;
  ; @example
  ;  (engine/variable-route-string? "/:app-home/your-route")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [route-string]
  (string/starts-with? route-string "/:app-home"))

(defn resolve-variable-route-string
  ; @param (string) route-string
  ; @param (string) app-home
  ;
  ; @example
  ;  (engine/resolve-variable-route-string "/:app-home/your-route" "/my-app")
  ;  =>
  ;  "/my-app/your-route"
  ;
  ; @return (boolean)
  [route-string app-home]
  (-> route-string (string/not-starts-with! "/:app-home")
                   (string/starts-with!        app-home)))

(defn route-props->server-route?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-props
  ;  {:get (function or map)
  ;   :post (function or map)}
  ;
  ; @return (boolean)
  [{:keys [get post]}]
  (boolean (or get post)))

(defn route-props->client-route?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) route-props
  ;
  ; @return (boolean)
  [route-props]
  (let [server-route? (route-props->server-route? route-props)]
       (not server-route?)))
