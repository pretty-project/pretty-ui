
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.side-effects
    (:require [mid-fruits.pretty :as pretty]
              [x.server-core.api :as a]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/dump-db!
  (fn [{:keys [db]} _]
      (let [pretty-db (pretty/mixed->string db)]
           (println pretty-db))))
