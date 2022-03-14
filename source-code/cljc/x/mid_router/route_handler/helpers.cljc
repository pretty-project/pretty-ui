
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.route-handler.helpers
    (:require [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn variable-route-string?
  ; @param (string) route-string
  ;
  ; @example
  ;  (route-handler.helpers/variable-route-string? "/@app-home/your-route")
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
  ;  (route-handler.helpers/resolve-variable-route-string "/@app-home/your-route" "/my-app")
  ;  =>
  ;  "/my-app/your-route"
  ;
  ; @return (boolean)
  [route-string app-home]
  (-> route-string (string/not-starts-with! "/@app-home")
                   (string/starts-with!        app-home)))
