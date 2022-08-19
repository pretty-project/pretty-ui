

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-screen.subs
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn screen-locked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db]
  (let [process-locks (get-in db [:ui :progress-screen/meta-items :process-locks])]
       (some? process-locks)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/screen-locked? screen-locked?)
