
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.footer.subs
    (:require [re-frame.api :refer [r]]))



;; -- Footer-props subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-footer-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ engine-id prop-key]]
  (get-in db [:engines :engine-handler/footer-props engine-id prop-key]))

(defn footer-props-stored?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (some? (get-in db [:engines :engine-handler/footer-props engine-id])))



;; -- Footer lifecycles subscriptions -----------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (r footer-props-stored? db engine-id))
