
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.css-handler.subs
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-css-paths
  ; @usage
  ;  (r environment/get-css-paths db)
  ;
  ; @return (maps in vector)
  ;  [{:core-js (string)
  ;    :uri (string)}]
  [db _]
  (get-in db [:environment :css-handler/data-items]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/get-css-paths]
(a/reg-sub :environment/get-css-paths get-css-paths)
