
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
  ;  (engine/variable-route-string? "/@app-home/your-route")
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [route-string]
  (string/starts-with? route-string "/@app-home"))

(defn resolve-variable-route-string
  ; @param (string) route-string
  ; @param (string) app-home
  ;
  ; @example
  ;  (engine/resolve-variable-route-string "/@app-home/your-route" "/my-app")
  ;  =>
  ;  "/my-app/your-route"
  ;
  ; @return (boolean)
  [route-string app-home]
  (-> route-string (string/not-starts-with! "/@app-home")
                   (string/starts-with!        app-home)))
