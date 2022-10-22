
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.css-handler.subs
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-css-paths
  ; @usage
  ;  (r environment/get-css-paths db)
  ;
  ; @return (maps in vector)
  ;  [{:js-build (keyword)
  ;    :uri (string)}]
  [db _]
  (get-in db [:environment :css-handler/data-items]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/get-css-paths]
(r/reg-sub :environment/get-css-paths get-css-paths)
