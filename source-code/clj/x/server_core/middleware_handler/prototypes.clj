

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.middleware-handler.prototypes)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn site-defaults-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) site-defaults
  ;
  ; @return (map)
  ;  {:security (map)
  ;    {:anti-forgery (boolean)}}
  [site-defaults]
  (assoc-in site-defaults [:security :anti-forgery] false))
