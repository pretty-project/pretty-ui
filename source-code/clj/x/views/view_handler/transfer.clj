
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.views.view-handler.transfer
    (:require [re-frame.api :as r]
              [x.server-core.api   :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-transfer! :x.views/transfer-views!
  {:data-f      (fn [_] (r/subscribed [:x.views/get-view-screens]))
   :target-path [:x.views :view-handler/meta-items]})
