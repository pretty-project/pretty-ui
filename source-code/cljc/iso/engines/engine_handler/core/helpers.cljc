
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns iso.engines.engine-handler.core.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) component-key
  ;
  ; @example
  ; (component-id :my-namespace/my-engine :view)
  ; =>
  ; :my-namespace.my-engine/view
  ;
  ; @return (keyword)
  [engine-id component-key]
  ; XXX#5467
  (if-let [namespace (namespace engine-id)]
          (keyword (str namespace "." (name engine-id)) (name component-key))
          (keyword (str               (name engine-id)) (name component-key))))

(defn default-data-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) data-key
  ;
  ; @example
  ; (default-data-path :my-engine :suggestions)
  ; =>
  ; [:engines :engine-handler/suggestions :my-engine]
  ;
  ; @return (vector)
  [engine-id data-key]
  [:engines (keyword "engine-handler" (name data-key)) engine-id])
