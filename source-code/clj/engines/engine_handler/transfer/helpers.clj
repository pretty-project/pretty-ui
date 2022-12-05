
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.transfer.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @example
  ; (transfer.helpers/transfer-id :my-engine)
  ; =>
  ; :my-engine/transfer-engine-props
  ;
  ; @example
  ; (transfer.helpers/transfer-id :my-namespace/my-engine)
  ; =>
  ; :my-namespace.my-engine/transfer-engine-props
  ;
  ; @return (keyword)
  [engine-id]
  (if-let [namespace (namespace engine-id)]
          (keyword (str namespace "." (name engine-id)) "transfer-engine-props")
          (keyword (str               (name engine-id)) "transfer-engine-props")))
