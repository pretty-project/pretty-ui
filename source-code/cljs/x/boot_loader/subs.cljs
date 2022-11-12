
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.subs
    (:require [candy.api      :refer [return]]
              [re-frame.api   :refer [r]]
              [x.app-core.api :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-restart-target
  ; @return (string)
  [db _]
  (if-let [restart-target (get-in db [:boot-loader :restart-handler/meta-items :restart-target])]
          (return restart-target)
          (r x.core/get-app-config-item db :app-home)))
