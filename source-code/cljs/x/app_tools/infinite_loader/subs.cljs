
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.subs
    (:require [x.app-core.api :as a]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [db [_ loader-id]]
  (let [visible? (get-in db [:tools :infinite-loader/data-items loader-id :observer-visible?])]
      ;(= visible? nil) = (= visible? true)
       (= visible? false)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :tools/infinite-observer-hidden? infinite-observer-hidden?)
