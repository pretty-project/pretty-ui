
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.debug-handler.subs
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-debug-mode
  ; @usage
  ;  (r get-debug-mode db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:x.core :debug-handler/meta-items :debug-mode]))

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
;  [:x.core/get-debug-mode]
(r/reg-sub :x.core/get-debug-mode get-debug-mode)

; @usage
;  [:x.core/debug-mode-detected?]
(r/reg-sub :x.core/debug-mode-detected? debug-mode-detected?)
