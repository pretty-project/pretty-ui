
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns iso.engines.engine-handler.routes.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) route-key
  ;
  ; @example
  ; (route-id :my-namespace/my-engine :extended)
  ; =>
  ; :my-namespace.my-engine/extended-route
  ;
  ; @return (keyword)
  [engine-id route-key]
  (if-let [namespace (namespace engine-id)]
          (keyword (str namespace "." (name engine-id)) (str (name route-key) "-route"))
          (keyword (str               (name engine-id)) (str (name route-key) "-route"))))
