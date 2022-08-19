
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.subs
    (:require [x.app-core.event-handler :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-debug-mode
  ; @usage
  ;  (r a/get-debug-mode db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:core :debug-handler/meta-items :debug-mode]))

(defn debug-mode-detected?
  ; @usage
  ;  (r a/debug-mode-detected? db)
  ;
  ; @return (boolean)
  [db _]
  (let [debug-mode (r get-debug-mode db)]
       (some? debug-mode)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/get-debug-mode]
(event-handler/reg-sub :core/get-debug-mode get-debug-mode)

; @usage
;  [:core/debug-mode-detected?]
(event-handler/reg-sub :core/debug-mode-detected? debug-mode-detected?)
