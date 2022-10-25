
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.plugin-handler.routes.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) route-key
  ;
  ; @example
  ;  (route-id :my-namespace/my-plugin :extended)
  ;  =>
  ;  :my-namespace.my-plugin/extended-route
  ;
  ; @return (keyword)
  [plugin-id route-key]
  (if-let [namespace (namespace plugin-id)]
          (keyword (str namespace "." (name plugin-id)) (str (name route-key) "-route"))
          (keyword (str               (name plugin-id)) (str (name route-key) "-route"))))
