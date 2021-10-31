
(ns x.app-controller.controll-handler
    (:require []))


(a/reg-event-fx
  :x.app-controller/use-controller!
  ; @param ()
  ; @param (map)
  ;  {:query (string or vector)
  ;   :on-}
  (fn [{} [_ controller-id controller-props]]))
