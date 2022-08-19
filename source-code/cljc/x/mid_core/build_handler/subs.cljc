

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.build-handler.subs
    (:require [x.mid-core.event-handler :as event-handler]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn get-app-build
  ; @usage
  ;  (r a/get-app-build db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:core :build-handler/meta-items :app-build]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; @usage
;  [:core/get-app-build]
(event-handler/reg-sub :core/get-app-build get-app-build)
