
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.build-handler.subs
    (:require [re-frame.api :as r]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn get-build-version
  ; @usage
  ;  (r get-build-version db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:x.core :build-handler/meta-items :build-version]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; @usage
;  [:x.core/get-build-version]
(r/reg-sub :x.core/get-build-version get-build-version)
