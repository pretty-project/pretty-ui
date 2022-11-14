
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

(defn get-app-build
  ; @usage
  ;  (r get-app-build db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:core :build-handler/meta-items :app-build]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; @usage
;  [:core/get-app-build]
(r/reg-sub :core/get-app-build get-app-build)
