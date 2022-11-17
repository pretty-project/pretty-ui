
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.footer.events
    (:require [map.api :refer [dissoc-in]]))



;; -- Footer-props events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-footer-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  [db [_ engine-id footer-props]]
  (assoc-in db [:engines :engine-handler/footer-props engine-id] footer-props))

(defn remove-footer-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/footer-props engine-id]))

(defn update-footer-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  [db [_ engine-id footer-props]]
  (update-in db [:engines :engine-handler/footer-props engine-id] merge footer-props))
