
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.view-handler.transfer
    (:require [re-frame.api      :as r]
              [x.server-core.api :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-transfer! :views/transfer-views!
  {:data-f      (fn [_] (r/subscribed [:views/get-view-screens]))
   :target-path [:views :view-handler/meta-items]})
