
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.subs
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-debug-mode
  ; @usage
  ;  (r get-debug-mode db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:core :debug-handler/meta-items :debug-mode]))

(defn debug-mode-detected?
  ; @usage
  ;  (r debug-mode-detected? db)
  ;
  ; @return (boolean)
  [db _]
  (let [debug-mode (r get-debug-mode db)]
       (some? debug-mode)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/get-debug-mode]
(r/reg-sub :core/get-debug-mode get-debug-mode)

; @usage
;  [:core/debug-mode-detected?]
(r/reg-sub :core/debug-mode-detected? debug-mode-detected?)
